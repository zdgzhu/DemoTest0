package com.example.okhttp.request;

import android.app.DownloadManager;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * 作用 ：接收请求参数，为我们生成Request 对象
 * 工具类：就用static
 */
public class CommonRequest {


    /**
     * 不带请求头的 Post 请求
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }



    /**
     *  可以带请求头的Post 请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url,RequestParams params,RequestParams headers) {
        //构件体
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //通过请求构件类的build 方法获取到真正的请求对象
        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeader = mHeaderBuild.build();
        Request request = new Request.Builder().url(url)
                .post(mFormBody)
                .headers(mHeader)
                .build();
        return request;

    }


    /**
     * 不带请求头的 Get 请求
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /***
     *  带请求头的 Get 的请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest(String url,RequestParams params,RequestParams headers) {
        //StringBuilder 效率更高
        //行话：get 请求就是 url ？ 后面带参数， uri 问号后面带参数
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        Request request = new Request.Builder()
                //最后会多一个 & ，所有要减去一位
                .url(urlBuilder.substring(0,urlBuilder.length()-1))
                .get()
                .headers(mHeader)
                .build();

        return request;
    }


    /**
     * 视频显示的时候用的
     */
    public static Request createMonitorRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if (params != null && params.hasParams()) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
    }









}
