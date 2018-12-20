package com.liuguilin.im.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;

import com.liuguilin.im.R;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.GlideUtils;

/**
 * FileName: LodingView
 * Founder: LiuGuiLin
 * Create Date: 2018/12/13 15:34
 * Email: lgl@szokl.com.cn
 * Profile: 加載
 */
public class LodingView {

    private DialogView mLodingView;

    private static LodingView mInstance = null;

    private LodingView() {
    }

    public static LodingView getInstance() {
        if (mInstance == null) {
            synchronized (LodingView.class) {
                if (mInstance == null) {
                    mInstance = new LodingView();
                }
            }
        }
        return mInstance;
    }

    public void initView(Context mContext) {
        mLodingView = DialogManager.getInstance().initView(mContext, R.layout.dialog_loding);
        mLodingView.setCancelable(false);
        ImageView ivLoding = mLodingView.findViewById(R.id.ivLoding);
        GlideUtils.loadGif(mContext, R.drawable.gif_login_loding, ivLoding);
    }

    public void show() {
        DialogManager.getInstance().show(mLodingView);
    }

    public void hide() {
        DialogManager.getInstance().hide(mLodingView);
    }

}