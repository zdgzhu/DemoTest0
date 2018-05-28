package com.example.adutil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

public class GsonUtils {

    private GsonUtils() {

    }

//    public static <T> T fromJson(String json, Class<T> type) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, type);
//    }

    public static Object fromJson(String json, Class<?>clazz) {
        Gson gson = new Gson();
        Object o = gson.fromJson(json, clazz);
        return o;

    }


    public static <T> List<T> listFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }








}
