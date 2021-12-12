package com.mrmulti.Util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.mrmulti.locale.LocaleManager;

public class ChangelangApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }
}