package com.example.demotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.activity.HomeActivity;

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
        btn01.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn01:
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;

                default:
                    break;

        }

    }

    
}
