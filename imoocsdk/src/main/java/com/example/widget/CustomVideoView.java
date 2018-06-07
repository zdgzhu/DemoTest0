package com.example.widget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.example.adutil.Utils;
import com.example.constant.SDKConstant;
import com.example.core.AdParameters;
import com.example.imoocsdk.R;

import java.io.IOException;


/**
 * 负责视频播放，暂停，事件触发
 */
public class CustomVideoView extends RelativeLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener,
        TextureView.SurfaceTextureListener {


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
    private String mFrameURI;
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
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying()) {
                        /**
                         * 还可以在这里更新progressbar
                         *  通知实现者，我们当前的播放进度,主要是用来发送播中监测，就是我们播放到一定秒数之后，
                         *   向服务器发送一个监测,通知服务器，我们当前的视频，播放到了第几秒
                         */
                        listener.onBufferUpdate(getCurrentPosition());
                        //每隔一秒，会通知外面一次,告知外面播放到了第几秒
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INTVAL);

                    }

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
        registerBroadcastReceiver();


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
    public void isShowFullBtn(boolean isShow) {
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


    /**
     * 复写的方法 是所有的view 都有的方法 ,view 在屏幕上发生改变的时候，会去回调这个方法
     * 在view的显示发生改变时，会回调这个方法，中间实现一些播放暂停的工作逻辑
     *
     * @param changedView
     * @param visibility  这个方法的使用场景，在A activity 中有我们的播放器，这个时候我点了 A activity 中的一个按钮，跳转到了B Activity，那么这个时候
     *                    A activity 这个时候就被挂入到了后台， 那么这个时候的视频播放，也要暂停了，所以需要处理一下如果不处理，后台的播放器还是会继续播放
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && playerState == STATE_PAUSING) {
            //决定是否播放，如果满足一个条件，就表示，我们真正的暂停了
            if (isRealPause() || isComplete()) {
                pause();
            } else {
                //通过条件来判断，是播放还是暂停
                decideCanplay();
            }

        } else {
            pause();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 触摸事件处理, 返回 true ，表示触摸事件传到视频播放器的view，就消耗掉这个事件，主要是防止与父容器的触摸事件产生冲突
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    //设置视频是否静音
    public void mute(boolean mute) {
        isMute = mute;
        if (mediaPlayer != null && this.audioManager != null) {
            float volume = isMute ? 0.0f : 1.0f;
            mediaPlayer.setVolume(volume, volume);
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


    //点击事件， 点击播放，点击到全屏等等
    @Override
    public void onClick(View v) {
        if (v == this.mMiniPlayBtn) {
            if (this.playerState == STATE_PAUSING) {
                if (Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                    resume();
                    this.listener.onClickPlay();
                }
            } else {
                load();
            }
        } else if (v==this.mFullBtn){
          this.listener.onClickFullScreenBtn();
        } else if (v == mVideoView) {
            this.listener.onClickVideo();
        }

    }

    /**
     * 异步告诉我们，mediaPlayer 处于prepared 状态，这个时候可以调用start播放视频
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
//        LogUtils.i(TAG, "onPrepared");
        showPlayView();
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            //加载成功之后，就监听他的一个缓存进度
            mediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;//加载成功之后，加重置次数 置为零 ，因为加载成功了，要是再加载，需要从0开始
            if (listener != null) {
                //通知外部，视频加载成功了
                listener.onAdVideoLoadSuccess();
            }
            //准备工作完成之后，就可以进入播放或者暂停状态了，为什么不是直接进入播放或者暂停状态，是因为我们的视频播放，有一定的条件限制，这里主要有两个条件
            //条件 1： view 在我们屏幕中显示 超过50% ，条件2 ：用户的设置，与我们当前的网络状态是一致的，才可以播放
            //满足自动播放条件，则直接播放
            if (Utils.canAutoPlay(getContext(),
                    AdParameters.getCurrentSetting()) &&
                    Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                setCurrentPlayState(STATE_PAUSING);
                //来回切换页面时 ，只有> 50
                resume();
            } else {
                setCurrentPlayState(STATE_PLAYING);
                pause();
            }
        }
    }

    /**
     * 发生异常的时候，让程序去正常的运行
     *
     *  @return true :=android 系统会认为，用户自己处理的异常，系统就不会再帮我们去处理，同时，fasle 就是默认值，Android系统会认为，你的mediaplayer
     *  并没有去处理这个error 类型的事件，它会帮你去做一些默认的处理，但是通常，我们是自去处理的
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        this.playerState = STATE_ERROR;
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        //判断我们当前的加载次数，有没有大于，。 LOAD_TOTAL_COUNT，如果大于，就表示真正的发生了错误
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            //同时显示pause 状态下的相关的view
            showPauseView(false);
            if (this.listener != null) {
                //通知我们的外部
                listener.onAdVideoLoadFailed();
            }
        }
        /**然后调用stop ，让他去重新加载
         *  加载的次数已经超过三次了，为什么还要调用stop去重新加载，去看stop的代码，就会发现，这时调用stop，只会是去清空mediaplayer，并不会去重新
         *  加载
         */
        this.stop();//去重新load
        return true;

    }


    /**
     * 视频播放完成之后，就会回调这个方法，
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (listener != null) {
            //通知外部，当前的视频播放完成了
            listener.onAdVideoLoadComplete();
        }
        /**
         * 表示当前的视频，是真正的处于暂停的状态，我们的暂停有两种，一种是：我们划出屏幕以后，自动暂停，像这种，我们回来是要自动播放的，第二种，
         * 播放完成之后，进入到真正的暂停状态，这种状态下，我们回来是不能进行自动播放的，现在通过一个变量来标记我们是真正的进入到暂停了
         */
        setIsComplete(true);
        setIsRealPause(true);
        //回到初始状态
        playBack();

    }

    /**
     * 在视频不断缓冲过程中被调用，视频非常大的话，会去分段缓存的，每缓存一段，就会调用一次这个方法
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //默认返回false
        return true;
    }
    public void setDataSource(String url) {
        this.mUrl = url;
    }

    public void setFrameURI(String url) {
        mFrameURI= url;
    }

    /**
     * 加载我们的视频 url
     * 这个方法什么时候去调用呢？？ 在构造方法中去调用它可以吗 ？ 其实这个时候是不可以的，为什么呢？因为我们数据的显示是在textureView 可用的时候，
     * 我们才能真正的去显示出数据来，在构造方法刚完成的时候，去调用，可能我们的textureView并没有准备好，所以在这个时候去调用，并没有一个真正有效的
     * TextureView ,所以我们要在 onSurfaceTextureAvailable 中去调用
     */
    public void load() {
        if (this.playerState != STATE_IDLE) {
            return;
        }
        showLoadingView(); //加载视频的动画，告诉用户，正在加载视频
        //设置当前的状态
        setCurrentPlayState(STATE_IDLE);
        //检查我们的播放器是否为空，如果为空，就去创建播放器

        try {
            checkMediaPlayer();
            mute(true);//表示静音
            //设置资源，设置URL
            mediaPlayer.setDataSource(this.mUrl);
            //开始异步加载   mediaPlayer.prepare();是同步加载，不建议使用
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "开始异步加载 视频 load: "+e.getMessage() );
//            e.printStackTrace();
            //加载过程出现异常怎么办？调用stop重新加载
            stop();
        }
    }

    // Pause  暂停视频的播放
    private void pause() {
        //如果当前的状态，不是播放的状态，就不需要暂停了，直接就返回了
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        //设置当前的状态 ,正常执行暂停状态
        setCurrentPlayState(STATE_PAUSING);
        //如果当前的视频正在播放，就将视频暂停
        if (isPlaying()) {
            mediaPlayer.pause();
            if (!this.canPlay) {
                this.mediaPlayer.seekTo(0);
            }
        }
        //显示暂停时的view
        this.showPauseView(false);
        //移除我们发送的事件
        mHandler.removeCallbacksAndMessages(null);

    }

    //全屏不显示暂停状态,后续可以整合，不必单独出一个方法
    public void pauseForFullScreen() {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mediaPlayer.pause();
            if (!this.canPlay) {
                mediaPlayer.seekTo(0);

            }
        }
        //移除所有的消息
        mHandler.removeCallbacksAndMessages(null);
    }

    //当前播放的时间
    public int getCurrentPosition() {
        if (this.mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    //视频的总时长
    public int getDuration() {
        if (this.mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 跳到 指定点 播放 视频 ，跳转 并 播放
     * 由于我们要从小屏跳到全屏播放的功能，所以需要这个方法，而跳转过去之后，是需要续播的，
     * 比如在小屏播放到了第5秒，跳转到大屏，我们就应该从第五秒开始继续播放
     */
    public void seekAndResume(int postion) {
        if (mediaPlayer != null) {
            showPauseView(true);
            entryResumeState();
            mediaPlayer.seekTo(postion);
            /**
             * 因为seekTo 在某些机型上，不一定是在start之前完成，所以，为了兼容性，选择了使用setOnSeekCompleteListener
             */
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.start();
                    mHandler.sendEmptyMessage(TIME_MSG);
                }
            });
        }
    }

    /**
     * 跳到指定点暂停视频 跳转并暂停，这个方法其实与pause 差不多
     */
    public void seekAndPause(int position) {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        //先实现暂停相关的view
        showPauseView(false);
        //设置当前的状态值
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            //与 pause() 方法 不同的是，这个时候我们传入这个位置点， position=0 ，。seekto() 这个方法是时间不确定的方法，
            mediaPlayer.seekTo(position);

            /**可不可以在 mediaPlayer.seekTo(position); 下面直接执行mediaPlayer.pause();？？  答案是不可以。为什么不可以？
             *这中方式只能适配部分手机，下面的方法能视频所有的手机
             * 这个方法是seekto()的事件监听，seekTo() 这个方法是时间不确定的方法， 所以是通过回调类型的这种方式去执行的
             */
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    /**
     * 恢复视频播放,包括从就绪状态进入播放状态,以及从pause状态进入进入播放状态
     * 一到resume就说明我们的视频 是处于播放中
     */
    public void resume() {
        //如果当前的状态不是暂停状态，就返回
        if (this.playerState != STATE_PAUSING) {
            return;
        }
        if (!isPlaying()) {
            //重置播放状态中的变量值
            entryResumeState();
            mediaPlayer.setOnSeekCompleteListener(null);
            //真正的进入播放
            mediaPlayer.start();
            //开始发送我们的事件了
            mHandler.sendEmptyMessage(TIME_MSG);
            showPauseView(true);

        } else {
            showPauseView(false);
        }
    }




    /**
     * 进入播放状态时的状态更新
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



    /**播放完成后回到初始状态 ,我们这里的处理逻辑是: 在播放完成之后，不把我们的播放器销毁，而是将我们的这个
     * 播放流跳转到我们的零，然后让他处于一个暂停状态，这样我们在下次播放的时候，就不需要耗费流量去加载视频了
     * 调用这个方法，其实已经播放完成了一次视频，这个方法和stop有些类似，但是 ， 这里不会将播放视频置为空，为什么，因为我们已经播放完一次了，
     * 如果用户想看第二次，那么，我们没有销毁播放器的话，那么用户可以直接去播放了，不需要重新load，这样既节省流量，也节省时间，还提高的效率
     */
    private void playBack() {
        //这个方法最终是要进入一个暂停的状态，所以呢，先将当前的状态设置为暂停
        setCurrentPlayState(STATE_PAUSING);
        //暂停设置好了之后，同样的，移除我们的handler。就是移除事件的发送
        mHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            //然后在我们mediaplayer不为空的时候，将他重置， 如何重置，很简单，调用seekto方法，让他跳转到我们的第 0 秒，然后进入一个暂停状态
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
        }
        //最后显示 视频暂时的画面view
        this.showPauseView(false);
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


    /**
     * 销毁我们当前的自定义view
     * 销毁我们的VideoView ,不仅会销毁我们的mediaPlayer,而且会销毁我们的事件监听
     */
    public void destroy() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        setCurrentPlayState(STATE_IDLE);
        mCurrentCount = 0;
        setIsComplete(false);
        setIsRealPause(false);
        unRegisterBroadcastReceiver();
        mHandler.removeCallbacksAndMessages(null);
        //除了播放和loading外其余任何状态都显示pause
        showPauseView(false);
    }

    /**
     * 设置监听， 当我们对应事件发生之后，会调用listener当中相关的方法，来通知外界，我们当前播放器
     * 产生了哪个事件，
     */
    public void setListener(ADVideoPlayerListener listener) {
        this.listener = listener;
    }

    public void setFrameLoadListener(ADFrameImageLoadListener frameLoadListener) {
        this.mFrameLoadListener = frameLoadListener;
    }

    private synchronized void checkMediaPlayer() {
        if (mediaPlayer == null) {
            //每次都重新创建一个新的播放器
            mediaPlayer = createMediaPlayer();
        }
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


    //暂停的时候，显示暂停的画面 false 是显示暂停view  ，true是隐藏view
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
    private void loadFrameImage() {
        if (mFrameLoadListener != null) {
            mFrameLoadListener.onStartFrameLoad(mFrameURI, new ImageLoaderListener() {
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




    /**
     * 最重要的是这个方法，只有TextureView 处于Available 的时候，才能为他加载帧数据,否则是一片黑屏
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //在这里创建真正的预览 Surface
        videoSurface = new Surface(surface);
        checkMediaPlayer();
        //设置预览的界面
        mediaPlayer.setSurface(videoSurface);
        load();
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

    //注册
    private void registerBroadcastReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    //解除Receiver
    private void unRegisterBroadcastReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);

        }
    }

    //判断view 的显示在屏幕上的面积，超过50%的时候，才考虑重新播放，否则就处于暂停状态
    private void decideCanplay() {
        if (Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
            //来回切换页面时，只有 >50,且满足自动播放条件才自动播放
            resume();
        } else {
            pause();
        }
    }

    /**
     * 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //主动锁屏是pause ，主动解锁时 resume
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    //如果用户解锁，并且回到了我的app，那么就需要重新播放我们的视频，
                    if (playerState == STATE_PAUSING) {
                        //判断是否是真正的暂停了
                        if (mIsRealPause) {
                            //手动点的暂停，返回来之后，还是要暂停
                            pause();
                        } else {
                            //判断view 的显示在屏幕上的面积，超过50%的时候，才考虑重新播放，否则就处于暂停状态
                            decideCanplay();
                        }

                    }

                    break;

                case Intent.ACTION_SCREEN_OFF:
                    //用户锁屏的时候，我们要暂停视频的播放
                    if (playerState == STATE_PLAYING) {
                        pause();
                    }
                    break;

            }


        }


    }


    /**
     * 供 VideoAdSlot 层来实现具体的点击逻辑，具体逻辑还会变，
     * 如果对UI 的点击没有具体监测的话，可以不回调
     */

    public interface ADVideoPlayerListener {
        //视频播放器播放到了第几秒
        public void onBufferUpdate(int time);
        //点击全屏按钮的时候，要跳转到全屏播放的事件监听
        public void onClickFullScreenBtn();
        //点击我们视频区域的时候的事件监听
        public void onClickVideo();

        public void onClickBackBtn();

        public void onClickPlay();
        //视频加载成功的事件监听
        public void onAdVideoLoadSuccess();
        //视频加载失败的事件监听
        public void onAdVideoLoadFailed();
        //我们视频播放完成的事件监听
        public void onAdVideoLoadComplete();

    }

    public interface ADFrameImageLoadListener {

        void onStartFrameLoad(String url, ImageLoaderListener listener);
    }

    public interface ImageLoaderListener {
        /**
         * 如果图片下载不成功，传null
         */
        void onLoadingComplete(Bitmap loadedImage);
    }


}
