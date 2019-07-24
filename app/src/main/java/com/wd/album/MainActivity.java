package com.wd.album;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView del_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        del_rv = findViewById(R.id.del_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        del_rv.setLayoutManager(linearLayoutManager);
        Adapter adapter = new Adapter(MainActivity.this);
        del_rv.setAdapter(adapter);
        //  git remote add origin
        //  git push -u origin master
        // Branch 'master' set up to track remote branch 'master' from 'origin'.
        // fatal: unable to access 'https://github.com/TrimbleGuan/Album.git/': Failed to connect to github.com port 443: Timed out


//        git init
//        git add README.md
//        git commit -m "first commit"
//        git remote add origin https://github.com/TrimbleGuan/Album2.git
//        git push -u origin master


    }
}
