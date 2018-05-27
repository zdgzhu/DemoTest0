package com.example.imoocsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.okhttp.CommonOkHttpClient;
import com.example.okhttp.request.CommonRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainLayoutActivity extends AppCompatActivity {
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        findViewById(R.id.btn01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });







    }


    private void startConnect() {
        CommonOkHttpClient.sendRequest(CommonRequest.createGetRequest("http://www.imooc.com", null), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getMessage() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: "+response.body().string() );
            }
        });



    }






}
