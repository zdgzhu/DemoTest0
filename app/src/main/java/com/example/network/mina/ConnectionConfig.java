package com.example.network.mina;

import android.content.Context;
import android.os.Build;

import com.example.network.http.HttpConstants;

/**
 * Created by dell on 2018/6/11.
 * 与服务器连接的配置参数类
 */

public class ConnectionConfig {

    private Context context;
    private String ip;
    private int port;
    private int readBufferSize;
    private long connectionTimeout;

    private ConnectionConfig() {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }


    public static class Builder{
        private Context context;
        //192.168.2.10
        private String ip ="192.168.2.10";
        //9123
        private int port = 9123;
        private int readBufferSize = 10240;
        private long connectionTimeout = 10 * 1000;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setIP(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int size) {
            this.readBufferSize = size;
            return this;
        }

        public Builder setConnectionTimeout(long millions) {
            this.connectionTimeout = millions;
            return this;
        }

        private void applyConfig(ConnectionConfig config) {
            config.context = this.context;
            config.ip = this.ip;
            config.port = this.port;
            config.readBufferSize = this.readBufferSize;
            config.connectionTimeout = this.connectionTimeout;

        }

        public ConnectionConfig build() {
            ConnectionConfig config = new ConnectionConfig();
            applyConfig(config);
            return config;
        }


    }


}
