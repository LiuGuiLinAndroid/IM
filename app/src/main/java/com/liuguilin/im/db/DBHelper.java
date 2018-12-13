package com.liuguilin.im.db;

import com.liuguilin.im.im.NewFriend;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * FileName: DBHelper
 * Founder: LiuGuiLin
 * Create Date: 2018/12/13 14:43
 * Email: lgl@szokl.com.cn
 * Profile: 数据库操作
 */
public class DBHelper {

    /**
     * 增
     *
     * @param support
     */
    public static void save(LitePalSupport support) {
        support.save();
    }

    /**
     * 删
     * @param support 新朋友
     */
    public static void delete(NewFriend support) {
        LitePal.delete(NewFriend.class, support.getId());
    }

    /**
     * 查
     */
    public static List<? extends LitePalSupport> query(Class<?> cls) {
        return (List<? extends LitePalSupport>) LitePal.findAll(cls);
    }

    /**
     * 改
     *
     * @param support
     */
    public static void update(LitePalSupport support) {
        support.save();
    }


}
