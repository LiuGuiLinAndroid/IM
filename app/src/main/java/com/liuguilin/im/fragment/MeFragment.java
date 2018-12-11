package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.ui.UserEditActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;


import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * FileName: MeFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:09
 * Email: lgl@szokl.com.cn
 * Profile: 我
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView iv_user;
    private TextView tv_niname;
    private LinearLayout ll_user;
    private LinearLayout ll_new_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_user = (CircleImageView) view.findViewById(R.id.iv_user);
        tv_niname = (TextView) view.findViewById(R.id.tv_niname);
        ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
        ll_new_friend = (LinearLayout) view.findViewById(R.id.ll_new_friend);

        ll_user.setOnClickListener(this);
        ll_new_friend.setOnClickListener(this);

        //逻辑部分
        IMUser imUser = IMSDK.getCurrentUser();
        if (imUser != null) {
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user:
                CommonUtils.startActivity(getActivity(), UserEditActivity.class, false);
                break;
            case R.id.ll_new_friend:

                break;
        }
    }
}
