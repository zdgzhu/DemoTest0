package com.example.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adutil.ImageLoaderUtil;
import com.example.demotest.R;
import com.example.module.recommand.RecommandBodyValue;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter  extends BaseAdapter{

    private static final int CARD_COUNT = 4; //表示listview的item 有四种类型
    private static final int VIDEO_TYPE = 0X00; //表示视频类型
    private static final int CARD_SINGLE_PIC = 0x01;//表示单图的item
    private static final int CARD_MULTI_PIC = 0x02; //表示多图的item
    private static final int CARD_PAGER_PIC = 0x03; //表示viewpager 的item

    private LayoutInflater mInflate;
    private Context mContext;
    private ArrayList<RecommandBodyValue> mData;
    //异步图片加载工具
    private ImageLoaderUtil mImagerLoader;
    private ViewHolder mViewHolder;


    public CourseAdapter(Context context, ArrayList<RecommandBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImagerLoader = ImageLoaderUtil.getInstance(mContext);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    //返回当前item的类型
    @Override
    public int getItemViewType(int position) {
        RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        return value.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取数据的type 的类型
        int type = getItemViewType(position);
        //获取对应列表上的数据
        final RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        if (convertView == null) {
            switch (type) {
                case VIDEO_TYPE:
                    convertView = mInflate.inflate(R.layout.item_product_card_single_layout, parent, false);

                    break;

                case CARD_SINGLE_PIC:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_single_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;

                case CARD_MULTI_PIC:
                    convertView = mInflate.inflate(R.layout.item_product_card_single_layout, parent, false);

                    break;

                case CARD_PAGER_PIC:
                    convertView = mInflate.inflate(R.layout.item_product_card_single_layout, parent, false);

                    break;

                default:
                    break;

            }



        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        switch (type) {

            case VIDEO_TYPE:

                break;

            case CARD_SINGLE_PIC:
                mImagerLoader.displayImage(mViewHolder.mLogoView,value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                //为单个ImageView 加载远程图片
                mImagerLoader.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;

            case CARD_MULTI_PIC:

                break;

            case CARD_PAGER_PIC:

                break;

            default:
                break;

        }








        return convertView;
    }



    private static class ViewHolder{
        //所有Card的共同属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有属性
        private RelativeLayout mVieoContentLayout;
        private ImageView mShareView;

        //Video Card外所有Card具有属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;
        //Card multi特有属性
        private LinearLayout mProductLayout;
        //Card single特有属性
        private ImageView mProductView;
        //Card ViewPager特有属性
        private ViewPager mViewPager;
    }



}
