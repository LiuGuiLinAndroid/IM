package com.liuguilin.im.list;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * FileName: TimeComparison
 * Founder: LiuGuiLin
 * Create Date: 2018/12/17 15:15
 * Email: lgl@szokl.com.cn
 * Profile: 按照时间排序
 */
public class TimeComparison implements Comparator<BmobIMMessage> {

    @Override
    public int compare(BmobIMMessage o1, BmobIMMessage o2) {
        if (o1.getCreateTime() > o2.getCreateTime()) {
            return 1;
        } else if (o1.getCreateTime() == o2.getCreateTime()) {
            return 0;
        }
        return -1;
    }
}
