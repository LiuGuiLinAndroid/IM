package com.liuguilin.im.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.liuguilin.im.MainActivity;
import com.liuguilin.im.R;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.utils.CommonUtils;

/**
 * FileName: IndexActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:19
 * Email: lgl@szokl.com.cn
 * Profile: 初始
 */
public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IMUser imUser = IMSDK.getCurrentUser();
                if (imUser != null) {
                    CommonUtils.startActivity(IndexActivity.this, MainActivity.class, true);
                }else {
                    //判断是否第一次
                    CommonUtils.startActivity(IndexActivity.this, LoginActivity.class, true);
                }
            }
        }, 1500);
    }
}
