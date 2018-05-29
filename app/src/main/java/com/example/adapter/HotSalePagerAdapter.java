package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adutil.ImageLoaderUtil;
import com.example.demotest.R;
import com.example.module.recommand.RecommandBodyValue;

import java.util.ArrayList;

public class HotSalePagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<RecommandBodyValue> mData;
    private LayoutInflater mInflate;
    private ImageLoaderUtil mImageLoader;

    public HotSalePagerAdapter(Context context, ArrayList<RecommandBodyValue> list) {
        mContext = context;
        mData = list;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
    }

    /** 返回 Integer.MAX_VALUE 表示无限循环
     * 正常应该返回 mData.size
     * @return
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    //从我们容器中移除当前的view
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     * 构建我们viewpager中每一个item
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //拿到当前item的数据 position 可能无限大，所欲需要取余
        final RecommandBodyValue value = mData.get(position % mData.size());
        View rootView = mInflate.inflate(R.layout.item_hot_product_pager_layout, null);
        TextView titleView = (TextView) rootView.findViewById(R.id.title_view);
        TextView infoView = (TextView) rootView.findViewById(R.id.info_view);
        TextView gonggaoView = (TextView) rootView.findViewById(R.id.gonggao_view);
        TextView saleView = (TextView) rootView.findViewById(R.id.sale_num_view);
        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = (ImageView) rootView.findViewById(R.id.image_one);
        imageViews[1] = (ImageView) rootView.findViewById(R.id.image_two);
        imageViews[2] = (ImageView) rootView.findViewById(R.id.image_three);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "暂时还不能跳转", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, CourseDetailActivity.class);
//                intent.putExtra(CourseDetailActivity.COURSE_ID, value.adid);
//                mContext.startActivity(intent);
            }
        });
        //绑定数据到view
        titleView.setText(value.title);
        infoView.setText(value.price);
        gonggaoView.setText(value.info);
        saleView.setText(value.text);
        for (int i = 0; i < imageViews.length; i++) {
            mImageLoader.displayImage(imageViews[i], value.url.get(i));
        }
        //将view 添加到container 中
        container.addView(rootView, 0);
        return rootView;
    }


}
