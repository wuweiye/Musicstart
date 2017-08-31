package com.example.administrator.musicstart.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/15.
 */

public class ServiceUtil {

    public static boolean isRuning(Context cxt ,String serviceName){
        ActivityManager mAM = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = mAM.getRunningServices(100);
        //Log.d("TAG","runningServiceInfos.size："+runningServiceInfos.size());
        for (ActivityManager.RunningServiceInfo runningServiceInfo :runningServiceInfos){
            //Log.d("TAG",runningServiceInfo.service.getClassName().toString());
            if(serviceName.equals(runningServiceInfo.service.getClassName())){
                return  true;
            }
        }

        return false;
    }

}
