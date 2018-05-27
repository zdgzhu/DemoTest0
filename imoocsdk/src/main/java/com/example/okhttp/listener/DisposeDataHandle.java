package com.example.okhttp.listener;

/**
 * 在处理json回调的时候，我们要完成 json 对象向实体对象的转化，所以我们需要 转化对象的字节码对象
 *
 * 将我们的响应回调和字节码进行封装
 *
 */
public class DisposeDataHandle {

    //响应的回调
    public DisposeDataListener mListener = null;
    //字节码
    public Class<?> mClazz = null;

    public String mSource = null;

    //这个构造方法：只传入了listener 就是将我们服务器回调返回的数据，原封不动的扔到(返回)到我们的应用层
    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }


    /**这个构造方法：不仅传入了listener 和字节码clazz,
     * 如果返回了这个class，那么我们就需要在我们回调中，去处理这个字节码，转化成我们对应的实体类对象
     * 这个算是 CommonJsonCallback的预处理
     */
    public DisposeDataHandle(DisposeDataListener listener,Class<?>clazz) {
        this.mListener = listener;
        this.mClazz = clazz;
    }


    public DisposeDataHandle(DisposeDataListener listener,String source) {
        this.mListener = listener;
        this.mSource = source;

    }




}
