package com.example.service.update;

/**
 * Created by dell on 2018/6/12.
 */

public interface UpdateDownloadListener {

    /**
     * 下载请求开始回调
     */
    public void onStarted();

    /**
     * 请求成功，下载前的准备回调
     */
    public void onPrepared(long contentLength, String downloadUrl);
    /**
     * 进度更新回调
     *
     * @param progress
     * @param downloadUrl
     */
    public void onProgressChanged(int progress, String downloadUrl);

    /**
     * 下载过程中暂停的回调
     */
    public void onPaused(int progress,int completeSize,String downloadUrl);

    /**
     * 下载完成回调
     */

    public void onFinished(int completeSize, String downloadUrl);

    /**
     * 下载失败回调
     */
    public void onFailure();



}
