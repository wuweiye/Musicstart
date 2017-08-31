package com.example.administrator.musicstart.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.init(this);


    }
}
