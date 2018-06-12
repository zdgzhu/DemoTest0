package com.example.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.application.ImoocApplication;

/**
 * Created by dell on 2018/6/12.
 * 配置文件工具类
 */

public class SPManager {

    private static SharedPreferences sp = null;
    private static SPManager spManager = null;
    private static SharedPreferences.Editor editor = null;

    /**
     * Preference 文件名
     */
    private static final String SHARE_PREFERENCE_NAME = "imooc.pre";
    /**
     * 上次基金更新时间
     */
    public static final String LAST_UPDATE_PRODUCT = "last_update_product";
    public static final String VIDEO_PLAY_SETTING = "video_play_setting"; //保存视频设置
    public static final String IS_SHOW_GUIDE = "is_show_guide";

    private SPManager() {
        sp = ImoocApplication.getInstance().getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SPManager getInstance() {
        if (spManager == null || sp == null || editor == null) {
            spManager = new SPManager();
        }
        return spManager;
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
//        editor.commit();
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public Long getLong(String key, int defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();

    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();

    }

    public float getFloat(String key, int defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public boolean isKeyExist(String key) {
        return sp.contains(key);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }







}
