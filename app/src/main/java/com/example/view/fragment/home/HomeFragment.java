package com.example.view.fragment.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.CourseAdapter;
import com.example.demotest.R;
import com.example.module.recommand.BaseRecommandModel;
import com.example.network.http.HttpConstants;
import com.example.network.http.RequestCenter;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.view.fragment.BaseFragment;
import com.example.view.home.HomeHeaderLayout;

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "TAG_HomeFragment";
    private static final int REQUEST_QRCODE = 0x01;
    /**
     * UI
     */
//    private View mContextView;
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
        requestRecommanData();
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

//        RequestCenter.postRequest(HttpConstants.ROOT_URL, null, new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                Log.d(TAG, "首页数据请求成功onSuccess: "+responseObj.toString());
//                Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
////                //完成我们真正的逻辑
////                mRecommandData = (BaseRecommandModel) responseObj;
////                //更新UI
////                showSuccessView();
//
//            }
//
//            @Override
//            public void onFailure(Object reasonObj) {
//                Log.d(TAG, "首页数据请求失败onFailure: "+reasonObj.toString());
//                Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
//                showErrorView();
//            }
//        },null);










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


        } else {
            showErrorView();
        }


    }

    private void showErrorView() {

    }


    @Override
    public void onClick(View v) {

    }


    //listview的监听事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
