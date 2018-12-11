package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.im.IMSDK;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * FileName: SessionFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 12:20
 * Email: lgl@szokl.com.cn
 * Profile: 会话
 */
public class SessionFragment extends BaseFragment {

    private RecyclerView mSessionRyView;
    private List<BmobIMConversation> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSessionRyView = (RecyclerView) view.findViewById(R.id.mSessionRyView);

        mList.addAll(IMSDK.loadAllConversation());

    }
}
