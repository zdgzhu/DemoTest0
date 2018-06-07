package com.example.report;

import com.example.adutil.Utils;
import com.example.module.monitor.Monitor;
import com.example.okhttp.CommonOkHttpClient;
import com.example.okhttp.HttpConstant;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.okhttp.request.CommonRequest;
import com.example.okhttp.request.RequestParams;

import java.util.ArrayList;

public class ReportManager {

    /**
     * 默认的事件回调
     */
    private static DisposeDataHandle handle = new DisposeDataHandle(new DisposeDataListener() {
        @Override
        public void onSuccess(Object responseObj) {

        }

        @Override
        public void onFailure(Object reasonObj) {

        }
    });


    /**
     * 发送视频暂停的monitor
     */
    public static void pauseVideoReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }



        }


    }






}
