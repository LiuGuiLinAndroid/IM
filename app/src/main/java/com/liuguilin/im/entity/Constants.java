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

    //Is Login
    public static boolean isLogin = false;

    //Current Fragment
    public static Fragment CURRENT_FRAGMENT = null;

    //Messgae Read Status
    public static final int MSG_UNREAD = 0;
    public static final int MSG_READ = 1;
    public static final int MSG_ADD = 2;
}
