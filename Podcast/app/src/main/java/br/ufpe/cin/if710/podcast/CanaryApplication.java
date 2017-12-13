package br.ufpe.cin.if710.podcast;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Arthur on 12/12/2017.
 */

public class CanaryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
    }
}
