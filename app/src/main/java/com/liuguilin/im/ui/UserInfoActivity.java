package com.liuguilin.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.AddFriendMessage;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.view.DialogView;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * FileName: UserInfoActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 15:40
 * Email: lgl@szokl.com.cn
 * Profile: 用户详情
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private CircleImageView iv_user;
    private ImageView iv_sex;
    private TextView tv_niname;
    private TextView tv_account;
    private Button btn_add;
    private Button btn_momo;

    private DialogView mErrorDialog;
    private TextView tv_content;
    private TextView tv_cancel;
    private TextView tv_city;
    private TextView tv_desc;

    //用戶信息
    public static BmobIMUserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        iv_user = (CircleImageView) findViewById(R.id.iv_user);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        tv_niname = (TextView) findViewById(R.id.tv_niname);
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_momo = (Button) findViewById(R.id.btn_momo);

        include_title_iv_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_momo.setOnClickListener(this);

        //逻辑
        include_title_text.setText(getString(R.string.str_user_info_title_text));

        initErrorDialog();

        if (QueryFriendActivity.mClickimUser != null) {
            String nickName = QueryFriendActivity.mClickimUser.getNickname();
            if (!TextUtils.isEmpty(nickName)) {
                tv_niname.setText(nickName);
            }
            tv_account.setText(QueryFriendActivity.mClickimUser.getUsername());

            iv_sex.setImageResource(QueryFriendActivity.mClickimUser.isSex() ? R.drawable.img_boy : R.drawable.img_girl);

            BmobFile bmobFile = QueryFriendActivity.mClickimUser.getAvatar();
            if (bmobFile != null) {
                String fileUrl = bmobFile.getFileUrl();
                if (!TextUtils.isEmpty(fileUrl)) {
                    GlideUtils.loadImg(this, fileUrl, iv_user);
                } else {
                    iv_user.setImageResource(R.drawable.img_def_photo);
                }
            } else {
                iv_user.setImageResource(R.drawable.img_def_photo);
            }
            String city = QueryFriendActivity.mClickimUser.getCity();
            if (!TextUtils.isEmpty(city)) {
                tv_city.setText(city);
            }
            String desc = QueryFriendActivity.mClickimUser.getDesc();
            if (!TextUtils.isEmpty(desc)) {
                tv_desc.setText(desc);
            }

            //构建聊天方
            info = new BmobIMUserInfo(
                    QueryFriendActivity.mClickimUser.getObjectId(),
                    QueryFriendActivity.mClickimUser.getUsername(),
                    QueryFriendActivity.mClickimUser.getAvatar() == null ? "" : QueryFriendActivity.mClickimUser.getAvatar().getFileUrl());
        } else {
            DialogManager.getInstance().show(mErrorDialog);
        }
    }

    private void initErrorDialog() {
        mErrorDialog = DialogManager.getInstance().initView(this, R.layout.dialog_error, Gravity.CENTER);
        mErrorDialog.setCancelable(false);

        tv_content = (TextView) mErrorDialog.findViewById(R.id.tv_content);
        tv_cancel = (TextView) mErrorDialog.findViewById(R.id.tv_cancel);

        tv_content.setText(getString(R.string.str_user_info_dialog_error));

        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mErrorDialog);
                break;
            case R.id.btn_add:
                CommonUtils.startActivity(this,AddFriendActivity.class,false);
                break;
            case R.id.btn_momo:

                break;
        }
    }

    private void startActivityForBundle(Class<?> cls) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        startActivity(new Intent(this, cls), bundle);
    }
}
