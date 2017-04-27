package com.todo;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by dzq on 16/7/11.
 */
public class MyApplication extends Application {

    private static Context applicationContext;

    public static Context instance() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        //初始化litepal
        LitePal.initialize(this);
    }


}
