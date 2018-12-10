package com.liuguilin.im.utils;

import android.app.Activity;
import android.content.Intent;

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
     * @param mActivity
     * @param cls
     * @param isFinish 是否Finish
     */
    public static void startActivity(Activity mActivity, Class<?> cls, boolean isFinish) {
        mActivity.startActivity(new Intent(mActivity, cls));
        if (isFinish) {
            mActivity.finish();
        }
    }

}
