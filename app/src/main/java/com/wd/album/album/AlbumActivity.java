package com.wd.album.album;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wd.album.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/*
 * 相册上传
 * */
public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView album_tv;
    private PopupWindow gridGelPw;
    private TextView grid_del_tv;
    private ImageView grid_delImg;
    private TextView grid_addseTv;
    boolean grid_qxboolen;
    private RecyclerView albumRecyc;
    private List<String> list = new ArrayList<>();
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private AlbumImageCaseAdapter adapter;
    boolean popShowBoolean = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initView();
        initData();

    }

    private void initData() {
        adapter = new AlbumImageCaseAdapter(list, AlbumActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumActivity.this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        albumRecyc.setLayoutManager(gridLayoutManager);
        albumRecyc.setAdapter(adapter);
        adapter.setRecyclerViewOnItemClickListener(new AlbumImageCaseAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
//                        //点击事件
//                        //设置选中的项
                if (position == list.size()) {
                    Intent intent5 = new Intent(AlbumActivity.this, MultiImageSelectorActivity.class);
                    // whether show camera
                    intent5.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                    // max select image amount
                    intent5.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                    // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
                    intent5.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    // default select images (support array list)
                    ArrayList<String> imageList = new ArrayList<>();
                    intent5.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, imageList);
                    startActivityForResult(intent5, 5);
                } else {
                    adapter.setSelectItem(position);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public boolean onItemLongClickListener(View view, int position,boolean type) {
                if(type==popShowBoolean){
                    adapter.setShowBox();
                    //设置选中的项
                    adapter.setSelectItem(position);
                    adapter.notifyDataSetChanged();
                    gridGelPw.showAtLocation(albumRecyc, Gravity.BOTTOM, 0, 0);
                    popShowBoolean = true;
                }else{
                    gridGelPw.dismiss();
                    adapter.setShowBox2();
                    adapter.notifyDataSetChanged();
                    popShowBoolean = false;
                }

                return true;
            }
        });
    }

    private void initView() {
        verifyStoragePermissions(AlbumActivity.this);
        try {
            albumRecyc = findViewById(R.id.album_recyc);
            back = findViewById(R.id.back);
            album_tv = findViewById(R.id.album_tv);
            back.setOnClickListener(this);
            album_tv.setOnClickListener(this);
            //加载Grid删除的PopupWindow的布局
            View grid_del_popupView = View.inflate(this, R.layout.grid_popup_del, null);
            //另一种新建方式
            gridGelPw = new PopupWindow(grid_del_popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            grid_del_tv = grid_del_popupView.findViewById(R.id.grid_del_tv);
            grid_delImg = grid_del_popupView.findViewById(R.id.grid_del_img);
            grid_addseTv = grid_del_popupView.findViewById(R.id.grid_addse_tv);
            grid_del_tv.setOnClickListener(this);
            grid_delImg.setOnClickListener(this);
            grid_addseTv.setOnClickListener(this);
            grid_del_tv.setOnClickListener(this);
            grid_delImg.setOnClickListener(this);
            grid_addseTv.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.CAMERA");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            //上传图片
            case R.id.album_tv:

                break;
            case R.id.grid_del_tv:
                //底部取消
                //点击取消关闭del_pw
                gridGelPw.dismiss();
                adapter.setShowBox2();
                adapter.notifyDataSetChanged();
                break;
            case R.id.grid_del_img:
                try {
                    //获取你选中的item
                    Map<Integer, Boolean> map3 = adapter.getMap();
                    int count3 = adapter.getItemCount();
                    for (int i = 0; i < count3; i++) {
                        int position = i - (count3 - adapter.getItemCount());
                        if (map3.get(i)!= null && map3.get(i)) {
                            //长按删除
                            map3.remove(i);
                            adapter.removeData(position);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    adapter.setShowBox2();
                    gridGelPw.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.grid_addse_tv:
                try {
                    if (grid_qxboolen == false) {
                        grid_qxboolen = true;
                        Map<Integer, Boolean> m2 = adapter.getMap();
                        for (int i = 0; i < m2.size(); i++) {
                            if(m2.get(i).booleanValue()==false){
                                m2.put(i, true);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else if (grid_qxboolen == true) {
                        grid_qxboolen = false;
                        Log.e("--------","fdsfsdf");
                        Map<Integer, Boolean> map1 = adapter.getMap();
                        for (int i = 0; i < map1.size(); i++) {
                            map1.put(i, false);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机导入
        if(requestCode == 5){
            if(resultCode == RESULT_OK){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                list.addAll(path);
                if (list.size()!=0){
                    adapter.notifyDataSetChanged();
                }

            }
        }
    }
}
