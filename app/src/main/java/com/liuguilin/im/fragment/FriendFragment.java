package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.im.Friend;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.ui.ChatActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.NullReceiverView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * FileName: FriendFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:18
 * Email: lgl@szokl.com.cn
 * Profile: 好友
 */
public class FriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mFriendRyView;
    private SwipeRefreshLayout mSwLayout;
    private UniversalAdapter<Friend> mAdapter;
    private List<Friend> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mFriendRyView = (RecyclerView) view.findViewById(R.id.mFriendRyView);
        mSwLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwLayout);

        mSwLayout.setOnRefreshListener(this);

        mFriendRyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new UniversalAdapter<>(mList, new UniversalAdapter.OnBindDataInterface<Friend>() {
            @Override
            public void onBindData(Friend model, final UniversalViewHolder hodler, int type, int position) {
                final IMUser imUser = model.getImUserFriend();
                String objectId = imUser.getObjectId();
                queryFriend(objectId,hodler);

                hodler.getSubView(R.id.ll_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.startActivity(getActivity(),ChatActivity.class,false);
                    }
                });
            }

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.view_list_friend;
            }
        });
        mFriendRyView.setAdapter(mAdapter);

        getFriends();
    }

    /**
     * 查詢好友
     * @param objectId
     * @param hodler
     */
    private void queryFriend(String objectId, final UniversalViewHolder hodler) {
        IMSDK.queryFriend("objectId", objectId, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        IMUser user = list.get(0);
                        //头像
                        BmobFile bmobFile = user.getAvatar();
                        if (bmobFile != null) {
                            String fileUrl = bmobFile.getFileUrl();
                            if (!TextUtils.isEmpty(fileUrl)) {
                                hodler.setImageUrl(getActivity(), R.id.iv_user, fileUrl);
                            }
                        }
                        //性別
                        hodler.setImageResource(R.id.iv_sex, user.isSex() ? R.drawable.img_boy : R.drawable.img_girl);
                        hodler.setText(R.id.tv_niname, TextUtils.isEmpty(user.getNickname()) ? user.getUsername() : user.getNickname());
                        hodler.setText(R.id.tv_desc, TextUtils.isEmpty(user.getDesc()) ? "" : user.getDesc());
                    }
                } else {
                    IMLog.e(e.toString());
                }
            }
        });
    }


    private void getFriends() {
        mSwLayout.setRefreshing(true);
        IMSDK.queryFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                mSwLayout.setRefreshing(false);
                if (e == null) {
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    IMLog.i("getFriends:" + list.size());
                } else {
                    IMLog.e(e.toString());
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
        getFriends();
    }
}
