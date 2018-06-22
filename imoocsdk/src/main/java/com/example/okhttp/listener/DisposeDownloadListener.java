package com.example.okhttp.listener;

public interface DisposeDownloadListener extends DisposeDataListener {
    public void onProgress(int progress);
}
