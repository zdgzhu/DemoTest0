package com.example.view.associatemail;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by dell on 2018/6/11.
 * 一个继承自AutoCompleteTextView的可编辑的文本视图，能够对用户键入的文本进行有效地扩充提示，
 * 而不需要用户输入整个内容。（用户输入一部分内容，剩下的部分系统就会给予提示）。
 */

public class MailBoxAssociateView extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {


    public MailBoxAssociateView(Context context) {
        super(context);
    }

    public MailBoxAssociateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MailBoxAssociateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**是判断输入文字列长度是否满足现实候补列表的要求的方法。
     * 当文本长度超过阈值时过滤
     */
    @Override
    public boolean enoughToFilter() {
        Log.e("TAG", "enoughToFilter: " );
        return getText().toString().contains("@") && getText().toString().indexOf("@") > 0;
    }
}
