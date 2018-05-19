package com.example.demotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "TAG";
    private Button btn01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        init();

        
    }

    private void init() {
        btn01 = findViewById(R.id.btn01);


    }


    @Override
    public void onClick(View v) {

    }

    
}
