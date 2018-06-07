package com.example.core.video;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.constant.SDKConstant;
import com.example.module.AdValue;
import com.example.widget.CustomVideoView;
import com.example.report.ReportManager;

/**
 * 视频业务逻辑
 */
public class VideoAdSlot implements CustomVideoView.ADVideoPlayerListener {

    private Context mContext;
    /**
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;

    /**
     *  Data
     */
    private AdValue mXAdInstance;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false;//是否可自动暂停的标志位

    private VideoAdSlot(AdValue adInstance, AdSDKSlotListener slotListener, CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mXAdInstance = adInstance;
        mSlotListener = slotListener;
        mParentView = slotListener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);

    }

    private void initVideoView(CustomVideoView.ADFrameImageLoadListener frameImageLoadListener) {
        mVideoView = new CustomVideoView(mContext, mParentView);
        if (mXAdInstance != null) {
        //这里还有代买没有写

        }
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mVideoView);

    }

    private boolean isPlaying() {
        if (mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    private boolean isRealPause() {
        if (mVideoView != null) {
            return mVideoView.isRealPause();
        }
        return false;
    }

    private boolean isComplete() {
        if (mVideoView != null) {
            return mVideoView.isComplete();
        }
        return false;
    }

    private void pauseVideo(boolean isAuto) {
        if (mVideoView != null) {
            if (isAuto) {
                //发自动暂停的监测
                if (!isRealPause() && isPlaying()) {
                    ReportManager.pauseVideoReport(mXAdInstance.event.pause.content,getPosition());
                }

            }
            mVideoView.seekAndPause(0);
        }

    }



    @Override
    public void onBufferUpdate(int time) {

    }

    private int getPosition() {
        return mVideoView.getCurrentPosition() / SDKConstant.MILLION_UNIT;
    }
    

    @Override
    public void onClickFullScreenBtn() {

    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickBackBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onAdVideoLoadSuccess() {

    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoLoadComplete() {

    }

    //传递消息到 app context 层
    public interface AdSDKSlotListener{

        public ViewGroup getAdParent();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoLoadComplete();

        public void onClickVideo(String url);


    }



}
