package com.instabugdemo;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A JavaScript interface to communicate with the javascript inside a WebView.
 *
 * Created by Nirajan Thapa on 6/6/17.
 */

public class WebAppInterface {

    private static final String TAG = "WebAppInterface";

    public interface javaScriptInterface {
        void handleJavaScriptEvents(String event, JSONObject payload);
    }

    javaScriptInterface javaScriptInterface;

    public void setJavaScriptInterface(javaScriptInterface javaScriptInterface) {
        this.javaScriptInterface = javaScriptInterface;
    }

    public Context context;

    private Handler handler;

    public WebAppInterface(Context context) {
        this.context = context;
        handler = new Handler(this.context.getMainLooper());
    }

    private void runOnUIThread(Runnable runnable) {
        handler.post(runnable);
    }

    @JavascriptInterface
    public void logEvent(String event) {
        Log.d(TAG, "JavaScript Event: " + event);
    }

    @JavascriptInterface
    public void scriptLoadError(String event) {
        Log.d(TAG, "scriptLoadError: " + event);
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.d(TAG, "postMessage: " + message);
        if (message != null && !TextUtils.isEmpty(message)) {
            try {
                final JSONObject jsonObject = new JSONObject(message);
                final String event = jsonObject.getString("event");
                if (javaScriptInterface != null) {
                    // JavaScript methods need to be called on the Main Thread
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            javaScriptInterface.handleJavaScriptEvents(event, jsonObject);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
