package com.example.core;


/**
 * 最终通知 应用层 视频是否成功
 */
public interface AdContextInterface {

    void onAdSuccess();

    void onAdFailed();

    void onClickVideo(String url);


}
