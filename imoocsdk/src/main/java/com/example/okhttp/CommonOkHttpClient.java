package com.example.okhttp;

import com.example.okhttp.https.HttpsUtils;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 用来发送get，post请求，包括设置一些请求的公用参数
 * https 的支持
 */
public class CommonOkHttpClient  {

    private static final int TIME_OUT = 30; //超时参数
    private static OkHttpClient mOkHttpClient;

    //为我们的client 去配置参数了
    static {
        //创建我们client对象的构建者
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();


//        okHttpClientBuilder.cookieJar();
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);//连接超时时间，单位是秒
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);//读超时时间
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);//写超时时间
        //默认客户端发送的请求允许重定向，或转发
        okHttpClientBuilder.followRedirects(true);
        //添加https支持 。对所有的https的支持
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //信任所有的https证书，所有返回true
                return true;
            }
        });

        //
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());

        mOkHttpClient = okHttpClientBuilder.build();



    }


    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    //通过构造好的Request ,Callback 去发送请求
    public static Call get(Request request, Callback mCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(mCallback);
        return call;
    }


    public static Call post(Request request, Callback mCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(mCallback);
        return call;
    }


    /**
     * 通过构造好的 Request ,Callback 去发送请求
     * @param request
     * @param handle
     * @return
     */
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }


    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }






    /**
     * 发送具体的http / https 请求
     * @param request
     * @param commCallback
     * @return call
     *  每次页面被销毁。请求应该被取消，提高页面的效率
     */
    public static Call sendRequest(Request request, Callback commCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);
        return call;
    }












}
