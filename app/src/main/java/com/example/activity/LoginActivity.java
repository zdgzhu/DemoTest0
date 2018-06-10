package com.example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.demotest.R;

public class LoginActivity extends AppCompatActivity {

    //自定义登录广播的action
    public static final String LOGIN_ACTION = "com.imooc.action.LOGIN_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
    }


}
