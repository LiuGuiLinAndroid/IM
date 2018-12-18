package com.liuguilin.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.ui.ChatActivity;
import com.liuguilin.im.view.DialogView;

import java.util.HashMap;

import cn.bmob.newim.bean.BmobIMConversation;

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
     *
     * @param mActivity
     * @param cls
     * @param isFinish  是否Finish
     */
    public static void startActivity(Activity mActivity, Class<?> cls, boolean isFinish) {
        mActivity.startActivity(new Intent(mActivity, cls));
        if (isFinish) {
            mActivity.finish();
        }
    }

    /**
     * 启动服务
     *
     * @param mContext
     * @param cls
     */
    public static void startService(Context mContext, Class<?> cls) {
        mContext.startService(new Intent(mContext, cls));
    }

    /**
     * Toast
     *
     * @param mContext
     * @param str
     */
    public static void Toast(Context mContext, String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 传递一个常态对话入口
     *
     * @param conversationEntrance
     */
    public static void startActivityForBundle(Context mContext, BmobIMConversation conversationEntrance) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", conversationEntrance);
        intent.putExtra("bundle", bundle);
        mContext.startActivity(intent);
    }

    /**
     * 获取视频第一帧
     *
     * @param path
     * @return
     */
    public static Bitmap createVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    /**
     * 获取网络视频第一帧
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createVideoThumb(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
