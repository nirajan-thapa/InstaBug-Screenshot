package com.instabugdemo;

import android.app.Application;
import android.util.Log;

import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.OnSdkDismissedCallback;
import com.instabug.library.bugreporting.model.Bug;
import com.instabug.library.invocation.InstabugInvocationEvent;

/**
 * Created by Nirajan Thapa on 3/18/18.
 */

public class MyApp extends Application {

    private static final String TAG = "MyApp";


    // TODO: Replace with your Instabug API Key
    private static final String INSTABUG_KEY_DEBUG = "";


    @Override
    public void onCreate() {
        super.onCreate();

        // InstaBug initializations
        new Instabug.Builder(this, INSTABUG_KEY_DEBUG)
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setCrashReportingState(Feature.State.DISABLED)
                .build();
        Instabug.setIntroMessageEnabled(false);

        if (BuildConfig.DEBUG)
            Instabug.setDebugEnabled(true);

        Instabug.setPreInvocation(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "PreInvocation InstaBug Running true");
            }
        });
        Instabug.setPreSendingRunnable(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "PreSending InstaBug Running false");
            }
        });
        Instabug.setOnSdkDismissedCallback(new OnSdkDismissedCallback() {
            @Override
            public void onSdkDismissed(DismissType dismissType, Bug.Type bugType){
                // Add some change whenever the sdk is invoked
                Log.d(TAG, "InstaBug SDK Dismissed");
            }
        });
    }
}
