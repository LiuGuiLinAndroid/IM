package com.liuguilin.im.entity;

import android.support.v4.app.Fragment;

/**
 * FileName: Constants
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 16:55
 * Email: lgl@szokl.com.cn
 * Profile:常量
 */
public class Constants {

    //Project TAG
    public static final String TAG = "IMLog";
    //Project Log Switch
    public static final boolean DEBUG = true;

    public static final String NEWS_KEY = "fe06d421e616b5deda176b5ef1e7f6df";

    //Current Fragment
    public static Fragment CURRENT_FRAGMENT = null;

    //Messgae Read Status
    public static final int MSG_UNREAD = 0;
    public static final int MSG_READ = 1;
    public static final int MSG_ADD = 2;

    //每五分钟插入
    public static long currentImTime = 0;

    //新闻次数限制
    public static boolean IS_NEWS = false;
}
