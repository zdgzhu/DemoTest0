package com.example.view.fragment;

import android.app.Activity;
import android.app.Fragment;

import com.example.constant.Constant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class BaseFragment extends Fragment {

    protected Activity mContext;










    public void requestPermissionsSDCardFrag() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Constant.WRITE_READ_EXTERNAL_PERMISSION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    doSDCardPermissionfrag();
                }

            }
        });

    }

    public void requestPermissionsCameraFrag() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Constant.WRITE_READ_EXTERNAL_PERMISSION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    doOpenCamera();
                }

            }
        });

    }

    //处理整个应该中sdCard 的业务
    public void doSDCardPermissionfrag() {

    }

    public void doOpenCamera() {


    }














}
