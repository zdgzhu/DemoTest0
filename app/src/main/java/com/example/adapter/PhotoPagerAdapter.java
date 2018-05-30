package com.example.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.adutil.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by dell on 2018/5/30.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private boolean mIsMatch;
    private ArrayList<String> mData;
    private ImageLoaderUtil mLoader;

    public PhotoPagerAdapter(Context context, ArrayList<String> list, boolean isMatch) {
        mContext = context;
        mData = list;
        mIsMatch = isMatch;
        mLoader = ImageLoaderUtil.getInstance(mContext);
    }



    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
