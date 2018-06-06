package com.example.adutil;

import android.content.Context;
import android.util.Log;

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











}
