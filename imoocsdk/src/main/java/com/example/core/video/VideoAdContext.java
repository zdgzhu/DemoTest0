package com.example.core.video;

import android.content.Intent;
import android.view.ViewGroup;

import com.example.activity.AdBrowserActivity;
import com.example.adutil.GsonUtils;
import com.example.adutil.Utils;
import com.example.core.AdContextInterface;
import com.example.module.AdValue;
import com.example.okhttp.HttpConstant;
import com.example.report.ReportManager;
import com.example.widget.CustomVideoView;

import static com.example.okhttp.HttpConstant.*;

public class VideoAdContext implements VideoAdSlot.AdSDKSlotListener {

    //the ad container
    private ViewGroup mParentView;

    private VideoAdSlot mAdSlot;
    private AdValue mInstance = null;
    //the listener to the app layer
    private AdContextInterface mListener;
    private CustomVideoView.ADFrameImageLoadListener mFrameLoadListener;

    public VideoAdContext(ViewGroup parentView, String instance,
                          CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        this.mParentView = parentView;
//        this.mInstance = (AdValue) ResponseEntityToModule.
//                parseJsonToModule(instance, AdValue.class);
        this.mInstance = (AdValue) GsonUtils.fromJson(instance, AdValue.class);
        this.mFrameLoadListener = frameLoadListener;
        load();
    }

    /**
     * init the ad,不调用则不会创建videoview
     */
    public void load() {
        if (mInstance != null && mInstance.resource != null) {
            mAdSlot = new VideoAdSlot(mInstance, this, mFrameLoadListener);
            //发送解析成功事件
            sendAnalizeReport(Params.ad_analize, AD_DATA_SUCCESS);
        } else {
            mAdSlot = new VideoAdSlot(null, this, mFrameLoadListener); //创建空的slot,不响应任何事件
            if (mListener != null) {
                mListener.onAdFailed();
            }
            sendAnalizeReport(Params.ad_analize, AD_DATA_FAILED);
        }
    }

    /**
     * release the ad
     */
    public void destroy() {
        mAdSlot.destroy();
    }

    public void setAdResultListener(AdContextInterface listener) {
        this.mListener = listener;
    }

    /**
     * 根据滑动距离来判断是否可以自动播放, 出现超过50%自动播放，离开超过50%,自动暂停
     */
    public void updateAdInScrollView() {
        if (mAdSlot != null) {
            mAdSlot.updateAdInScrollView();
        }
    }

    @Override
    public ViewGroup getAdParent() {
        return mParentView;
    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onAdSuccess();
        }
        sendAnalizeReport(Params.ad_load, AD_PLAY_SUCCESS);
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mListener != null) {
            mListener.onAdFailed();
        }
        sendAnalizeReport(Params.ad_load, AD_PLAY_FAILED);
    }

    @Override
    public void onAdVideoLoadComplete() {
    }

    @Override
    public void onClickVideo(String url) {
        if (mListener != null) {
            mListener.onClickVideo(url);
        } else {
            Intent intent = new Intent(mParentView.getContext(), AdBrowserActivity.class);
            intent.putExtra(AdBrowserActivity.KEY_URL, url);
            mParentView.getContext().startActivity(intent);
        }
    }

    /**
     * 发送广告数据解析成功监测
     */
    private void sendAnalizeReport(Params step, String result) {
        try {
            ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().
                            getApplicationContext()), mInstance == null ? "" : mInstance.resourceID,
                    (mInstance == null ? null : mInstance.adid), Utils.getAppVersion(mParentView.getContext()
                            .getApplicationContext()), step, result);
        } catch (Exception e) {

        }
    }







}
