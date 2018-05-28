package com.example.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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

    }

    //单例模式
    public static ImoocApplication getInstance() {
        return mApplication;
    }

}
