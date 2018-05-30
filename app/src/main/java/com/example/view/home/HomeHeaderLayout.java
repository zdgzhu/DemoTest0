package com.example.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.PhotoPagerAdapter;
import com.example.adutil.ImageLoaderUtil;
import com.example.demotest.R;
import com.example.module.recommand.RecommandFooterValue;
import com.example.module.recommand.RecommandHeadValue;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

/**
 * Created by dell on 2018/5/30.
 */

public class HomeHeaderLayout extends RelativeLayout{

    private Context mContext;

    /**
     * UI
     */
    private RelativeLayout mRootView;
    private Banner mBanner;
    private TextView mHotView;
    private PhotoPagerAdapter mAdapter;
    private ImageView[] mImageViews = new ImageView[4];
    private LinearLayout mFootLayout;

    /**
     * data
     */
    private RecommandHeadValue mHeaderValue;
    private ImageLoaderUtil mImageLoader;


    public HomeHeaderLayout(Context context) {
        this(context,null);
    }

    public HomeHeaderLayout(Context context,  RecommandHeadValue headerValue) {
        this(context, null,headerValue);
    }

    public HomeHeaderLayout(Context context, AttributeSet attrs, RecommandHeadValue headValue) {
//        super(context, attrs, headValue);
        super(context,attrs);
        mContext = context;
        mHeaderValue = headValue;
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_home_head_layout, this);
        mBanner = (Banner) findViewById(R.id.pager);
        mHotView = (TextView) mRootView.findViewById(R.id.zuixing_view);//今日最新
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.head_image_one);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.head_image_two);
        mImageViews[2] = (ImageView) mRootView.findViewById(R.id.head_image_three);
        mImageViews[3] = (ImageView) mRootView.findViewById(R.id.head_image_four);
        mFootLayout = (LinearLayout) findViewById(R.id.content_layout);

        mAdapter = new PhotoPagerAdapter(mContext, mHeaderValue.ads, true);

        for (int i = 0; i < mImageViews.length; i++) {
            mImageLoader.displayImage(mImageViews[i], mHeaderValue.middle.get(i));
        }

        for (RecommandFooterValue value : mHeaderValue.footer) {
            mFootLayout.addView(createItem(value));
        }

        mHotView.setText(mContext.getString(R.string.today_zuixing));


    }

    private HomeBottomItem createItem(RecommandFooterValue value) {
        HomeBottomItem item = new HomeBottomItem(mContext, value);
        return item;
    }










}
