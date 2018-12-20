package com.liuguilin.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.service.IMService;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.SharedPreUtils;
import com.liuguilin.im.view.DialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * FileName: SettingActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 9:55
 * Email: lgl@szokl.com.cn
 * Profile: 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private LinearLayout ll_change_pw;
    private LinearLayout ll_switch_languaue;
    private ImageView iv_new_msg;
    private Button btn_exit_app;

    private boolean isNewMsg;

    private DialogView mLanguaueDialog;
    private ImageView iv_zh;
    private LinearLayout ll_zh;
    private ImageView iv_tw;
    private LinearLayout ll_tw;
    private ImageView iv_en;
    private LinearLayout ll_en;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        initLanguaueDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        ll_change_pw = (LinearLayout) findViewById(R.id.ll_change_pw);
        ll_switch_languaue = (LinearLayout) findViewById(R.id.ll_switch_languaue);
        iv_new_msg = (ImageView) findViewById(R.id.iv_new_msg);
        btn_exit_app = (Button) findViewById(R.id.btn_exit_app);

        btn_exit_app.setOnClickListener(this);
        include_title_iv_back.setOnClickListener(this);
        ll_change_pw.setOnClickListener(this);
        ll_switch_languaue.setOnClickListener(this);
        iv_new_msg.setOnClickListener(this);

        include_title_text.setText(getText(R.string.str_setting_title_text));

        isNewMsg = SharedPreUtils.getInstance().getBool("newMsg", true);
        iv_new_msg.setImageResource(isNewMsg ? R.drawable.img_switch_on : R.drawable.img_switch_off);
    }

    private void initLanguaueDialog() {

        mLanguaueDialog = DialogManager.getInstance().initView(this, R.layout.dialog_languaue);
        iv_zh = (ImageView) mLanguaueDialog.findViewById(R.id.iv_zh);
        ll_zh = (LinearLayout) mLanguaueDialog.findViewById(R.id.ll_zh);

        iv_tw = (ImageView) mLanguaueDialog.findViewById(R.id.iv_tw);
        ll_tw = (LinearLayout) mLanguaueDialog.findViewById(R.id.ll_tw);

        iv_en = (ImageView) mLanguaueDialog.findViewById(R.id.iv_en);
        ll_en = (LinearLayout) mLanguaueDialog.findViewById(R.id.ll_en);

        ll_zh.setOnClickListener(this);
        ll_tw.setOnClickListener(this);
        ll_en.setOnClickListener(this);

        int languaue = SharedPreUtils.getInstance().getInt("languaue", 0);
        updateDialogIcon(languaue);
    }

    /**
     * 更新图标
     * @param languaue
     */
    private void updateDialogIcon(int languaue) {
        switch (languaue) {
            case 0:
                iv_zh.setVisibility(View.VISIBLE);
                iv_tw.setVisibility(View.INVISIBLE);
                iv_en.setVisibility(View.INVISIBLE);
                break;
            case 1:
                iv_zh.setVisibility(View.INVISIBLE);
                iv_tw.setVisibility(View.VISIBLE);
                iv_en.setVisibility(View.INVISIBLE);
                break;
            case 2:
                iv_zh.setVisibility(View.INVISIBLE);
                iv_tw.setVisibility(View.INVISIBLE);
                iv_en.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.btn_exit_app:
                stopService(new Intent(this, IMService.class));
                IMSDK.exitUser();
                //退出主页
                EventManager.post(EventManager.EVENT_TYPE_MAIN_EXIT);
                CommonUtils.startActivity(this, LoginActivity.class, true);
                break;
            case R.id.ll_change_pw:
                CommonUtils.startActivity(this, ChangePwActivity.class, false);
                break;
            case R.id.ll_switch_languaue:
                DialogManager.getInstance().show(mLanguaueDialog);
                break;
            case R.id.iv_new_msg:
                if (isNewMsg) {
                    iv_new_msg.setImageResource(R.drawable.img_switch_off);
                    SharedPreUtils.getInstance().setBool("newMsg", false);
                } else {
                    iv_new_msg.setImageResource(R.drawable.img_switch_on);
                    SharedPreUtils.getInstance().setBool("newMsg", true);
                }
                isNewMsg = !isNewMsg;
                break;
            case R.id.ll_zh:
                updateDialogIcon(0);
                updateLanguaue(0);
                DialogManager.getInstance().hide(mLanguaueDialog);
                break;
            case R.id.ll_tw:
                updateDialogIcon(1);
                updateLanguaue(1);
                DialogManager.getInstance().hide(mLanguaueDialog);
                break;
            case R.id.ll_en:
                updateDialogIcon(2);
                updateLanguaue(2);
                DialogManager.getInstance().hide(mLanguaueDialog);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_SETTING_EXIT:
                finish();
                break;
        }
    }

    /**
     * 刷新語言
     *
     * @param index
     */
    private void updateLanguaue(int index) {
        SharedPreUtils.getInstance().setInt("languaue", index);
        EventManager.post(EventManager.EVENT_TYPE_UPDATE_LANGUAUE);
    }
}
