package com.liuguilin.im.fragment;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.ui.MyQrCodeActivity;
import com.liuguilin.im.ui.NewFriendActivity;
import com.liuguilin.im.ui.UserEditActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * FileName: MeFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:09
 * Email: lgl@szokl.com.cn
 * Profile: 我
 */
public class MeFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    private CircleImageView iv_user;
    private TextView tv_niname;
    private TextView tv_account;
    private ImageView iv_sex;
    private LinearLayout ll_user;
    private LinearLayout ll_new_friend;
    private ImageView iv_new_firend_point;
    private LinearLayout ll_qrcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_user = (CircleImageView) view.findViewById(R.id.iv_user);
        tv_niname = (TextView) view.findViewById(R.id.tv_niname);
        tv_account = (TextView) view.findViewById(R.id.tv_account);
        iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
        iv_new_firend_point = (ImageView) view.findViewById(R.id.iv_new_firend_point);
        ll_new_friend = (LinearLayout) view.findViewById(R.id.ll_new_friend);
        ll_qrcode = (LinearLayout) view.findViewById(R.id.ll_qrcode);

        ll_user.setOnClickListener(this);
        ll_new_friend.setOnClickListener(this);
        ll_qrcode.setOnTouchListener(this);

        //逻辑部分
        updateUserInfo();
    }

    private void updateUserInfo() {
        IMUser imUser = IMSDK.getCurrentUser();
        if (imUser != null) {
            tv_account.setText(imUser.getUsername());
            String nickName = imUser.getNickname();
            if (!TextUtils.isEmpty(nickName)) {
                tv_niname.setText(nickName);
            } else {
                tv_niname.setText(getString(R.string.str_me_not_nickname));
            }
            BmobFile userFile = imUser.getAvatar();
            if (userFile != null) {
                String url = userFile.getFileUrl();
                if (!TextUtils.isEmpty(url)) {
                    GlideUtils.loadImg(getActivity(), url, iv_user);
                }
            }
            iv_sex.setImageResource(imUser.isSex() ? R.drawable.img_boy : R.drawable.img_girl);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user:
                CommonUtils.startActivity(getActivity(), UserEditActivity.class, false);
                break;
            case R.id.ll_new_friend:
                CommonUtils.startActivity(getActivity(), NewFriendActivity.class, false);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_NEW_FIREND:
                iv_new_firend_point.setVisibility(View.VISIBLE);
                break;
            case EventManager.EVENT_TYPE_NEW_FIREND_UN:
                iv_new_firend_point.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ll_qrcode:
                CommonUtils.startActivity(getActivity(), MyQrCodeActivity.class, false);
                break;
        }
        return true;
    }
}
