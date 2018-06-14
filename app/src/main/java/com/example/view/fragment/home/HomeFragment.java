package com.example.view.fragment.home;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.CaptureActivity;
import com.example.activity.PhotoViewActivity;
import com.example.activity.SearchActivity;
import com.example.adapter.CourseAdapter;
import com.example.demotest.R;
import com.example.module.recommand.BaseRecommandModel;
import com.example.module.recommand.RecommandBodyValue;
import com.example.module.recommand.RecommandFooterValue;
import com.example.network.http.HttpConstants;
import com.example.network.http.RequestCenter;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.util.Util;
import com.example.view.fragment.BaseFragment;
import com.example.view.home.HomeHeaderLayout;

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "TAG_HomeFragment";
    private static final int REQUEST_QRCODE = 0x01;
    /**
     * UI
     */

    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    /**
     * data
     */
    private CourseAdapter mAdapter;
    private BaseRecommandModel mRecommandData;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 运行了");
        requestRecommanData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();

        return mContentView;
    }


    private void initView() {
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);

        //启动加载动画 帧动画
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 发送推荐产品请求
     */

    private void requestRecommanData() {
        Log.d(TAG, "requestRecommanData: ");
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d(TAG, "首页数据请求成功onSuccess: ");
                Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                //完成我们真正的逻辑
                mRecommandData = (BaseRecommandModel) responseObj;
                //更新UI
                showSuccessView();

            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.d(TAG, "首页数据请求失败onFailure: "+reasonObj.toString());
                Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
                showErrorView();
            }
        });
    }


    //显示请求成功的UI
    private void showSuccessView() {
        if (mRecommandData.data.list != null && mRecommandData.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            //显示listview
            mListView.setVisibility(View.VISIBLE);
            //为listview 添加header
            mListView.addHeaderView(new HomeHeaderLayout(mContext,mRecommandData.data.head));

            //创建我们的adapter
            Log.d(TAG, "showSuccessView: mRecommandData.size = "+ mRecommandData.data.list.size());
            mAdapter = new CourseAdapter(mContext, mRecommandData.data.list);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    mAdapter.updateAdInScrollView();
                }
            });


        } else {
            showErrorView();
        }


    }

    private void showErrorView() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.qrcode_view:

                requestPermissionsCameraFrag();

                break;

            case R.id.category_view:
                //与我交谈
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Util.createQQUrl("277451977"));
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;

            case R.id.search_view:
                Intent searchIntent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(searchIntent);
                break;

        }

    }


    //listview的监听事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecommandBodyValue value = (RecommandBodyValue) mAdapter.getItem(position - mListView.getHeaderViewsCount());
        if (value.type != 0) {
            Log.e(TAG, "onItemClick:value.url = "+value.url );
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            startActivity(intent);
        }

    }

    @Override
    public void doOpenCamera() {
        super.doOpenCamera();
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent,REQUEST_QRCODE);
    }


}
