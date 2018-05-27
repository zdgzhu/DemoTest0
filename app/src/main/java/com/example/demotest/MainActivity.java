package com.example.demotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.activity.HomeActivity;
import com.example.okhttp.CommonOkHttpClient;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.okhttp.request.CommonRequest;
import com.example.okhttp.response.CommonJsonCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    http://192.168.128.2:8080/ServletDemo/DemoServlet

    private String TAG = "TAG";
    private Button btn01;
    private String url = "http://www.imooc.com";
    private String url1 = "http://localhost:8080/ServletDemo/DemoServlet";
    private String url2 = "http://10.0.2.2:8080/ServletDemo/DemoServlet";
    private String url3 = " http://192.168.128.2:8080/ServletDemo/aa";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        init();

        
    }

    private void init() {
     findViewById(R.id.btn01).setOnClickListener(this);
     findViewById(R.id.btn02).setOnClickListener(this);
     findViewById(R.id.btn03).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn01:
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;

            case R.id.btn02:
                CommonOkHttpClient.sendRequest(CommonRequest.createGetRequest(url2, null), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: "+response.body().string());
                    }
                });
                break;

            case R.id.btn03:
              CommonOkHttpClient.sendRequest(CommonRequest.createGetRequest(url3,null),
                      new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener() {
                          @Override
                          public void onSuccess(Object responseObj) {
                              Toast.makeText(MainActivity.this, "成功 ："+responseObj.toString(), Toast.LENGTH_SHORT).show();
                              Log.d(TAG, "onSuccess: "+responseObj.toString());
                          }

                          @Override
                          public void onFailure(Object reasonObj) {
                              Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                              Log.d(TAG, "onFailure: "+reasonObj.toString());

                          }
                      })));
                break;








                default:
                    break;

        }

    }

    
}
