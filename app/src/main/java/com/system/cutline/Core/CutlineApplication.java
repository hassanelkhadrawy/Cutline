package com.system.cutline.Core;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;


public class CutlineApplication extends Application {

    private static CutlineApplication instance;

    public static CutlineApplication getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("Application instance has not been initialized!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public boolean isMyServiceRunning(Class<? extends Service> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
