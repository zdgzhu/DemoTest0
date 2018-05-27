package com.example.adutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.imoocsdk.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoaderUtil {

    /**
     * 默认的参数值
     */
    private static final int THREAD_COUNT = 2; // 标明我们的UIL最多可以有2条线程
    private static final int PRIORITY = 2;// 标明我们图片加载的优先级
    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;// 图片缓存的大小 2MB  1*1024= 1 KB
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;//硬盘缓存的大小
    private static final int CONNECTION_TIME_OUT = 5 * 1000;// 连接超时的时间
    private static final int READ_TIME_OUT = 30 * 1000;// 读取超时的时间

    private static ImageLoaderUtil mInstance = null;
    private static ImageLoader mLoder = null;

    //单例模式
    public static ImageLoaderUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 单例模式都是私有的构造方法
     * 私有构造方法完成初始化工作
     */

    private ImageLoaderUtil(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)//配置图片现在线程的最大数量
                //Thread.NORM_PRIORITY 获取系统正常的线程优先级， 每个系统有可能不一样 Thread.NORM_PRIORITY - PRIORITY 进行降级，没必要那么高
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
                .denyCacheImageMultipleSizesInMemory()// 防止缓存多套尺寸的图片到我得内存当中
                .memoryCache(new WeakMemoryCache())//使用弱引用 内存缓存
                .diskCacheSize(DISK_CACHE_SIZE) //硬盘缓存的大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的 URI 名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO) //图片下载的顺序
                .defaultDisplayImageOptions(getDefaultOptions()) //默认的图片加载 option
                //设置图片下载器，现在使用的默认的，也可以使用自定义的
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs() // 设置是否要写日志， debug 环境下输出日志
                .build();
        ImageLoader.getInstance().init(config);
        mLoder = ImageLoader.getInstance();
    }

    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener,
                             DisplayImageOptions options) {
        if (mLoder != null) {
            mLoder.displayImage(path, imageView, options, listener);
        }
    }


    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
        if (mLoder != null) {
            mLoder.displayImage(path, imageView, listener);
        }
    }


    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }



    /**
     * 默认的图片显示 Option ,可设置图片的缓存策略，解编码的方式等等，非常重要
     */
    public DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中，重要，否则图片不会缓存到内存中
                .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中，重要，否则图片不会缓存到硬盘中
                .considerExifParams(true) //是否考虑JPEG 图片的EXIF 参数 （旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码方式
                .decodingOptions(new BitmapFactory.Options()) //设置图片的解码配置
                .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
                .showImageForEmptyUri(R.drawable.xadsdk_img_error) //在我们图片地址为空的时候，显示这个图片
                .build();
        return options;
    }


    public DisplayImageOptions getOptionsWithNoCache() {

        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                //.cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(400))
                .build();
        return options;
    }


}
