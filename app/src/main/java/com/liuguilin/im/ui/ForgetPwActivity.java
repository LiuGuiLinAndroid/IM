package com.liuguilin.im.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * FileName: ForgetPwActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 11:36
 * Email: lgl@szokl.com.cn
 * Profile: 忘记密码
 */
public class ForgetPwActivity extends BaseActivity implements View.OnClickListener {

    private static final int H_TIMER = 0x12;

    private static int TIME = 60;

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private EditText et_reg_phone;
    private EditText et_reg_pw;
    private EditText et_reg_code;
    private TextView tv_get_code;
    private Button btn_reset;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case H_TIMER:
                    tv_get_code.setText(TIME + "s");
                    if (TIME > 0) {
                        TIME--;
                        mHandler.sendEmptyMessageDelayed(H_TIMER, 1000);
                    } else {
                        TIME = 60;
                        tv_get_code.setEnabled(true);
                        tv_get_code.setText(getString(R.string.str_reg_get_code));
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);
        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        et_reg_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_reg_pw = (EditText) findViewById(R.id.et_reg_pw);
        et_reg_code = (EditText) findViewById(R.id.et_reg_code);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        include_title_iv_back.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_forget_pw));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.tv_get_code:
                requestSMSCode();
                break;
            case R.id.btn_reset:
                resetPasswordBySMSCode();
                break;
        }
    }

    /**
     * 短信验证码重置密码
     */
    private void resetPasswordBySMSCode() {
        String code = et_reg_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_code_null));
            return;
        }
        String pw = et_reg_pw.getText().toString().trim();
        if (TextUtils.isEmpty(pw)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_null));
            return;
        }

        //重置密码
        IMSDK.resetPasswordBySMSCode(code, pw, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    CommonUtils.Toast(ForgetPwActivity.this, getString(R.string.str_toast_reset_success));
                    finish();
                } else {
                    CommonUtils.Toast(ForgetPwActivity.this, getString(R.string.str_toast_reset_fail));
                    IMLog.e(e.toString());
                }
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void requestSMSCode() {
        String phone = et_reg_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_phone_null));
            return;
        }
        IMSDK.requestSMSCode(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    CommonUtils.Toast(ForgetPwActivity.this, getString(R.string.str_toast_get_code_success));
                    //开启倒计时
                    mHandler.sendEmptyMessage(H_TIMER);
                    tv_get_code.setEnabled(false);
                } else {
                    CommonUtils.Toast(ForgetPwActivity.this, getString(R.string.str_toast_get_code_fail));
                    IMLog.e(e.toString());
                }
            }
        });
    }
}
