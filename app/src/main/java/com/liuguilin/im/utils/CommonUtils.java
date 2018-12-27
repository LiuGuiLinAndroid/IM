package com.liuguilin.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.ui.ChatActivity;
import com.liuguilin.im.view.DialogView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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

    /**
     * 获取Assets下的json
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 保存Bitmap到SD卡
     *
     * @param mContext
     * @param bitmap
     * @param name
     * @return
     */
    public static boolean saveBitmap(Context mContext, Bitmap bitmap, String name) {
        try {
            //获得sd卡路径
            String sdcardPath = System.getenv("EXTERNAL_STORAGE");
            //图片保存的文件夹名
            String dir = sdcardPath + "/IMApp/";
            //以File来构建
            File file = new File(dir);
            //如果不存在,创建此文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            IMLog.i("file uri==>" + dir);
            //将要保存的图片文件
            File mFile = new File(dir + name);
            if (mFile.exists()) {
                CommonUtils.Toast(mContext, mContext.getString(R.string.str_toest_photo_is_have));
                return false;
            }
            //构建输出流
            FileOutputStream outputStream = new FileOutputStream(mFile);
            //compress到输出outputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + dir + name)));
              return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析时间
     *
     * @param pattern
     * @param time
     * @return
     */
    public static String parsingTime(String pattern, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }

    public static String parsingTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(time);
    }
}
