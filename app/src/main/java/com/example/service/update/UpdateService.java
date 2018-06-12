package com.example.service.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.demotest.R;

import java.io.File;

/**
 * Created by dell on 2018/6/12.
 */

public class UpdateService extends Service {
    /**
     * 服务器固定地址
     */
    private static final String APK_URL_TITLE = "http://www.imooc.com/mobile/mukewang.apk";
    /**
     * 文件存放路径
     */
    private String filePath;
    /**
     * 文件下载路径
     */
    private String apkUrl;

    private NotificationManager notificationManager;
    private Notification mNotification;

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory() + "/imooc/imooc.apk";

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        apkUrl = APK_URL_TITLE;
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {




    }

    public void notifyUser(String tickerMsg, String message, int progress) {
        notifyThatExceedLv21(tickerMsg,message,progress);

    }

    private void notifyThatExceedLv21(String tickerMsg, String message, int progress) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "default");
        // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，
        // 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
        notification.setSmallIcon(R.drawable.bg_message_imooc);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bg_message_imooc));
        notification.setContentTitle(getString(R.string.app_name));
        if (progress > 0 && progress < 100) {
            notification.setProgress(100, progress, false);
        } else {
            /**
             * 0,0,false,可以将进度条影藏
             */
            notification.setProgress(0, 0, false);
            notification.setContentText(message);
        }
        //可以点击通知栏的删除按钮删除
        notification.setAutoCancel(true);
        notification.setWhen(System.currentTimeMillis());
        //Ticker是状态栏显示的提示
        notification.setTicker(tickerMsg);
        //点击跳转的intent
        notification.setContentIntent(progress > 100 ? getContentIntent() : PendingIntent.getActivity(this, 0,
                new Intent(), PendingIntent.FLAG_CANCEL_CURRENT));
        mNotification = notification.build();
        notificationManager.notify(0, mNotification);

    }

    private PendingIntent getContentIntent() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, getInstallApkIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        return contentIntent;
    }

    /**
     *  下载完成， 安装
     */
    private Intent getInstallApkIntent() {
        File apkfile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 删除无用文件
     */
    private boolean deleteApkFile() {
        File apkFile = new File(filePath);
        if (apkFile.exists() && apkFile.isFile()) {
            return apkFile.delete();
        }
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


















}
