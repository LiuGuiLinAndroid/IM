package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;

/**
 * FileName: LoginActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:52
 * Email: lgl@szokl.com.cn
 * Profile: Login
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private EditText et_account;
    private EditText et_pw;
    private TextView tv_login_tips;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        et_account = (EditText) findViewById(R.id.et_account);
        et_pw = (EditText) findViewById(R.id.et_pw);
        tv_login_tips = (TextView) findViewById(R.id.tv_login_tips);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_login_login));
        include_title_iv_back.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_tips:

                break;
            case R.id.btn_login:

                break;
        }
    }

    private void submit() {
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "account不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String pw = et_pw.getText().toString().trim();
        if (TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "pw不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
