package com.example.myfilms;

import android.app.Application;
import android.content.Context;

public final class AppApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
