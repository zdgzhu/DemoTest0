package com.example.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.PhotoPagerAdapter;
import com.example.adutil.Utils;
import com.example.demotest.R;
import com.example.share.ShareDialog;
import com.example.util.Util;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.internal.platform.Platform;

/**
 * 显示产品大图页面
 */
public class PhotoViewActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String PHOTO_LIST = "photo_list";

    /**
     *  UI
     */
    private ViewPager mPager;
    private TextView mIndictorView;
    private ImageView mShareView;

    /**
     *  Data
     */
    private PhotoPagerAdapter mAdapter;
    private ArrayList<String> mPhotoLists;
    private int mLenght;
    private int currentPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_layout);

        initData();
        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        mPhotoLists = intent.getStringArrayListExtra(PHOTO_LIST);
        mLenght = mPhotoLists.size();
    }

    private void initView() {
        mIndictorView = (TextView) findViewById(R.id.indictor_view);
        mIndictorView.setText("1/" + mLenght);
        mShareView = (ImageView) findViewById(R.id.share_view);
        mShareView.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.photo_pager);
        mAdapter = new PhotoPagerAdapter(this, mPhotoLists, false);
        mPager.setPageMargin(Utils.px2dip(this, 30));
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndictorView.setText(String.valueOf((position + 1)).concat("/").
                        concat(String.valueOf(mLenght)));
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       //隐藏软键盘
        Util.hideSoftInputMethod(this, mIndictorView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_view:
//                showShare();
                Toast.makeText(this, "被点击", Toast.LENGTH_SHORT).show();
                ShareDialog dialog = new ShareDialog(this, true);
                dialog.setShareType(cn.sharesdk.framework.Platform.SHARE_IMAGE);
                dialog.setShareTitle(getString(R.string.imooc));
                dialog.setShareTitleUrl(getString(R.string.imooc_site));
                dialog.setShareText(getString(R.string.imooc));
                dialog.setShareSite(getString(R.string.imooc));
                dialog.setShareTitle(getString(R.string.imooc));
                dialog.setImagePhoto(mPhotoLists.get(currentPos));
                dialog.setUrl(mPhotoLists.get(currentPos));
                dialog.setResourceUrl(mPhotoLists.get(currentPos));
                dialog.show();
                break;
        }

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }





}
