package com.example.okhttp.exception;

public class OkHttpException extends Exception {

    /**
     * 异常码，这个比较重要，应用层或许需要这个异常码来做不同的处理
     */
    private int ecode;

    /**
     * 其他的错误信息
     */
    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
        return;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }


}
