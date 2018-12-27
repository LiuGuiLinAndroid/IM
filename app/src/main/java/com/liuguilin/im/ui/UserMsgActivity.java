package com.liuguilin.im.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.im.Friend;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * FileName: UserMsgActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/27 14:37
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class UserMsgActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private CircleImageView iv_photo;
    private TextView tv_desc;
    private TextView tv_nickname;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_birthday;
    private TextView tv_phone;
    private TextView tv_city;
    private Button btn_delete;

    private IMUser imUser;

    private DialogView mDeleteDialog;
    private TextView tv_content;
    private TextView tv_confirm;
    private TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        initView();
    }

    private void initView() {

        initDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        iv_photo = (CircleImageView) findViewById(R.id.iv_photo);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_city = (TextView) findViewById(R.id.tv_city);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        include_title_iv_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_chat_more_right_text));

        String objectId = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(objectId)) {
            IMSDK.queryFriend("objectId", objectId, new FindListener<IMUser>() {
                @Override
                public void done(List<IMUser> list, BmobException e) {
                    if (list != null && list.size() > 0) {
                        imUser = list.get(0);
                        if (imUser != null) {
                            updateMsg();
                        }
                    }
                }
            });
        }
    }

    private void initDialog() {
        mDeleteDialog = DialogManager.getInstance().initView(this, R.layout.dialog_delete_friend);

        tv_content = (TextView) mDeleteDialog.findViewById(R.id.tv_content);
        tv_confirm = (TextView) mDeleteDialog.findViewById(R.id.tv_confirm);
        tv_cancel = (TextView) mDeleteDialog.findViewById(R.id.tv_cancel);

        tv_confirm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        tv_content.setText(getString(R.string.str_user_info_confirm_delete));
    }

    /**
     * 更新用戶信息
     */
    private void updateMsg() {
        BmobFile bmobFile = imUser.getAvatar();
        if (bmobFile != null) {
            String url = bmobFile.getFileUrl();
            if (!TextUtils.isEmpty(url)) {
                GlideUtils.loadImg(this, url, R.drawable.img_def_photo, iv_photo);
            }
        }

        tv_nickname.setText(TextUtils.isEmpty(imUser.getNickname()) ? imUser.getUsername() : imUser.getNickname());

        tv_desc.setText("" + imUser.getDesc());
        tv_sex.setText(imUser.isSex() ? getString(R.string.str_common_boy) : getString(R.string.str_common_girl));
        tv_age.setText("" + imUser.getAge());
        tv_birthday.setText("" + imUser.getBirthday());
        tv_phone.setText("" + imUser.getMobilePhoneNumber());
        tv_city.setText("" + imUser.getCity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.btn_delete:
                DialogManager.getInstance().show(mDeleteDialog);
                break;
            case R.id.tv_confirm:
                DialogManager.getInstance().hide(mDeleteDialog);
                //查询一遍
                IMSDK.queryFriends(new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                Friend friend = list.get(i);
                                IMUser imUserFriend = friend.getImUserFriend();
                                if (imUserFriend.getObjectId().equals(imUser.getObjectId())) {
                                    IMSDK.deleteFriend(friend, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                CommonUtils.Toast(UserMsgActivity.this, getString(R.string.str_toast_delete_success));
                                                EventManager.post(EventManager.EVENT_TYPE_FRIEND_LIST);
                                                finish();
                                            } else {
                                                IMLog.e(e.toString());
                                                CommonUtils.Toast(UserMsgActivity.this, getString(R.string.str_toast_delete_fail));
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mDeleteDialog);
                break;
        }
    }
}
