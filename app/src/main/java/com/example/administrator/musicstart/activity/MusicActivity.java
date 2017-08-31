package com.example.administrator.musicstart.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.adapter.FragmentAdapter;
import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.fragment.LocalMusicFragment;
import com.example.administrator.musicstart.fragment.PlayFragment;
import com.example.administrator.musicstart.service.OnPlayerEventListener;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.ToastUtils;
import com.example.administrator.musicstart.utils.binding.Bind;
import com.example.administrator.musicstart.utils.binding.ViewBinder;

public class MusicActivity extends FragmentActivity implements OnPlayerEventListener, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.navigation_view)
    private  NavigationView mNavigationView;
    @Bind(R.id.iv_menu)
    private ImageView mIvMenu;
    @Bind(R.id.drawer_music)
    private DrawerLayout mDrawerLayout;
    @Bind(R.id.fl_play_bar)
    private FrameLayout mFLPlayBar;
    @Bind(R.id.viewpager)
    private ViewPager mViewPager;
    @Bind(R.id.tv_local_music)
    private TextView tvLocalMusic;
    @Bind(R.id.tv_online_music)
    private TextView tvOnlineMusic;
    @Bind(R.id.iv_play_bar_play)
    private ImageView ivPlayBarPlay;
    @Bind(R.id.iv_play_bar_next)
    private ImageView ivPlayBarNext;
    @Bind(R.id.pb_play_bar)
    private ProgressBar mProgressBar;
    @Bind(R.id.tv_play_bar_title)
    private TextView tvPlayBarTitle;
    @Bind(R.id.tv_play_bar_artist)
    private TextView tvPlayBarArtist;




    private boolean isPlayFragmentShow = false;
    private PlayFragment mPlayFragment;
    private LocalMusicFragment mLocalMusicFragment;
    private PlayFragment playFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        getPlayService().setOnPlayEventListener(this);
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

        tvLocalMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        tvOnlineMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        ivPlayBarPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        ivPlayBarNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }

            
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    tvLocalMusic.setSelected(true);
                    tvOnlineMusic.setSelected(false);
                } else {
                    tvLocalMusic.setSelected(false);
                    tvOnlineMusic.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void next() {
        getPlayService().next();
    }

    private void play() {
       /* ToastUtils.show("play");*/
        getPlayService().playPause();
    }

    //显示播放页面
    private void showPlayingFragment() {

        if (isPlayFragmentShow) {
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
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    public void hidePlayingFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    //控件初始化
    private void init() {

        ViewBinder.bind(this);
        mLocalMusicFragment = new LocalMusicFragment();
        playFragment = new PlayFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(playFragment);
        mViewPager.setAdapter(adapter);
        tvLocalMusic.setSelected(true);


    }

    @Override
    public void onBackPressed() {

        if(mPlayFragment != null && isPlayFragmentShow){
            hidePlayingFragment();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null) {
            mPlayFragment.onPublish(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onChange(Music music) {

        onPlay(music);
        Log.e("TAG","start");
        if (mPlayFragment != null) {
            Log.e("TAG","mPlayFragment ");
            mPlayFragment.onChange(music);
        }else {
            Log.e("TAG","mPlayFragment null");
        }

    }

    private void onPlay(Music music) {
        if (music == null) {
            return;
        }

       /* Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        ivPlayBarCover.setImageBitmap(cover);*/
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlayBarPlay.setSelected(true);
        } else {
            ivPlayBarPlay.setSelected(false);
        }
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);

        /*if (mLocalMusicFragment != null) {
            mLocalMusicFragment.onItemPlay();
        }*/
    }

    public PlayService getPlayService(){
        return AppCache.getPlayService();
    }

    @Override
    public void onPlayerPause() {
        ivPlayBarPlay.setSelected(false);

        if (mPlayFragment != null) {
            mPlayFragment.onPlayerPause();
        }

    }

    @Override
    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
        if (mPlayFragment != null) {
            mPlayFragment.onPlayerResume();
        }
    }

    @Override
    public void onTimer(long remain) {

    }

    @Override
    public void onMusicListUpdate() {

    }
}
