package com.android.oz.myleakactiviy;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author O.z Young
 * @date 16/8/29
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
