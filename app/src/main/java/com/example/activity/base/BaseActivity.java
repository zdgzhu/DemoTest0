package com.example.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.constant.Constant;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;


import io.reactivex.functions.Consumer;

/**
 * 所有activity的基类，用来处理一些公共事件，如数据统计
 *
 */
public class BaseActivity extends AppCompatActivity{

    public String TAG = "TAG";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void requestPermissionsSDCardAct() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Constant.WRITE_READ_EXTERNAL_PERMISSION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    doSDCardPermissionAct();
                }

            }
        });

    }

    //处理整个应该中sdCard 的业务
    public void doSDCardPermissionAct() {

    }





}
