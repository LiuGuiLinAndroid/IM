package com.liuguilin.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.view.DialogView;

/**
 * FileName: CommonUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:47
 * Email: lgl@szokl.com.cn
 * Profile: 通用
 */
public class CommonUtils {

    /**
     * 跳转Activity
     *
     * @param mActivity
     * @param cls
     * @param isFinish  是否Finish
     */
    public static void startActivity(Activity mActivity, Class<?> cls, boolean isFinish) {
        mActivity.startActivity(new Intent(mActivity, cls));
        if (isFinish) {
            mActivity.finish();
        }
    }

    /**
     * 启动服务
     *
     * @param mContext
     * @param cls
     */
    public static void startService(Context mContext, Class<?> cls) {
        mContext.startService(new Intent(mContext, cls));
    }

    /**
     * Toast
     *
     * @param mContext
     * @param str
     */
    public static void Toast(Context mContext, String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
