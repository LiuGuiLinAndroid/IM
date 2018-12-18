package com.liuguilin.im.utils;

import android.content.Context;

import com.liuguilin.im.R;

/**
 * FileName: FormatCurrentUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/18 13:57
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class FormatCurrentUtils {

    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getTimeRange(Context mContext, long mTime, long nowTime) {
        /**除以1000是为了转换成秒*/
        long between = (nowTime - mTime);
        int elapsedTime = (int) (between);
        if (mContext != null) {
            if (elapsedTime < seconds_of_1minute) {
                return mContext.getString(R.string.str_time_format_1);
            }
            if (elapsedTime < seconds_of_30minutes) {
                return elapsedTime / seconds_of_1minute + mContext.getString(R.string.str_time_format_2);
            }
            if (elapsedTime < seconds_of_1hour) {
                return mContext.getString(R.string.str_time_format_3);
            }
            if (elapsedTime < seconds_of_1day) {
                return elapsedTime / seconds_of_1hour + mContext.getString(R.string.str_time_format_4);
            }
            if (elapsedTime < seconds_of_15days) {
                return elapsedTime / seconds_of_1day + mContext.getString(R.string.str_time_format_5);
            }
            if (elapsedTime < seconds_of_30days) {
                return mContext.getString(R.string.str_time_format_6);
            }
            if (elapsedTime < seconds_of_6months) {
                return elapsedTime / seconds_of_30days + mContext.getString(R.string.str_time_format_7);
            }
            if (elapsedTime < seconds_of_1year) {
                return mContext.getString(R.string.str_time_format_8);
            }
            if (elapsedTime >= seconds_of_1year) {
                return elapsedTime / seconds_of_1year + mContext.getString(R.string.str_time_format_9);
            }
        }
        return "";
    }
}
