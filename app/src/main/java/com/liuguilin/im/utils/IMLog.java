package com.liuguilin.im.utils;

import android.text.TextUtils;
import android.util.Log;

import com.liuguilin.im.entity.Constants;

/**
 * FileName: IMLog
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 16:54
 * Email: lgl@szokl.com.cn
 * Profile: Log
 */
public class IMLog {

    public static void i(String text) {
        if (Constants.DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.i(Constants.TAG, text);
            }
        }
    }

    public static void e(String text) {
        if (Constants.DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.e(Constants.TAG, text);
            }
        }
    }
}
