package com.example.administrator.musicstart.activity;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.fragment.PlayFragment;

public class MusicActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private  NavigationView mNavigationView;
    private ImageView mIvMenu;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFLPlayBar;

    private boolean isPlayFragmentShow = false;
    private PlayFragment mPlayFragment;

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

        mFLPlayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayingFragment();
            }
        });
    }

    //显示播放页面
    private void showPlayingFragment() {

       /* if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();*/
        isPlayFragmentShow = true;
    }

    //控件初始化
    private void init() {

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_music);
        mFLPlayBar = (FrameLayout) findViewById(R.id.fl_play_bar);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
