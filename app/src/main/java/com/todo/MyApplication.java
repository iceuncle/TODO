package com.todo;

import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by dzq on 16/7/11.
 */
public class MyApplication extends LitePalApplication {

    private static Context applicationContext;

    public static Context instance() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }


}
