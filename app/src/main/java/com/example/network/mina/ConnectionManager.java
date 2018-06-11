package com.example.network.mina;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.serialization.ObjectSerializationEncoder;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * Created by dell on 2018/6/11.
 * 负责与远程和服器的连接建立，消息接收
 */

public class ConnectionManager {

    public static String BROADCAST_ACTION = "com.imooc.mina.broadcast";
    public static String MESSAGE = "message";

    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    //NioSocketConnector负责客户端向服务端发起连接请求，会创建一个NioSocketSession来负责读写操作
    private NioSocketConnector mConnector;
    private IoSession mSession;
    private InetSocketAddress mAddress;

    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<Context>(config.getContext());
        init();

    }

    /**
     * 完成Connection初始化
     */
    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnector = new NioSocketConnector();
        //设置读取缓存区的大小
        mConnector.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //编码过滤 默认的
        mConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        //绑定逻辑处理器
        mConnector.setHandler(new DefaultHandle(mContext.get()));
        // 读写通道10秒内无操作进入空闲状态
//        mConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    /**
     * 取得连接对象
     */
    public boolean connect() {
        try {
            ConnectFuture future = mConnector.connect(mAddress);
            //等待完成连接
            future.awaitUninterruptibly();
            mSession = future.getSession();
        } catch (Exception e) {
            return false;
        }
        return mSession == null ? false : true;
    }

    public void disConnection() {
        mConnector.dispose();
        mConnector = null;
        mSession = null;
        mAddress = null;
        mContext = null;
    }





    private static class DefaultHandle extends IoHandlerAdapter{
        private Context mContext;

        public DefaultHandle(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            if (session != null) {
                SessionManager.getInstance().setSession(session);
            }
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (mContext != null) {
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(MESSAGE, message.toString());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            SessionManager.getInstance().removeSession();
        }
    }



}
