package com.example.administrator.musicstart.application;

import android.app.Application;
import android.content.Context;

import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.MusicUtils;
import com.example.administrator.musicstart.utils.PreferenceUtils;
import com.example.administrator.musicstart.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class AppCache {

    private PlayService mPlayService;
    private Context mContext ;
    // 本地歌曲列表
    private final List<Music> mMusicList = new ArrayList<>();

    public AppCache(){

    }

    private static class SingletonHolder{
        private static AppCache sAppCache  = new AppCache();
    }

    private static AppCache getInstance(){
        return SingletonHolder.sAppCache;
    }

    public static void init(Application application){
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();

        ToastUtils.init(mContext);
        PreferenceUtils.init(mContext);
        MusicUtils.scanMusic(mContext,mMusicList);


    }
    public static Context getContext() {
        return getInstance().mContext;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }
    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    public static List<Music> getMusicList() {
        return getInstance().mMusicList;
    }

}
