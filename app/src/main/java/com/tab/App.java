package com.tab;

import android.app.Application;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class App extends Application {
    public static App instance;
    public JCVideoPlayerStandard VideoPlaying;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
