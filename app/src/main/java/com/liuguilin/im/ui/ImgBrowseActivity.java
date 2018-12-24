package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.utils.GlideUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * FileName: ImgBrowseActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/24 14:12
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class ImgBrowseActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private PhotoView photo_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_browse);
        initView();
    }

    private void initView() {

        String url = getIntent().getStringExtra("text");

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        photo_view = (PhotoView) findViewById(R.id.photo_view);

        include_title_iv_back.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_brower_img));

        if (!TextUtils.isEmpty(url)) {
            setMsgImg(url);
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

    /**
     * 设置图片
     * @param msgImg
     */
    private void setMsgImg(String msgImg) {
        if (!TextUtils.isEmpty(msgImg)) {
            if (msgImg.contains("&")) {
                String[] list = msgImg.split("&");
                if (list != null) {
                    if (list.length > 0) {
                        if (!TextUtils.isEmpty(list[0])) {
                            GlideUtils.loadFile(this, new File(list[0]), R.drawable.img_load_img, photo_view);
                        } else {
                            if (list.length > 1) {
                                if (!TextUtils.isEmpty(list[1])) {
                                    GlideUtils.loadImg(this, list[1], R.drawable.img_load_img, photo_view);
                                }
                            }
                        }
                    }
                }
            } else {
                if (msgImg.startsWith("http:")) {
                    GlideUtils.loadImg(this, msgImg, R.drawable.img_load_img, photo_view);
                } else {
                    GlideUtils.loadFile(this, new File(msgImg), R.drawable.img_load_img, photo_view);
                }
            }
        }
    }
}
