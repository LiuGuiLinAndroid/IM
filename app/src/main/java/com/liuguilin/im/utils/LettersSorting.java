package com.liuguilin.im.utils;

import com.liuguilin.im.im.Friend;

import java.util.Comparator;

/**
 * FileName: LettersSorting
 * Founder: LiuGuiLin
 * Create Date: 2018/12/19 14:32
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class LettersSorting implements Comparator<Friend> {
    @Override
    public int compare(Friend o1, Friend o2) {
        return o1.getLetter().compareTo(o2.getLetter());
    }
}
