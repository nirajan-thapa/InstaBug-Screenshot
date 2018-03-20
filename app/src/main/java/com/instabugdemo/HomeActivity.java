package com.instabugdemo;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationMode;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements ShakeDetector.Listener, WebAppInterface.javaScriptInterface {

    private static final String TAG = "HomeActivity";

    private WebView scribbleWebView;
    private WebAppInterface webAppInterface;

    private ShakeDetector shakeDetector;

    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shakeDetector = new ShakeDetector(this);

        scribbleWebView = findViewById(R.id.scribble_webview);
        refreshButton = findViewById(R.id.refresh);

        initializeWebViewSettings();

        loadURL();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadURL();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector.start(sensorManager);
    }

    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
    }

    public void initializeWebViewSettings() {
        WebSettings webSettings = scribbleWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);

        // Set up javascript interface
        webAppInterface = new WebAppInterface(this);
        scribbleWebView.addJavascriptInterface(webAppInterface, "Android");
        webAppInterface.setJavaScriptInterface(this);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
//        webSettings.setAppCachePath(ScribbleChatApplication.getChatApplication().getApplicationContext().getCacheDir().getPath());
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            scribbleWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            scribbleWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        scribbleWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        scribbleWebView.setScrollbarFadingEnabled(true);
        scribbleWebView.requestFocus(View.FOCUS_DOWN);

//        scribbleWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                Timber.d("onJsAlert: " + " URL: " + url + " \n"
//                        + "Message: " + message + " \n"
//                        + "JsResult: " + result.toString());
//                return super.onJsAlert(view, url, message, result);
//            }
//
//            @Override
//            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//                Timber.d("onJsConfirm: " + " URL: " + url + " \n"
//                        + "Message: " + message + " \n"
//                        + "JsResult: " + result.toString());
//                return super.onJsConfirm(view, url, message, result);
//            }
//
//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//                Timber.d("onJsPrompt: " + " URL: " + url + " \n"
//                        + "Message: " + message + " \n"
//                        + "JsResult: " + result.toString());
//                return super.onJsPrompt(view, url, message, defaultValue, result);
//            }
//
//            @Override
//            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
//                Timber.d("onJsBeforeUnload: " + " URL: " + url + " \n"
//                        + "Message: " + message + " \n"
//                        + "JsResult: " + result.toString());
//                return super.onJsBeforeUnload(view, url, message, result);
//            }
//
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Timber.d("onConsoleMessage: " + consoleMessage.message() + " #" + consoleMessage.lineNumber() + " --" + consoleMessage.sourceId());
//                return super.onConsoleMessage(consoleMessage);
//            }
//        });

        // Set up the WebView client
//        externalWebViewClient = new ExternalWebViewClient();
//        externalWebViewClient.setWebViewClientListener(scribblePresenter);
//        scribbleWebView.setWebViewClient(externalWebViewClient);

        scribbleWebView.setWebViewClient(new WebViewClient());
    }

    private void loadURL() {
        Log.d(TAG, "Load URL");
        scribbleWebView.loadUrl("file:///android_asset/threejs_ex.html");
    }

    @Override
    public void handleJavaScriptEvents(String event, JSONObject payload) {
        Log.d(TAG, "handleJavaScriptEvents");
    }

    @Override
    public void hearShake() {
        Instabug.invoke(InstabugInvocationMode.NEW_BUG);
    }
}
