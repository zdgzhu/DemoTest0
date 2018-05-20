package com.example.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装所有的请求参数到HashMap 参数中
 */
public class RequestParams  {

    //ConcurrentHashMap 是线程安全的
    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();

    public RequestParams() {
        this((Map<String,String>)null);
    }

    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {

        this(new HashMap<String, String>(){

            /**: missing method body, or declare abstract  没找到方法体，或声明为抽象的（方法）
             *
             *  如果只是 put 就会报错
             */

            {
                put(key,value) ;
            }

        });
    }


    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            fileParams.put(key, object);
        }
    }

    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;

    }













}
