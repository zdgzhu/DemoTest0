package com.example.view.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demotest.R;
import com.example.view.fragment.BaseFragment;

public class MineFragment extends BaseFragment{

    /**
     * UI
     */
    private View mContextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContextView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        return mContextView;
    }






}
