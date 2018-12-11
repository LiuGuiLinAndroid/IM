package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseFragment;


/**
 * FileName: FriendFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:18
 * Email: lgl@szokl.com.cn
 * Profile: 好友
 */
public class FriendFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        return view;
    }
}
