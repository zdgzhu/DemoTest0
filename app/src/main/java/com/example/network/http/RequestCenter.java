package com.example.network.http;

import android.support.v4.app.NavUtils;

import com.example.module.recommand.BaseRecommandModel;
import com.example.okhttp.CommonOkHttpClient;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.okhttp.request.CommonRequest;
import com.example.okhttp.request.RequestParams;

/**
 * 所有的请求方法
 */
public class RequestCenter {

    public static void postRequest(String url, RequestParams params, DisposeDataListener listener,Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));

    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null,listener, BaseRecommandModel.class);

    }











}
