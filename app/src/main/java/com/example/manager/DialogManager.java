package com.example.manager;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.demotest.R;

/**
 * Created by dell on 2018/6/11.
 */

public class DialogManager {

    private static DialogManager mInstance = null;
    private ProgressDialog mDialog;
    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    public void showProgressDialog(Context context) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage(context.getResources().getString(R.string.please_wait));
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();

    }

    public void dismissProgressDialog() {

        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = null;
    }





}
