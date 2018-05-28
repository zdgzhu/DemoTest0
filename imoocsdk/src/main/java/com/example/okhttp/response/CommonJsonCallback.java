package com.example.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.example.adutil.GsonUtils;
import com.example.okhttp.exception.OkHttpException;
import com.example.okhttp.listener.DisposeDataHandle;
import com.example.okhttp.listener.DisposeDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonJsonCallback  implements Callback{

    /**
     * 与服务器返回的字段 一一对应
     */
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie";

    /**
     * the java layer exception, do not same to the logic error
     * 自定义我们常见的异常类型
     */
    protected final int NETWORK_ERROR = -1; // 网络异常the network relative error
    protected final int JSON_ERROR = -2; // json解析错误相关的异常 the JSON relative error
    protected final int OTHER_ERROR = -3; //其他的异常 the unknow error


    /**
     * 将其他线程的数据转发到 UI 线程
     */
    private Handler mDeliverHandler;//进行消息的转发
    private DisposeDataListener mListener;// 回调
    private Class<?> mClass; //字节码

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClazz;
        this.mDeliverHandler = new Handler(Looper.getMainLooper());
    }


    /**
     * 请求失败 处理 ，如果请求失败，那么就将这个异常抛到应用层，因为应用层可能会根据不同的异常
     * 去做不同的处理
     */
    @Override
    public void onFailure(Call call, final IOException e) {
        /**
         * 此时还在非 UI 线程，因此需要转发
         */
        mDeliverHandler.post(new Runnable() {
            @Override
            public void run() {
           mListener.onFailure(new OkHttpException(NETWORK_ERROR,e.getMessage()));
            }
        });

    }

    /**
     * 真正的响应处理函数
     */
    @Override
    public void onResponse(Call call, final Response response) throws IOException {
       //获取返回的字符串
        final String result = response.body().string();
//        final ArrayList<String>
        mDeliverHandler.post(new Runnable() {
            @Override
            public void run() {
                //首相是拿到响应的信息result ，然后转给handleResponse
                handleResponse(result);


            }
        });


    }


    //处理服务器返回的响应数据
    private void handleResponse(Object responseObj) {
        //为了保证代码的健壮性
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            //因为服务器还没有返回真正的数据，所以是  NETWORK_ERROR
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            //开始真正的解析数据
//            JSONObject result = new JSONObject(responseObj.toString());
          //消息是有的，常识解析
//            if (result.has(RESULT_CODE)) {
                //从json 对象中取出我们的响应码。如果是0 ，则是正确的响应，这个0 是跟服务器约定的
//                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    //表明不需要解析，直接返回数据到应用层
                    if (mClass == null) {
                        mListener.onSuccess(responseObj.toString());

                    } else {
                        //即，应用层需要我们将json，对象转化为了实体对象，这个是自己写的类的进行的转化，
                        //也可以用第三方库区进行转化

//                        Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                        Object obj = GsonUtils.fromJson(responseObj.toString(), mClass);
                        //标明正确的转为了实体对象
                        if (obj != null) {
                            mListener.onSuccess(obj);
                        } else {
                            //返回的不是合法的json
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }

                    }


//                }

//            } else {
//                //将服务器返回给我们的异常回调到应用层去处理
//                mListener.onFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
//            }



        } catch (Exception e) {
            //将服务器返回给我们的异常回调到应用层去处理
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }


    }



}
