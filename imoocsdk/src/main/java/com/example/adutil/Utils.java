package com.example.adutil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.constant.SDKConstant;

/**
 * Created by dell on 2018/5/29.
 */

public class Utils {


    /**
     * density :在DisplayMetrics类中属性density的值为dpi/160，可用于px与dip的互相转换
     * dpi 系统默认
     * density （dp）=dpi/160 （dp） 可以说，density 的但是dp
     * dp= dpi/160
     *  px 转dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.e("TAG_Density", "dip2px  density : "+scale);
        return (int) (scale*pxValue);

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue/scale);
    }

    public static boolean containString(String source, String destation) {
        if (source.equals("") || destation.equals("")) {
            return false;
        }
        if (source.contains(destation)) {
            return true;
        }
        return false;

    }


    //获取view 出现在屏幕上的百分比
    public static int getVisiblePercent(View pView) {
        if (pView != null && pView.isShown()) {
            DisplayMetrics dm = pView.getContext().getResources().getDisplayMetrics();
            //获取屏幕的宽度
            int displayWidth = dm.widthPixels;
            //新建一个矩形
            Rect rect = new Rect();
            //获取当前view在屏幕上出现的矩形
            pView.getGlobalVisibleRect(rect);
            //判断矩形，距离顶部的距离，和矩形的left小于屏幕的宽度
            if ((rect.top > 0) && (rect.left > displayWidth)) {
                //计算矩形的面积（也就是view 显示在屏幕上的位置）
                double areaVisible = rect.width() * rect.height();
                //计算view的面积
                double areaTotal = pView.getWidth() * pView.getHeight();
                //计算百分比
                return (int) (areaVisible / areaTotal) * 100;

            } else {
                return -1;
            }
        }
        return -1;
    }



    public static boolean canAutoPlay(Context context, SDKConstant.AutoPlaySetting setting) {
        boolean result = true;
        switch (setting) {
            case AUTO_PLAY_3G_4G_WIFI:
                result = true;
                break;
            case AUTO_PLAY_ONLY_WIFI:
                if (isWifiConnected(context)) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case AUTO_PLAY_NEVER:
                result = false;
                break;
        }
        return result;
    }



    //is wifi connected
    public static boolean isWifiConnected(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }






}
