package com.example.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.base.BaseActivity;
import com.example.adutil.ImageLoaderUtil;
import com.example.demotest.R;
import com.example.module.mina.MinaMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell on 2018/6/13.
 */

public class SystemMsgAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MinaMessage> mListData;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private ImageLoaderUtil mImageloader;

    public SystemMsgAdapter(Context context, ArrayList<MinaMessage> listData) {
        mContext = context;
        mListData = listData;
        inflater = LayoutInflater.from(mContext);
        mImageloader = ImageLoaderUtil.getInstance(mContext);

    }


    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MinaMessage mesage = (MinaMessage) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_message_card_layout, null);
            holder.mDayView = (TextView) convertView.findViewById(R.id.time_view);
            holder.mSiteView = (TextView) convertView.findViewById(R.id.site_view);
            holder.mPhotoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
            holder.mTitleView = (TextView) convertView.findViewById(R.id.title_view);
            holder.mInfoView = (TextView) convertView.findViewById(R.id.info_view);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.mDetailView = (TextView) convertView.findViewById(R.id.xiangqing_viw);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 根据数据初始化item
        holder.mDayView.setText(mesage.dayTime);
        holder.mSiteView.setText(mesage.site);
        holder.mTitleView.setText(mesage.title);
        holder.mInfoView.setText(mesage.info);
        mImageloader.displayImage(holder.mPhotoView, mesage.photoUrl);
        if (TextUtils.isEmpty(mesage.imageUrl)) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
            mImageloader.displayImage(holder.mImageView, mesage.imageUrl);
        }
        if (mesage.type != 3) {
            holder.mDetailView.setText(mContext.getString(R.string.chart_with_me));
        } else {
            holder.mDetailView.setText(mContext.getString(R.string.scan_detail));
        }

        return convertView;
    }

    private static class ViewHolder{
        private TextView mDayView;
        private TextView mSiteView;
        private CircleImageView mPhotoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mDetailView;
        private ImageView mImageView;


    }


}
