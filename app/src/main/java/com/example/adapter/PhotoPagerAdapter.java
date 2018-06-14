package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.activity.CourseDetailActivity;
import com.example.adutil.ImageLoaderUtil;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

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
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView photoView = null;
        if (mIsMatch) {
            photoView = new ImageView(mContext);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    mContext.startActivity(intent);

                }
            });

        } else {
            //这个是图片缩放库
            photoView = new PhotoView(mContext);

        }

        mLoader.displayImage(photoView, mData.get(position));
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
