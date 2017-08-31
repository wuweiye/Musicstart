package com.example.administrator.musicstart.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.enums.PlayModeEnum;
import com.example.administrator.musicstart.receive.NoisyAudioStreamReceiver;
import com.example.administrator.musicstart.utils.MusicUtils;
import com.example.administrator.musicstart.utils.PreferenceUtils;


import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;

/**
 * Created by Administrator on 2017/8/29.
 */

public class PlayService extends Service implements AudioManager.OnAudioFocusChangeListener{

    private static final String TAG = "Service";
    private static final long TIME_UPDATE = 100L;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static List<Music> mMusicList;
    private MediaPlayer mPlayer = new MediaPlayer();
    private IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private Handler mHandler = new Handler();
    private AudioManager mAudioManager;
    private OnPlayerEventListener mListener;
    // 正在播放的歌曲[本地|网络]
    private Music mPlayingMusic;
    // 正在播放的本地歌曲的序号
    private static int mPlayingPosition;
    private long quitTimerRemain;
    private int playState = STATE_IDLE;

    public Music getPlayingMusic() {
        return mPlayingMusic;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicList = AppCache.getMusicList();
        Log.e("TAG",mMusicList.size()+"-----");
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);




    }

    private void listener() {

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("TAG","1111");
                next();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                pause();
                break;
        }
    }

    private void pause() {

        if (!isPlaying()) {
            return;
        }

        mPlayer.pause();
        playState = STATE_PAUSE;
        mHandler.removeCallbacks(mPublishRunnable);
       /* Notifier.showPause(mPlayingMusic);*/
/*        mAudioManager.abandonAudioFocus(this);*/
        /*unregisterReceiver(mNoisyReceiver);*/
        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    public boolean isPlaying() {
        return playState == STATE_PLAYING;
    }
    public boolean isPausing() {
        return playState == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return playState == STATE_PREPARING;
    }


    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }

    /*public void stop() {
        pause();
        stopQuitTimer();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        Notifier.cancelAll();
        AppCache.setPlayService(null);
        stopSelf();
    }*/

    /**
     * 扫描音乐
     */
    public void updateMusicList() {
        MusicUtils.scanMusic(this, mMusicList);
        if (!mMusicList.isEmpty()) {
            mPlayingMusic = (mPlayingMusic == null) ? mMusicList.get(mPlayingPosition) : mPlayingMusic;
        }
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    public void play(int position){

        if(mMusicList.isEmpty()){
            return;
        }

        if(position < 0){
            position = mMusicList.size() - 1;
        }else if(position >= mMusicList.size()-1){
            position = 0;
        }
        mPlayingPosition = position;
        Music music = mMusicList.get(mPlayingPosition);
        PreferenceUtils.saveCurrentSongId(music.getId());
        play(music);
    }

    public void play(Music music){
        mPlayingMusic = music;
        try {
            mPlayer.reset();
            mPlayer.setDataSource(music.getPath());
            mPlayer.prepareAsync();
            playState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);

            listener();
            if (mListener != null) {
                mListener.onChange(music);
            }
          /*  Notifier.showPlay(music);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnPreparedListener mPreparedListener  = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            start();
        }
    };

    private boolean start() {
        mPlayer.start();
        if(mPlayer.isPlaying()){
            playState = STATE_PLAYING;
            mHandler.post(mPublishRunnable);

        }

        return  mPlayer.isPlaying();
    }


    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }


    public PlayService getService(){
            return  PlayService.this;
        }


    public void playPause() {
        if (isPreparing()) {
            return;
        }
        if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            resume();
        } else {
            play(getPlayingPosition());
        }
    }

    private void resume() {
        if (!isPausing()) {
            return;
        }

        if (start()) {
            if (mListener != null) {
                mListener.onPlayerResume();
            }
        }
    }

    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    public void next() {

        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(PreferenceUtils.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition + 1);
                break;
        }
    }

    public void prev() {
        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(PreferenceUtils.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition - 1);
                break;
        }
    }
}
