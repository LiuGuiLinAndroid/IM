package com.liuguilin.im.list;

import com.liuguilin.im.bean.ImChatBean;

import java.util.Comparator;

/**
 * FileName: TimeComparison
 * Founder: LiuGuiLin
 * Create Date: 2018/12/17 15:15
 * Email: lgl@szokl.com.cn
 * Profile: 按照时间排序
 */
public class TimeComparison implements Comparator<ImChatBean> {

    @Override
    public int compare(ImChatBean o1, ImChatBean o2) {
        if (o1.getUpdateTime() > o2.getUpdateTime()) {
            return 1;
        } else if (o1.getUpdateTime() == o2.getUpdateTime()) {
            return 0;
        }
        return -1;
    }
}
