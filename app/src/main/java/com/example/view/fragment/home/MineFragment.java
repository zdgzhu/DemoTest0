package com.example.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.LoginActivity;
import com.example.activity.SettingActivity;
import com.example.adutil.ImageLoaderUtil;
import com.example.adutil.Utils;
import com.example.demotest.R;
import com.example.manager.UserManager;
import com.example.module.update.UpdateModel;
import com.example.network.http.RequestCenter;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.service.update.UpdateService;
import com.example.share.ShareDialog;
import com.example.util.Util;
import com.example.view.CommonDialog;
import com.example.view.fragment.BaseFragment;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  个人信息
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{

    /**
     * UI
     */
    private View mContextView;
    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mVideoPlayerView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;

    //自定义个广播接收器
    private LoginBroadcastReceiver receiver = new LoginBroadcastReceiver();
    public MineFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        registerBroadcast();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mContextView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        initView();
        return mContextView;
    }

    private void initView() {
        mLoginLayout = (RelativeLayout) mContextView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mLoginedLayout = (RelativeLayout) mContextView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);

        mPhotoView = (CircleImageView) mContextView.findViewById(R.id.photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = (TextView) mContextView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);
        mVideoPlayerView = (TextView) mContextView.findViewById(R.id.video_setting_view);
        mVideoPlayerView.setOnClickListener(this);
        mShareView = (TextView) mContextView.findViewById(R.id.share_imooc_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = (TextView) mContextView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mLoginInfoView = (TextView) mContextView.findViewById(R.id.login_info_view);
        mLoginInfoView.setOnClickListener(this);
        mUserNameView = (TextView) mContextView.findViewById(R.id.username_view);
        mTickView = (TextView) mContextView.findViewById(R.id.tick_view);
        mUpdateView = (TextView) mContextView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);

    }



    @Override
    public void onResume() {
        super.onResume();
        //根据用户信息更新我们的fragment
        if (UserManager.getInstance().hasLogined()) {
            if (mLoginedLayout.getVisibility() == View.GONE) {
                mLoginLayout.setVisibility(View.GONE);
                mLoginedLayout.setVisibility(View.VISIBLE);
                mUserNameView.setText(UserManager.getInstance().getUser().data.name);
                mTickView.setText(UserManager.getInstance().getUser().data.tick);
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //分享网址
            case R.id.share_imooc_view:
                 shareFirend();
                break;

            case R.id.login_layout:
            case R.id.login_view:
                //如果未登录，就跳转到登录界面
                if (!UserManager.getInstance().hasLogined()) {
                    //未登录
                    toLogin();
                }
                break;

            case R.id.my_qrcode_view:

                break;

            case R.id.video_setting_view: //播放设置
                mContext.startActivity(new Intent(mContext, SettingActivity.class));

                break;

            case R.id.update_view: //版本更新

                  requestPermissionsSDCardFrag();

                break;
        }



    }

    @Override
    public void doSDCardPermissionfrag() {
        //检查版本更新
        checkVersion();
    }




    //去登录界面
    private void toLogin() {
        Intent intent = new Intent(mContext,LoginActivity.class);
        mContext.startActivity(intent);

    }

    /**
     * 分享慕课网给好友
     */
    private void shareFirend() {
        ShareDialog dialog = new ShareDialog(mContext,false);
        dialog.setShareType(Platform.SHARE_IMAGE);
        dialog.setShareTitle("慕课网");
        dialog.setShareTitleUrl("http://www.imooc.com");
        dialog.setShareSite("imooc");
        dialog.setShareTitleUrl("http://www.imooc.com");
        dialog.setImagePhoto(Environment.getExternalStorageDirectory()+"/test2.ipg");
        dialog.show();
    }

    //发送版本检查更新的请求
    private void  checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    //说明有新版本，开始下载吧
                    CommonDialog dialog = new CommonDialog(mContext, getString(R.string.update_new_version), getString(R.string.update_title),
                            getString(R.string.update_install), getString(R.string.cancle), new CommonDialog.DialogClickListener() {
                        @Override
                        public void onDialogClick() {
                            Intent intent = new Intent(mContext, UpdateService.class);
                            mContext.startService(intent);
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(mContext, getString(R.string.no_new_version_msg), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }


    //注册广播
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter(LoginActivity.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, filter);

    }
    //销毁一个广播
    private void unregisterBroadcast() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
    }



    /**
     * 自定义一个广播接收器，接收mina发送来的消息，并更新 UI
     */
    private class LoginBroadcastReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            if (UserManager.getInstance().hasLogined()) {
                //更新我们的 fragment
                if (mLoginedLayout.getVisibility() == View.GONE) {
                    mLoginLayout.setVisibility(View.GONE);
                    mLoginedLayout.setVisibility(View.VISIBLE);
                    mUserNameView.setText(UserManager.getInstance().getUser().data.name);
                    mTickView.setText(UserManager.getInstance().getUser().data.tick);
                    ImageLoaderUtil.getInstance(mContext).displayImage(mPhotoView, UserManager.getInstance().getUser().data.photoUrl);
            }
            }
        }


    }


}
