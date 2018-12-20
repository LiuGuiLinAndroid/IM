package com.liuguilin.im.ui;

import android.content.Intent;
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
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.service.IMService;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * FileName: ChangePwActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 10:17
 * Email: lgl@szokl.com.cn
 * Profile: 修改密码
 */
public class ChangePwActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private EditText et_old;
    private EditText et_new;
    private EditText et_new_to;
    private Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        et_old = (EditText) findViewById(R.id.et_old);
        et_new = (EditText) findViewById(R.id.et_new);
        et_new_to = (EditText) findViewById(R.id.et_new_to);
        btn_change = (Button) findViewById(R.id.btn_change);

        btn_change.setOnClickListener(this);

        include_title_iv_back.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_change_pw_title_text));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.btn_change:
                submit();
                break;
        }
    }

    private void submit() {
        String old = et_old.getText().toString().trim();
        if (TextUtils.isEmpty(old)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_null));
            return;
        }

        String newPw = et_new.getText().toString().trim();
        if (TextUtils.isEmpty(newPw)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_null));
            return;
        }

        String to = et_new_to.getText().toString().trim();
        if (TextUtils.isEmpty(to)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_null));
            return;
        }

        if (!newPw.equals(to)) {
            CommonUtils.Toast(this, getString(R.string.str_toast_pw_to_error));
            return;
        }

        IMSDK.changePw(old, to, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    CommonUtils.Toast(ChangePwActivity.this,getString(R.string.str_toast_reset_success));
                    stopService(new Intent(ChangePwActivity.this, IMService.class));
                    IMSDK.exitUser();
                    EventManager.post(EventManager.EVENT_TYPE_SETTING_EXIT);
                    EventManager.post(EventManager.EVENT_TYPE_MAIN_EXIT);
                    CommonUtils.startActivity(ChangePwActivity.this, LoginActivity.class, true);
                } else {
                    CommonUtils.Toast(ChangePwActivity.this,getString(R.string.str_toast_reset_fail));
                    IMLog.e(e.toString());
                }
            }
        });
    }
}
