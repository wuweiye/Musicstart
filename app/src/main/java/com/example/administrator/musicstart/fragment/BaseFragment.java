package com.example.administrator.musicstart.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.administrator.musicstart.bean.Music;
import com.example.administrator.musicstart.utils.binding.ViewBinder;

/**
 * Created by Administrator on 2017/8/29.
 */

public abstract class BaseFragment extends Fragment {

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ViewBinder.bind(this, view);
        init();
        setListener();
    }

    protected abstract void init();

    protected abstract void setListener();



    /*protected PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }*/

}
