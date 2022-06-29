package com.example.photoediter;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;
import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import timber.log.Timber;

@HiltAndroidApp
public class App extends MultiDexApplication {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RxJavaPlugins.setErrorHandler(Timber::w);
        initLog();
    }

    public static App getInstance() {
        return instance;
    }

    private void initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


}
