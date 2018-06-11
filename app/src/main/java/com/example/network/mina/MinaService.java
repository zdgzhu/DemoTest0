package com.example.network.mina;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.network.http.HttpConstants;

/**
 * Created by dell on 2018/6/11.
 */

public class MinaService extends Service {

    private ConnectThread mConection;

    @Override
    public void onCreate() {
        super.onCreate();
        mConection = new ConnectThread("mina", getApplicationContext());
        mConection.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConection.disConnection();
        mConection = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private class ConnectThread extends HandlerThread {
        boolean isConnect;
        ConnectionManager connectionManager;

        public ConnectThread(String name, Context context) {
            super(name);
            ConnectionConfig config = new ConnectionConfig.Builder(context)
                    .setIP(HttpConstants.LOGIN_IP)
                    .setPort(HttpConstants.LOGIN_PORT)
                    .setReadBufferSize(HttpConstants.READBUFFER_SIZE)
                    .build();
            connectionManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            for (; ; ) {
                isConnect = connectionManager.connect();
                if (isConnect) {
                    break;
                }
                try {
                    //每隔三秒去连接一次直到成功
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void disConnection() {
            connectionManager.disConnection();
        }

    }



}
