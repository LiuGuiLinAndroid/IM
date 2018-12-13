package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * FileName: AddFriendActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 16:54
 * Email: lgl@szokl.com.cn
 * Profile: 验证申请
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private EditText et_note;

    //用戶信息
    private BmobIMUserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        et_note = (EditText) findViewById(R.id.et_note);

        include_title_iv_back.setOnClickListener(this);
        title_right_text.setOnClickListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_add_friend_commit_title));
        title_right_text.setText(getString(R.string.str_common_commit));

        info = UserInfoActivity.info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.title_right_text:
                String note = et_note.getText().toString().trim();
                IMSDK.sendAddFriendMessage(info, note, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                        if (e == null) {
                            finish();
                            CommonUtils.Toast(AddFriendActivity.this, getString(R.string.str_add_friend_send_success));
                        } else {
                            IMLog.e(e.toString());
                            CommonUtils.Toast(AddFriendActivity.this, getString(R.string.str_add_friend_send_fail));
                        }
                    }
                });
                break;
        }
    }
}
