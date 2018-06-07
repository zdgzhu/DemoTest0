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

    /**
     * send the sus monitor
     */
    public static void susReport(ArrayList<Monitor> monitors, boolean isAuto) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", "0");
                    if (isAuto) {
                        params.put("auto", "1");
                    }
                }
                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the sueReoprt
     */
    public static void sueReport(ArrayList<Monitor> monitors, boolean isFull, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    if (isFull) {
                        params.put("fu", "1");
                    }
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the su report
     */
    public static void suReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (monitor.time == playTime) {
                    if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                        params.put("ve", String.valueOf(playTime));
                    }
                    CommonOkHttpClient.get(
                            CommonRequest.createMonitorRequest(monitor.url, params), handle);
                }
            }
        }
    }

    /**
     * send the clicl full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void fullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the click back full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void exitfullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
                RequestParams params = new RequestParams();
                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
                    params.put("ve", String.valueOf(playTime));
                }
                CommonOkHttpClient.get(
                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }


    /**
     * 发送视频是否正常解析及展示监测
     */
    public static void sendAdMonitor(boolean isPad, String sid, String ie, String appVersion, HttpConstant.Params step, String result) {
        RequestParams params = new RequestParams();
        params.put(HttpConstant.Params.lvs.getKey(), HttpConstant.Params.lvs.getValue());
        params.put(HttpConstant.Params.st.getKey(), HttpConstant.Params.st.getValue());
        params.put(HttpConstant.Params.os.getKey(), HttpConstant.Params.os.getValue());
        params.put(HttpConstant.Params.p.getKey(), HttpConstant.Params.p.getValue());
        params.put(HttpConstant.Params.appid.getKey(), HttpConstant.Params.appid.getValue());
        if (isPad) {
            params.put(HttpConstant.Params.bt_pad.getKey(), HttpConstant.Params.bt_pad.getValue());
        } else {
            params.put(HttpConstant.Params.bt_phone.getKey(), HttpConstant.Params.bt_phone.getValue());
        }
        params.put(step.getKey(),
                step.getValue());
        params.put(HttpConstant.STEP_CD, result);
        params.put(HttpConstant.SID, sid);
        params.put(HttpConstant.IE, ie);
        params.put(HttpConstant.AVS, appVersion);

        CommonOkHttpClient.get(CommonRequest.createGetRequest(HttpConstant.ATM_MONITOR, params), handle);
    }


}
