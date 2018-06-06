package com.example.widget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.midi.MidiSender;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.constant.SDKConstant;
import com.example.imoocsdk.R;


/**
 * 负责视频播放，暂停，事件触发
 *
 */
public class CustomVideoView extends RelativeLayout implements View.OnClickListener,MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnInfoListener,
        TextureView.SurfaceTextureListener{


    /**
     * Constant
     */
    private static final String TAG = "TAG_CustomVideoView";
    private static final int TIME_MSG = 0x01;//事件类型
    private static final int TIME_INTVAL = 1000;// 事件间隔
    private static final int STATE_ERROR = -1; // 当前的错误状态
    private static final int STATE_IDLE = 0;//空闲状态
    private static final int STATE_PLAYING = 1;// 播放状态
    private static final int STATE_PAUSING = 2;//暂停状态
    //视频加载会失败，如果加载失败超过三次，就表示真正的加载失败
    private static final int LOAD_TOTAL_COUNT = 3;

    /**
     * UI
     */
    //VideoView 要添加到的父容器
    private ViewGroup mParentContainer;
    //当前的布局
    private RelativeLayout mPlayerView;
    //显示数据的帧数
    private TextureView mVideoView;
    //功能按钮
    private Button mMiniPlayBtn;
    private ImageView mFullBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;
    //音频播放器，要控制音量是否静音
    private AudioManager audioManager;
    //这个才是最终要显示 帧数据的类
    private Surface videoSurface;

    /**
     * Data
     */
    private String mUrl; //要加载的视频地址
    private String mFramURI;
    private boolean isMute; //是否 静音
    //屏幕宽高  宽是屏幕的宽，高是按照 16:9 来算的
    private int mScreenWidth, mDestationHeight;

    /**
     * Status 状态保护
     */
    private boolean canPlay = true;
    //当前的播放器是不是真正的进入暂停了
    private boolean mIsRealPause;
    //播放器是否播放完成了
    private boolean mIsComplete;
    private int mCurrentCount;
    //标记当前是处于哪个状态 默认处于空闲状态
    private int playerState = STATE_IDLE;
    //事件监听类，通知外界发生了哪些事件，然后由外界去实现这个监听，并且处理相应的逻辑，属于事件监听回调
    private ADVideoPlayerListener listener;
    //播放的核心类
    private MediaPlayer mediaPlayer;
    //自定义的  接收屏幕是否锁屏，锁屏是要暂停播放的
    private ScreenEventReceiver mScreenReceiver;
    private ADFrameImageLoadListener mFrameLoadListener;
    //每隔 TIME_INVAL （1 秒）发送一个事件
    private Handler mHandler=new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:

                    break;
            }
        }

    };


    //构造方法，主要是实现一些初始化的工作，parentContainer vedioView 要添加的父容器
    public CustomVideoView(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        //音频控制器
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData(); //初始化我们的数据
        initView(); //初始化我们的 View
        //注册 自定义的 ScreenEventReceiver


    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        //主要是初始化屏幕的宽和高 ，宽是默认的
        mScreenWidth = dm.widthPixels;
        //高 是  宽度*常量的9/16
        mDestationHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PRECENT);

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) inflater.inflate(R.layout.xadsdk_video_player, this);
        mVideoView = (TextureView) mPlayerView.findViewById(R.id.xadsdk_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);
        initSmallLayoutMode();
    }

    //小屏模式状态
    private void initSmallLayoutMode() {
        LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayerView.setLayoutParams(params);
        mMiniPlayBtn = (Button) mPlayerView.findViewById(R.id.xadsdk_small_play_btn);
        mFullBtn = (ImageView) mPlayerView.findViewById(R.id.xadsdk_to_full_view);
        mLoadingBar = (ImageView) mPlayerView.findViewById(R.id.loading_bar);
        mFrameView = (ImageView) mPlayerView.findViewById(R.id.framing_view);
        mFullBtn.setOnClickListener(this);
    }

    //view 的一些显示
    private void isShowFullBtn(boolean isShow) {
        mFullBtn.setImageResource(isShow ? R.drawable.xadsdk_ad_mini : R.drawable.xadsdk_ad_mini_null);
        mFullBtn.setVisibility(isShow ? View.VISIBLE : View.VISIBLE);
    }

    //得到当前是否是真正的暂停
    public boolean isRealPause() {
        return mIsRealPause;
    }

    //得到当前是否是真正的完成
    public boolean isComplete() {
        return mIsComplete;
    }








    /** 复写的方法 是所有的view 都有的方法 ,view 在屏幕上发生改变的时候，会去回调这个方法
     * 在view的显示发生改变时，会回调这个方法，中间实现一些播放暂停的工作逻辑
     * @param changedView
     * @param visibility
     *  这个方法的使用场景，在A activity 中有我们的播放器，这个时候我点了 A activity 中的一个按钮，跳转到了B Activity，那么这个时候
     *  A activity 这个时候就被挂入到了后台， 那么这个时候的视频播放，也要暂停了，所以需要处理一下如果不处理，后台的播放器还是会继续播放
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);


    }


    /**
     *  触摸事件处理, 返回 true ，表示触摸事件传到视频播放器的view，就消耗掉这个事件，主要是防止与父容器的触摸事件产生冲突
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    //设置视频是否静音
    public void  mute(boolean mute) {
        isMute = mute;
        if (mediaPlayer != null && this.audioManager != null) {
            float volume = isMute ? 0.0f : 1.0f;
            mediaPlayer.setVolume(volume,volume);
        }

    }

    //判断当前是否处于播放状态
    public boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public boolean isFrameHidden() {
        return mFrameView.getVisibility() == View.VISIBLE ? false : true;
    }

    //设置播放是否完成
    public void setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }

    public void setIsRealPause(boolean isRealPause) {
        this.mIsRealPause = isRealPause;
    }






    @Override
    public void onClick(View v) {

    }

    /**
     * 异步告诉我们，mediaPlayer 处于prepared 状态，这个时候可以调用start播放视频
     */
    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    /**
     * 发生异常的时候，让程序去正常的运行
     *
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }








    /**
     *  视频播放完成之后，就会回调这个方法，
     *
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     *  在视频不断缓冲过程中被调用，视频非常大的话，会去分段缓存的，每缓存一段，就会调用一次这个方法
     *
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //默认返回false
        return true;
    }





    public void  load() {
        if (this.playerState != STATE_IDLE) {
            return;
        }

    }

    /**
     *  进入播放状态时的状态更新
     */
    private void entryResumeState() {
        canPlay = true;
        setCurrentPlayState(STATE_PLAYING);
        setIsRealPause(false);
        setIsComplete(false);
    }

    //设置当前播放器的状态
    private void setCurrentPlayState(int state) {
        playerState = state;
    }



    //创建mediaPlayer
    private MediaPlayer createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        //添加各种listener
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (videoSurface != null && videoSurface.isValid()) {
            //设置surface
            mediaPlayer.setSurface(videoSurface);
        } else {
            stop();
        }
        return mediaPlayer;

    }


    //暂停的时候，显示暂停的画面
    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mMiniPlayBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        if (!show) {
            mFrameView.setVisibility(View.VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(View.GONE);

        }

    }
    //显示加载的时候的view
    private void showLoadingView() {
        mFullBtn.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);
        AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
        anim.start();
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
        loadFrameImage();
    }

    //显示播放的时候的view
    private void showPlayView() {
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
        
    }



    /**
     * 异步加载定帧图
     */
    private void loadFrameImage(){
        if (mFrameLoadListener != null) {
            mFrameLoadListener.onStartFrameLoad(mFramURI, new ImageLoaderListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    if (loadedImage != null) {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mFrameView.setImageBitmap(loadedImage);
                    } else {
                        mFrameView.setScaleType(ImageView.ScaleType.CENTER);
                        mFrameView.setImageResource(R.drawable.xadsdk_img_error);
                    }
                }
            });
        }


    }






    //让我们的播放处于停止状态，这个停止和暂停是不一样的，处于stop状态，只能是重新 prepare
    private void stop() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.reset();
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        //将handler的消息也移除掉
        mHandler.removeCallbacksAndMessages(null);
        //设置当前的 状态为 空闲状态
        setCurrentPlayState(STATE_IDLE);
        //重新加载 load  满足重新加载的条件
        if (mCurrentCount < LOAD_TOTAL_COUNT) {
            mCurrentCount += 1;
            load();
        } else {
            //表示已经重新加载了 ，当加载的次数达到预定的次数时，就停止加载
            showPauseView(false);//显示暂停状态
        }


    }















    /**最重要的是这个方法，只有TextureView 处于Available 的时候，才能为他加载帧数据,否则是一片黑屏
     *
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

        }


    }


    /**
     *  供 VideoAdSlot 层来实现具体的点击逻辑，具体逻辑还会变，
     *  如果对UI 的点击没有具体监测的话，可以不回调
     */

    public interface ADVideoPlayerListener{

        public void onBufferUpdate(int time);

        public void onClickFullScreenBtn();

        public void onClickVideo();

        public void onClickBackBtn();

        public void onClickPlay();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoLoadComplete();

    }

    public interface ADFrameImageLoadListener{

        void onStartFrameLoad(String url,ImageLoaderListener listener);
    }

    public interface ImageLoaderListener{
        /**
         * 如果图片下载不成功，传null
         */
        void onLoadingComplete(Bitmap loadedImage);
    }





}
