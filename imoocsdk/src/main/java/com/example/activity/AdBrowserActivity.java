package com.example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.imoocsdk.R;
import com.example.widget.adbrowser.BrowserWebView;

public class AdBrowserActivity extends AppCompatActivity {

    private static final String TAG = "TAG_AdBrowserActivity";
    public static final String KEY_URL = "url";

    private BrowserWebView mAdBrowserWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_browser);
        mAdBrowserWebView = (BrowserWebView) findViewById(R.id.webView);
        if (isValidExtras()) {
            mAdBrowserWebView.loadUrl(mUrl);

        }



    }

    private boolean isValidExtras() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        return !TextUtils.isEmpty(mUrl);

    }


}
