package com.example.view.associatemail;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by dell on 2018/6/11.
 * 用户必须提供一个MultiAutoCompleteTextView.Tokenizer用来区分不同的子串。
 * Tokenizer，可以支持连续提示。即第一次点击提示信息后，会自动在后面添加分隔符(默认为逗号，并加上空格)，然后又可以继续显示提示信息。
 */

public class MailBoxAssociateTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    private static final String TAG = "TAG_Tokenizer";
    /**
     *  用于查找当前光标位置之前的分隔符的位置并返回，向前查询
     *  text - 用户已经输入的文本内容
     * cursor - 当前光标的位置，在文本内容后面
     */
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        Log.e(TAG, "findTokenStart: " );
        // indexOf 返回第一次出现的指定子字符串在此字符串中的索引
        int index = text.toString().indexOf("@");
        if (index < 0) {
            index = text.length();
        }
        if (index >= findTokenEnd(text, cursor)) {
            index = 0;
        }

// 返回一个要加分隔符的字符串的开始位置
        return index;
    }


    /**
     * 用于查找当前光标位置之后的分隔符的位置并返回，向后查询
     * text - 用户已经输入的文本内容
     * cursor - 当前光标的位置，在文本内容之间
     */
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        Log.e(TAG, "findTokenEnd: " );
        int i = cursor;
        int len = text.length();
        while (i < len) {
            //向后查询"@"字符，若找到则直接返回去所在位置
            if (text.charAt(i) == '@') {
                return i;
            } else {
                i++;
            }

        }

        return len;
    }


    /**
     * 用于返回提示信息加上分隔符后的文本内容
     * 用于返回提示信息加上分隔符后的文本内容
     * text - 提示信息中的文本内容
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        Log.e(TAG, "terminateToken: " );
        int i = text.length();
         //去掉原始匹配的数据的末尾空格
        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }
        //判断原始匹配的数据去掉末尾空格后是否有"@" 有则立即返回
        if (i > 0 && text.charAt(i - 1) == '@') {
            return text;
        } else {
            /**
             * CharSequence类型的数据有可能是富文本SpannableString类型
             * 故需要进行判断
             */
            if (text instanceof Spanned) {
                /**
                 *  创建一个新的SpannableString，传进来的text会被退化成String，
                 *  导致sp中丢失掉了text中的样式配置
                 */
                SpannableString sp = new SpannableString(text);
                /**
                 * 故需要借助TextUtils.copySpansFrom从text中复制原来的样式到新的sp中，
                 *  以保持原先样式不变情况下添加一个逗号和空格
                 */
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                // text为纯文本，直接加上逗号和空格 return text + ", "
                return text;
            }

        }

    }



}
