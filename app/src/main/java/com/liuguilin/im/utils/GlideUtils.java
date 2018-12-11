package com.liuguilin.im.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.io.File;

/**
 * FileName: GlideUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 15:54
 * Email: lgl@szokl.com.cn
 * Profile: Glide
 */
public class GlideUtils {

    /**
     * 标准的图片加载函数
     *
     * @param mContext
     * @param imgUrl
     * @param mImageView
     */
    public static void loadImg(Context mContext, String imgUrl, ImageView mImageView) {
        if (Util.isOnMainThread()) {
            Glide.with(mContext).load(imgUrl).into(mImageView);
        }
    }

    /**
     * 加载Gif
     *
     * @param mContext
     * @param res
     * @param mImageView
     */
    public static void loadGif(Context mContext, int res, ImageView mImageView) {
        if (Util.isOnMainThread()) {
            Glide.with(mContext).load(res).asGif().into(mImageView);
        }
    }

    /**
     * 加载文件图片
     *
     * @param mContext
     * @param imgFile
     * @param mImageView
     */
    public static void loadFile(Context mContext, File imgFile, ImageView mImageView) {
        try {
            if (Util.isOnMainThread()) {
                Glide.with(mContext).load(imgFile).into(mImageView);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
