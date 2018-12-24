package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.utils.IMLog;

/**
 * FileName: TextBrowseActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/24 14:16
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class TextBrowseActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_browse);

        initView();
    }

    private void initView() {
        String text = getIntent().getStringExtra("text");

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        tv_content = (TextView) findViewById(R.id.tv_content);

        include_title_iv_back.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_brower_text));

        if (!TextUtils.isEmpty(text)) {
            tv_content.setText(text);
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

