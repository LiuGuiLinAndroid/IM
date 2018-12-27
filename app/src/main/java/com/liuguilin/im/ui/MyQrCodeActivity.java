package com.liuguilin.im.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.view.DialogView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * FileName: MyQrCodeActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/14 15:59
 * Email: lgl@szokl.com.cn
 * Profile: 二维码
 */
public class MyQrCodeActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private CircleImageView iv_user;
    private TextView tv_niname;
    private ImageView iv_sex;
    private TextView tv_city;
    private ImageView iv_qrcode;

    private Bitmap mBitmap;

    private DialogView mSaveDialog;
    private TextView tv_save_qr;
    private TextView tv_dialog_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
    }

    private void initView() {

        initDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        iv_user = (CircleImageView) findViewById(R.id.iv_user);
        tv_niname = (TextView) findViewById(R.id.tv_niname);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        tv_city = (TextView) findViewById(R.id.tv_city);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);

        include_title_iv_back.setOnClickListener(this);
        iv_qrcode.setOnLongClickListener(this);

        include_title_text.setText(getString(R.string.str_qrcode_title_text));

        updateUser();
    }

    private void initDialog() {
        mSaveDialog = DialogManager.getInstance().initView(this, R.layout.dialog_save_qrcode, Gravity.BOTTOM);
        tv_save_qr = (TextView) mSaveDialog.findViewById(R.id.tv_save_qr);
        tv_save_qr.setOnClickListener(this);
        tv_dialog_cancel = (TextView) mSaveDialog.findViewById(R.id.tv_dialog_cancel);
        tv_dialog_cancel.setOnClickListener(this);
    }

    private void updateUser() {
        IMUser imUser = IMSDK.getCurrentUser();
        if (imUser != null) {
            String nickName = imUser.getNickname();
            if (!TextUtils.isEmpty(nickName)) {
                tv_niname.setText(nickName);
            } else {
                tv_niname.setText(imUser.getUsername());
            }

            String city = imUser.getCity();
            if (!TextUtils.isEmpty(city)) {
                tv_city.setText(city);
            } else {
                tv_city.setVisibility(View.GONE);
            }

            BmobFile userFile = imUser.getAvatar();
            if (userFile != null) {
                String url = userFile.getFileUrl();
                if (!TextUtils.isEmpty(url)) {
                    GlideUtils.loadImg(this, url, R.drawable.img_def_photo, iv_user);
                }
            }

            iv_sex.setImageResource(imUser.isSex() ? R.drawable.img_boy : R.drawable.img_girl);

            int width = getWindowManager().getDefaultDisplay().getWidth();
            int height = getWindowManager().getDefaultDisplay().getHeight();

            mBitmap = CodeUtils.createImage(imUser.getUsername(), width * 2 / 3, height / 3, null);
            if (mBitmap != null) {
                iv_qrcode.setImageBitmap(mBitmap);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.tv_dialog_cancel:
                DialogManager.getInstance().hide(mSaveDialog);
                break;
            case R.id.tv_save_qr:
                CommonUtils.saveBitmap(this, mBitmap, System.currentTimeMillis() + ".png");
                CommonUtils.Toast(this, getString(R.string.str_toast_save_success));
                DialogManager.getInstance().hide(mSaveDialog);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qrcode:
                DialogManager.getInstance().show(mSaveDialog);
                break;
        }
        return false;
    }
}
