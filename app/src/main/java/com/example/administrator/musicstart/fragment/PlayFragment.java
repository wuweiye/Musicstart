package com.example.administrator.musicstart.fragment;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.enums.PlayModeEnum;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.PreferenceUtils;
import com.example.administrator.musicstart.utils.ToastUtils;
import com.example.administrator.musicstart.utils.binding.Bind;

import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Created by Administrator on 2017/8/1.
 */

public class PlayFragment extends BaseFragment {

    @Bind(R.id.ll_content)
    private LinearLayout llContent;
    @Bind(R.id.iv_play_page_bg)
    private ImageView ivPlayingBg;
    @Bind(R.id.iv_back)
    private ImageView ivBack;
    @Bind(R.id.tv_title)
    private TextView tvTitle;
    @Bind(R.id.tv_artist)
    private TextView tvArtist;
    @Bind(R.id.vp_play_page)
    private ViewPager vpPlay;
    /*@Bind(R.id.il_indicator)
    private IndicatorLayout ilIndicator;*/
    @Bind(R.id.sb_progress)
    private SeekBar sbProgress;
    @Bind(R.id.tv_current_time)
    private TextView tvCurrentTime;
    @Bind(R.id.tv_total_time)
    private TextView tvTotalTime;
    @Bind(R.id.iv_mode)
    private ImageView ivMode;
    @Bind(R.id.iv_play)
    private ImageView ivPlay;
    @Bind(R.id.iv_next)
    private ImageView ivNext;
    @Bind(R.id.iv_prev)
    private ImageView ivPrev;


    private int mLastProgress;
    /*private AlbumCoverView mAlbumCoverView;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }


    public void onPublish(int progress) {

        if (isAdded()) {
            sbProgress.setProgress(progress);
            /*if (mLrcViewSingle.hasLrc()) {
                mLrcViewSingle.updateTime(progress);
                mLrcViewFull.updateTime(progress);
            }*/
            //更新当前播放时间
            if (progress - mLastProgress >= 1000) {
                tvCurrentTime.setText(formatTime(progress));
                mLastProgress = progress;
            }
        }
    }

    private String formatTime(long time) {
        return formatTime("mm:ss", time);
    }

    public  String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

    @Override
    protected void init() {

        onChange(getPlayService().getPlayingMusic());
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","back");
                onBackPressed();

            }
        });

        ivMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayMode();
            }
        });
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               /* getPlayService().seekTo(i);*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar == sbProgress) {
                    if (getPlayService().isPlaying() || getPlayService().isPausing()) {
                        int progress = seekBar.getProgress();
                        getPlayService().seekTo(progress);
                        /*mLrcViewSingle.onDrag(progress);
                        mLrcViewFull.onDrag(progress);*/
                        tvCurrentTime.setText(formatTime(progress));
                        mLastProgress = progress;
                    } else {
                        seekBar.setProgress(0);
                    }
                }
            }
        });
    }

    public void onChange(Music music) {
        if (isAdded()) {
            onPlay(music);
        }
    }

    private void onPlay(Music music) {

        if (music == null) {
            return;
        }

        tvTitle.setText(music.getTitle());
        tvArtist.setText(music.getArtist());
        sbProgress.setProgress(0);
        sbProgress.setSecondaryProgress(0);
        sbProgress.setMax((int) music.getDuration());
        mLastProgress = 0;
        tvCurrentTime.setText(R.string.play_time_start);
        tvTotalTime.setText(formatTime(music.getDuration()));
       /* setCoverAndBg(music);*/  //设置背景
        // setLrc(music);
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlay.setSelected(true);
            /*mAlbumCoverView.start();*/
        } else {
            ivPlay.setSelected(false);
          /*  mAlbumCoverView.pause();*/
        }
    }

    private void setCoverAndBg(Music music) {
       /* mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music));
        ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));*/
    }

    public PlayService getPlayService(){
        return AppCache.getPlayService();
    }

    public void onPlayerPause() {
        if (isAdded()) {
            ivPlay.setSelected(false);
           /* mAlbumCoverView.pause();*/
        }
    }

    public void onPlayerResume() {
        if (isAdded()) {
            ivPlay.setSelected(true);
           /* mAlbumCoverView.start();*/
        }
    }



    private void next() {
        getPlayService().next();
    }

    private void prev() {
        getPlayService().prev();
    }

    private void play() {
        getPlayService().playPause();
    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(PreferenceUtils.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                ToastUtils.show(R.string.mode_shuffle);
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                ToastUtils.show(R.string.mode_one);
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                ToastUtils.show(R.string.mode_loop);
                break;
        }
        PreferenceUtils.savePlayMode(mode.value());
        initPlayMode();
    }

    private void initPlayMode() {
        int mode = PreferenceUtils.getPlayMode();
        ivMode.setImageLevel(mode);
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
        ivBack.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        }, 300);
    }
}
