package com.liuguilin.im.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * FileName: LanguaueUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 13:59
 * Email: lgl@szokl.com.cn
 * Profile:语言工具类
 */
public class LanguaueUtils {

    /**
     * 切換語言
     * @param mContext
     */
    public static void updateLanguaue(Context mContext) {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        //读取配置
        int languaue = SharedPreUtils.getInstance().getInt("languaue", 0);
        IMLog.e("languaue:" + languaue);
        if (languaue == 0) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (languaue == 1) {
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else if (languaue == 2) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }
}
