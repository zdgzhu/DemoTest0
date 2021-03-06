package com.example.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.module.recommand.RecommandBodyValue;

import java.util.ArrayList;

public class Util {



    public static int getVersionCode(Context context) {
        int versionCode = 1;

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public static String getVersionName(Context context) {
        String versionName = "1.0.0";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;

    }



    //为ViewPager 结构化数据
    public static ArrayList<RecommandBodyValue> handleData(RecommandBodyValue value) {
        ArrayList<RecommandBodyValue> values = new ArrayList<>();
        String[] titles = value.title.split("@");
        String[] infos = value.info.split("@");
        String[] prices = value.price.split("@");
        String[] texts = value.text.split("@");
        ArrayList<String> urls = value.url;
        int start = 0;
        for (int i = 0; i < titles.length; i++) {
            RecommandBodyValue tempValue = new RecommandBodyValue();
            tempValue.title = titles[i];
            tempValue.info = infos[i];
            tempValue.price = prices[i];
            tempValue.text = texts[i];
            tempValue.url = extractData(urls, start, 3);
            start += 3;
            values.add(tempValue);
        }
        return values;
    }

    private static ArrayList<String> extractData(ArrayList<String> source, int start, int interval) {
        ArrayList<String> tempUrls = new ArrayList<>();
        for (int i = start; i < start + interval; i++) {
            tempUrls.add(source.get(i));
        }
        return tempUrls;

    }


    public static Uri createQQUrl(String qq) {
        String result = "mqqwpa://im/chat?chat_type=wpa&uin=".concat(qq);
        return Uri.parse(result);
    }



    public static void hideSoftInputMethod(Context context, View v) {
        /* 隐藏软键盘 */
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }






}
