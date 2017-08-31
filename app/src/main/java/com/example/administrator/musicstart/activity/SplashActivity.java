package com.example.administrator.musicstart.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.example.administrator.musicstart.R;
import com.example.administrator.musicstart.application.AppCache;
import com.example.administrator.musicstart.service.PlayService;
import com.example.administrator.musicstart.utils.ServiceUtil;

public class SplashActivity extends Activity {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        init();
        checkService();
    }

    private void init() {
      /*  mIvSplash = (ImageView) findViewById(R.id.iv_splash);*/

    }

    private void checkService() {

        if(AppCache.getPlayService() == null){
            startService();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }

            }, 1000);
        }else{

            startMusicActivity();
            finish();



        }


    }

    private void startMusicActivity() {

        Intent intent = new Intent();
        intent.setClass(this, MusicActivity.class);
        intent.putExtras(getIntent());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);

        PlayService playService = new PlayService();
        AppCache.setPlayService(playService);
        startMusicActivity();
        finish();


    }


    private class PlayServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
    private void startService() {
        boolean isRuning = ServiceUtil.isRuning(getApplication(),"com.example.administrator.musicstart.service.PlayService");
        if(isRuning){
            Log.d("TAG","startService");
            startService(new Intent(getApplicationContext(), PlayService.class));
        }else{
            Log.d("TAG","Servicing");
            stopService(new Intent(getApplicationContext(),PlayService.class));

            Log.d("TAG","startService");
            startService(new Intent(getApplicationContext(), PlayService.class));
        }
    }


}
