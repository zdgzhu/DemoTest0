package com.example.network.http;

import android.support.v4.app.NavUtils;

import com.example.module.recommand.BaseRecommandModel;
import com.example.module.update.UpdateModel;
import com.example.module.user.User;
import com.example.okhttp.CommonOkHttpClient;
import com.example.okhttp.HttpConstant;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.okhttp.request.CommonRequest;
import com.example.okhttp.request.RequestParams;

/**
 * 所有的请求方法
 */
public class RequestCenter {

    public static void getRequest(String url, RequestParams params, DisposeDataListener listener,Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));

    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.HOME_RECOMMAND, null,listener, BaseRecommandModel.class);

    }

    //用户登陆请求
    public static void login(String userName, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", userName);
        params.put("pwd", password);
        RequestCenter.getRequest(HttpConstants.LOGIN, params, listener, User.class);
    }

    /**
     * 应用版本号请求更新
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.CHECK_UPDATE,null,listener,UpdateModel.class);

    }










}
