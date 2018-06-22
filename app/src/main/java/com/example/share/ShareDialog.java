package com.example.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constant.Constant;
import com.example.demotest.R;
import com.example.network.http.RequestCenter;
import com.example.okhttp.listener.DisposeDownloadListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ShareDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private DisplayMetrics dm;

    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private RelativeLayout mDownloadLayout;
    private TextView mCancelView;

    /**
     *  share relative
     *  当前支持的数据类型
     */
    private int mShareType; //指定分享类型
    private String mShareTitle;//指定分享内容标题
    private String mShareText; //指定分享内容文本
    private String mSharePhoto;//指定分享本地图片
    private String mShareTitleUrl;
    private String mShareSiteUrl;
    private String mShareSite;
    private String mUrl;
    private String mResourceUrl;

    private boolean isShowDownload;


    public ShareDialog( Context context,boolean isShowDownload) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
        this.isShowDownload = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        /**
         * 通过获取到 dialog 的window 来控制dialog的宽高及位置
         *  我们的对话框在底部，并且宽度是充满屏幕宽度的，所以我们如何让他从底部出现，并且宽度是充满屏幕宽度的？
         * 只设置布局为match_parent是实现不了这个效果的，所以，我们要先获取我们的dialog的window，通过这个window
         * 来去将他指定到我们的BOTTON，就是我们的底部，并且设置它的宽度为手机屏幕的像素宽度，然后为我们的dialog的window
         * 重新设置一下Attributes，才能将我们的对话框设置到窗口底部，并且充满屏幕宽度
         *
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels;// 设置宽度
        dialogWindow.setAttributes(lp);

        mWeixinLayout = (RelativeLayout) findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = (RelativeLayout) findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = (RelativeLayout) findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = (RelativeLayout) findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
        mDownloadLayout = (RelativeLayout) findViewById(R.id.download_layout);
        mDownloadLayout.setOnClickListener(this);
        if (isShowDownload) {
            mDownloadLayout.setVisibility(View.VISIBLE);
        }
        mCancelView = (TextView) findViewById(R.id.cancel_view);
        mCancelView.setOnClickListener(this);

    }

    public void setShareType(int mShareType) {
        this.mShareType = mShareType;
    }

    public void setShareTitle(String mShareTitle) {
        this.mShareTitle = mShareTitle;
    }

    public void setShareText(String mShareText) {
        this.mShareText = mShareText;
    }

    public void setSharePhoto(String mSharePhoto) {
        this.mSharePhoto = mSharePhoto;
    }

    public void setShareTitleUrl(String mShareTitleUrl) {
        this.mShareTitleUrl = mShareTitleUrl;
    }

    public void setImagePhoto(String photo) {
        mSharePhoto = photo;
    }
    public void setShareSiteUrl(String mShareSiteUrl) {
        this.mShareSiteUrl = mShareSiteUrl;
    }

    public void setShareSite(String mShareSite) {
        this.mShareSite = mShareSite;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setResourceUrl(String mResourceUrl) {
        this.mResourceUrl = mResourceUrl;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                Toast.makeText(mContext, "微信被点击", Toast.LENGTH_SHORT).show();
                shareData(ShareManager.PlatformType.WeChat);
                break;

            case R.id.moment_layout:
                Toast.makeText(mContext, "微信朋友圈被点击", Toast.LENGTH_SHORT).show();
                shareData(ShareManager.PlatformType.WeChatMoments);
                break;

            case R.id.qq_layout:
                Toast.makeText(mContext, "QQ被点击", Toast.LENGTH_SHORT).show();
                shareData(ShareManager.PlatformType.QQ);
                break;

            case R.id.qzone_layout:
                Toast.makeText(mContext, "QQ空间被点击", Toast.LENGTH_SHORT).show();
                shareData(ShareManager.PlatformType.QZone);
                break;

            case R.id.cancel_view:
                dismiss();
                break;

            case R.id.download_layout:
                //检查文件夹是否存在
                RequestCenter.downloadFile(mResourceUrl,
                        Constant.APP_PHOTO_DIR.concat(String.valueOf(System.currentTimeMillis())),
                        new DisposeDownloadListener() {
                            @Override
                            public void onSuccess(Object responseObj) {

                                Toast.makeText(mContext,
                                        mContext.getString(R.string.download_success),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Toast.makeText(mContext,
                                        mContext.getString(R.string.download_failed) + mResourceUrl,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onProgress(int progrss) {
                                Log.e("dialog", progrss + "XX");
                            }
                        });
                break;

                default:
                    break;

        }


    }

    private String TAG = "TAG_ShareDialog";
    //分享结果的事件监听
    private PlatformActionListener mListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e(TAG, "onComplete: ");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e(TAG, "onError: throwable = "+throwable);
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e(TAG, "onCancel: " );
        }
    };

    //完成分享的方法
    private void shareData(ShareManager.PlatformType platform) {
        //封装我们的ShareData
        ShareData mData = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTitleUrl);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setUrl(mUrl);
        mData.mPlatformType = platform;
        mData.mShareParams = params;
        ShareManager.getInstance().shareData(mData, mListener);

    }



}
