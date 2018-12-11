package com.liuguilin.im.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.MainActivity;
import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.entity.Constants;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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

    private DialogView mLoginQuestionDialog;

    //Dialog
    private TextView tv_reg_user;
    private TextView tv_forget_pw;
    private TextView tv_dialog_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        initDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        et_account = (EditText) findViewById(R.id.et_account);
        et_pw = (EditText) findViewById(R.id.et_pw);
        tv_login_tips = (TextView) findViewById(R.id.tv_login_tips);
        btn_login = (Button) findViewById(R.id.btn_login);

        tv_login_tips.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_login_login));
        include_title_iv_back.setVisibility(View.GONE);
    }

    private void initDialog() {
        mLoginQuestionDialog = DialogManager.getInstance().initView(this, R.layout.dialog_login_question, Gravity.BOTTOM);

        tv_reg_user = (TextView) mLoginQuestionDialog.findViewById(R.id.tv_reg_user);
        tv_forget_pw = (TextView) mLoginQuestionDialog.findViewById(R.id.tv_forget_pw);
        tv_dialog_cancel = (TextView) mLoginQuestionDialog.findViewById(R.id.tv_dialog_cancel);

        tv_reg_user.setOnClickListener(this);
        tv_forget_pw.setOnClickListener(this);
        tv_dialog_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_tips:
                DialogManager.getInstance().show(mLoginQuestionDialog);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_reg_user:
                DialogManager.getInstance().hide(mLoginQuestionDialog);
                CommonUtils.startActivity(this, RegUserActivity.class, false);
                break;
            case R.id.tv_forget_pw:
                DialogManager.getInstance().hide(mLoginQuestionDialog);
                CommonUtils.startActivity(this, ForgetPwActivity.class, false);
                break;
            case R.id.tv_dialog_cancel:
                DialogManager.getInstance().hide(mLoginQuestionDialog);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String account = et_account.getText().toString().trim();
        String pw = et_pw.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_account_null));
            return;
        }
        if (TextUtils.isEmpty(pw)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_null));
            return;
        }
        IMSDK.login(account, pw, new SaveListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if (e == null) {
                    Constants.isLogin = true;
                    CommonUtils.Toast(LoginActivity.this, getString(R.string.str_login_success));
                    CommonUtils.startActivity(LoginActivity.this, MainActivity.class, true);
                } else {
                    CommonUtils.Toast(LoginActivity.this, getString(R.string.str_login_fail));
                    IMLog.e("Login Error:" + e.toString());
                }
            }
        });
    }
}
