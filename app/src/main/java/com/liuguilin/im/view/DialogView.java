package com.liuguilin.im.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.liuguilin.im.R;

/**
 * FileName: DialogView
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 10:40
 * Email: lgl@szokl.com.cn
 * Profile: Dialog
 */
public class DialogView extends Dialog {

    public DialogView(Context context, int layout, int style, int gravity) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = gravity;
        window.setAttributes(params);
    }
}
