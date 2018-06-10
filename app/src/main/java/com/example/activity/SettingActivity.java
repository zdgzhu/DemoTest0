package com.example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.demotest.R;

public class SettingActivity extends AppCompatActivity {

    /**
     *  UI
     */
    private RelativeLayout mWifiLayout;
    private RelativeLayout mAlwayLayout;
    private RelativeLayout mNeverLayout;
    private CheckBox mWifiBox,mAlwayBox, mNeverBox;
    private ImageView mBackView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
    }
}
