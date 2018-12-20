package com.liuguilin.im.manager;

import android.content.Context;
import android.view.Gravity;

import com.liuguilin.im.R;
import com.liuguilin.im.view.DialogView;

/**
 * FileName: DialogManager
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 10:46
 * Email: lgl@szokl.com.cn
 * Profile: Dialog 管理
 */
public class DialogManager {

    private static DialogManager mInstance = null;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param mContext 上下文
     * @param layoutId 布局
     * @param gravity  位置
     * @return
     */
    public DialogView initView(Context mContext, int layoutId, int gravity) {
        return new DialogView(mContext, layoutId, R.style.Theme_dialog, gravity);
    }

    public DialogView initView(Context mContext, int layoutId) {
        return new DialogView(mContext, layoutId, R.style.Theme_dialog, Gravity.CENTER);
    }

    /**
     * 显示
     *
     * @param mDialog
     */
    public void show(DialogView mDialog) {
        if (mDialog != null) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

    /**
     * 隐藏
     *
     * @param mDialog
     */
    public void hide(DialogView mDialog) {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }
}
