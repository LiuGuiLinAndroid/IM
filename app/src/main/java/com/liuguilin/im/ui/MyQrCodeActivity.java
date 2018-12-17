package com.liuguilin.im.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.utils.GlideUtils;
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
public class MyQrCodeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private CircleImageView iv_user;
    private TextView tv_niname;
    private ImageView iv_sex;
    private TextView tv_city;
    private ImageView iv_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();

    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        iv_user = (CircleImageView) findViewById(R.id.iv_user);
        tv_niname = (TextView) findViewById(R.id.tv_niname);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        tv_city = (TextView) findViewById(R.id.tv_city);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);

        include_title_iv_back.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_qrcode_title_text));

        updateUser();
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
                    GlideUtils.loadImg(this, url,R.drawable.img_def_photo, iv_user);
                }
            }

            iv_sex.setImageResource(imUser.isSex() ? R.drawable.img_boy : R.drawable.img_girl);

            int width = getWindowManager().getDefaultDisplay().getWidth();
            int height = getWindowManager().getDefaultDisplay().getHeight();

            Bitmap mBitmap = CodeUtils.createImage(imUser.getUsername(), width * 2 / 3, height /3, null);
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
        }
    }
}
