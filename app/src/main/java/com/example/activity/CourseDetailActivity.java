package com.example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.demotest.R;

/**
 * 课程详情Activity, 展示课程详情,这个页面要用signalTop模式
 */
public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
    }
}
