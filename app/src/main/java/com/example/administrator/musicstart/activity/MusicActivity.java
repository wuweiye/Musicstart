package com.example.administrator.musicstart.activity;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.musicstart.R;

public class MusicActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private  NavigationView mNavigationView;
    private ImageView mIvMenu;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        init();
        listener();
    }

    private void listener() {

        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //控件初始化
    private void init() {

         mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
         mIvMenu = (ImageView) findViewById(R.id.iv_menu);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_music);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
