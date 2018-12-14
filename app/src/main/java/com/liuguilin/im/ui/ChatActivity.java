package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;

/**
 * FileName: ChatActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 17:23
 * Email: lgl@szokl.com.cn
 * Profile: 聊天
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private ImageView iv_voice;
    private EditText et_text;
    private TextView tv_send_voice;
    private ImageView iv_emoji;
    private ImageView iv_more;
    private LinearLayout ll_camera;
    private LinearLayout ll_album;
    private LinearLayout ll_location;
    private LinearLayout ll_file;
    private LinearLayout ll_more;
    private LinearLayout ll_emoji;
    private Button btn_send;

    private DialogView mVoiceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
    }

    private void initView() {

        initVoiceDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);

        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_send_voice = (TextView) findViewById(R.id.tv_send_voice);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_file = (LinearLayout) findViewById(R.id.ll_file);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        ll_emoji = (LinearLayout) findViewById(R.id.ll_emoji);

        include_title_iv_back.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        tv_send_voice.setOnClickListener(this);
        tv_send_voice.setOnTouchListener(this);

        title_right_text.setText(getString(R.string.str_chat_more_right_text));

        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                    iv_more.setVisibility(View.GONE);
                } else {
                    btn_send.setVisibility(View.GONE);
                    iv_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initVoiceDialog() {
        mVoiceDialog = DialogManager.getInstance().initView(this, R.layout.dialog_voice, Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.iv_voice:
                if (et_text.getVisibility() == View.VISIBLE) {
                    iv_voice.setImageResource(R.drawable.img_key);
                    tv_send_voice.setVisibility(View.VISIBLE);
                    et_text.setVisibility(View.GONE);

                    iv_emoji.setImageResource(R.drawable.img_emoji);
                    if (ll_emoji.getVisibility() == View.VISIBLE) {
                        ll_emoji.setVisibility(View.GONE);
                    }
                    if (ll_more.getVisibility() == View.VISIBLE) {
                        ll_more.setVisibility(View.GONE);
                    }
                    if (btn_send.getVisibility() == View.VISIBLE) {
                        btn_send.setVisibility(View.GONE);
                    }

                } else {
                    iv_voice.setImageResource(R.drawable.img_voice);
                    tv_send_voice.setVisibility(View.GONE);
                    et_text.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_emoji:
                iv_voice.setImageResource(R.drawable.img_voice);
                tv_send_voice.setVisibility(View.GONE);
                et_text.setVisibility(View.VISIBLE);

                if (ll_emoji.getVisibility() == View.VISIBLE) {
                    iv_emoji.setImageResource(R.drawable.img_emoji);
                    ll_emoji.setVisibility(View.GONE);
                } else {
                    iv_emoji.setImageResource(R.drawable.img_key);
                    ll_emoji.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_more:
                iv_emoji.setImageResource(R.drawable.img_emoji);
                if (ll_emoji.getVisibility() == View.VISIBLE) {
                    ll_emoji.setVisibility(View.GONE);
                }
                iv_voice.setImageResource(R.drawable.img_voice);
                tv_send_voice.setVisibility(View.GONE);
                et_text.setVisibility(View.VISIBLE);

                if (ll_more.getVisibility() == View.VISIBLE) {
                    ll_more.setVisibility(View.GONE);
                } else {
                    ll_more.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                IMLog.i("ACTION_DOWN");
                DialogManager.getInstance().show(mVoiceDialog);
                break;
            case MotionEvent.ACTION_MOVE:
                IMLog.i("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                IMLog.i("ACTION_UP");
                DialogManager.getInstance().hide(mVoiceDialog);
                break;
        }
        return false;
    }
}
