package com.wd.album.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wd.album.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumImageCaseAdapter extends RecyclerView.Adapter<AlbumImageCaseAdapter.ViewHolder> {
    /* 数据源 */
    private List<String> list;
    private Context context;
    //是否显示单选框,默认false
    private boolean isshowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    //接口实例
    private RecyclerViewOnItemClickListener onItemClickListener;


    public AlbumImageCaseAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        initMap();
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < list.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public int getItemCount() {
        return list !=null ? list.size()+1 :0;
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == list.size()) {
            holder.mineItemImage.setImageResource(R.mipmap.ic_launcher);
            holder.checkBox.setVisibility(View.GONE);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        onItemClickListener.onItemClickListener(v, position);
                    }
                }
            });
        } else {
            String s = list.get(position);
            Glide.with(context).load(s).into(holder.mineItemImage);
            //长按显示/隐藏
            if (isshowBox) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
            //设置checkBox改变监听
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //用map集合保存
                    map.put(position, isChecked);
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            holder.checkBox.setChecked(map.get(position));

            //为Item设置点击事件
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        onItemClickListener.onItemClickListener(v, position);
                    }
                }
            });
            holder.root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //不管显示隐藏，清空状态
                    initMap();
                    return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, position,false);
                }
            });
        }
    }

    // 删除一个数据
    public void removeData(int position) {
        list.remove(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_case_image_item, parent, false);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isshowBox = !isshowBox;
    }

    //取消CheckBox
    public void setShowBox2() {
        //取反
        isshowBox = false;
    }


    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return map;
    }

    //视图管理
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mineItemImage;
        private View root;
        private CheckBox checkBox;

        public ViewHolder(View root) {
            super(root);
            this.root = root;
            mineItemImage = root.findViewById(R.id.mine_item_image);
            checkBox = (CheckBox) root.findViewById(R.id.cb);
        }
    }

    //设置点击事件
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //接口回调设置点击事件
    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);
        //长按事件
        boolean onItemLongClickListener(View view, int position,boolean type);
    }
}
