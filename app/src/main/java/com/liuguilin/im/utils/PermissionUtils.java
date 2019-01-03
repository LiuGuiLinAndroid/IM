package com.liuguilin.im.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * FileName: PermissionUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/18 18:04
 * Email: lgl@szokl.com.cn
 * Profile:权限
 */
public class PermissionUtils {

    /**
     * 检查某个权限
     *
     * @param mContext
     * @param permission
     * @return
     */
    public static boolean checkSelfPermission(Context mContext, String permission) {
        int hasPermission = ContextCompat.checkSelfPermission(mContext, permission);
        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限组
     *
     * @param mActivity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissions(Activity mActivity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
    }

    /**
     * 请求所有权限
     *
     * @param mActivity
     * @param requestCode
     */
    public static void requestAllPermissions(Activity mActivity, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    /**
     * 检查相机权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkCamera(Context mContext) {
        return checkSelfPermission(mContext, Manifest.permission.CAMERA);
    }

    /**
     * 请求相机权限
     *
     * @param mActivity
     * @param requestCode
     */
    public static void requestCamera(Activity mActivity, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, requestCode);
    }

    /**
     * 检查录音权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkAudio(Context mContext) {
        return checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 请求录音权限
     *
     * @param mActivity
     * @param requestCode
     */
    public static void requestAudio(Activity mActivity, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);
    }

    /**
     * 检查内存卡权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkRWSD(Context mContext) {
        boolean w = checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean r = checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        return w && r;
    }

    /**
     * 请求SD卡权限
     *
     * @param mActivity
     * @param requestCode
     */
    public static void requestRWSD(Activity mActivity, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }
}
