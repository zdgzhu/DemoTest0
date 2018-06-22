package com.example.application;

import android.app.Application;

import com.example.share.ShareManager;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

/**
 *  1、他是整个程序的入口， 2、初始化工作、
 *  3、为整个应用的其他模块提供上下文
 */
public class ImoocApplication extends Application {


    private static ImoocApplication mApplication = null;

    //这个方法只执行一次
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashHandler.getInstance().init(this);
        initShareSDK();
        initJPush();
        initUMeng();

    }

    //单例模式
    public static ImoocApplication getInstance() {
        return mApplication;
    }


    public void initShareSDK() {
        ShareManager.initSDK(this);
    }

    public void initJPush() {
        // ture 调试模式，上线之后，就调整为false
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    //初始化友盟
    public void initUMeng() {
        //开发过程，设置为true
//        MobclickAgent.setDebugMode(true);
//        MobclickAgent.openActivityDurationTrack(false);
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
//        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5b27dcedf43e482c150001e3");

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
         UMConfigure.setLogEnabled(true);

        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this, null, null, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");

    }

}
