package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.db.DBHelper;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.im.Friend;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.ui.ChatActivity;
import com.liuguilin.im.ui.QueryFriendActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;
import com.liuguilin.im.view.NullReceiverView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * FileName: FriendFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:18
 * Email: lgl@szokl.com.cn
 * Profile: 好友
 */
public class FriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mFriendRyView;
    private SwipeRefreshLayout mSwLayout;
    private UniversalAdapter<Friend> mAdapter;
    private List<Friend> mList = new ArrayList<>();
    //好友列表
    private List<IMUser> mImUserList = new ArrayList<>();

    private DialogView mDeleteDialog;
    private TextView tv_delete;
    private static int DELETE_ID = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        initDeleteDialog();

        mFriendRyView = (RecyclerView) view.findViewById(R.id.mFriendRyView);
        mSwLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwLayout);

        mSwLayout.setOnRefreshListener(this);

        mFriendRyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new UniversalAdapter<>(mList, new UniversalAdapter.OnBindDataInterface<Friend>() {
            @Override
            public void onBindData(Friend model, final UniversalViewHolder hodler, int type, final int position) {
                final IMUser imUser = model.getImUserFriend();
                String objectId = imUser.getObjectId();
                queryFriend(objectId, hodler);

                hodler.getSubView(R.id.ll_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IMUser im = mImUserList.get(position);
                        //构建聊天方
                        BmobIMUserInfo info = new BmobIMUserInfo(im.getObjectId(), im.getUsername(), im == null ? "" : im.getAvatar().getFileUrl());
                        //构建聊天室
                        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                        CommonUtils.startActivityForBundle(getActivity(), conversationEntrance);
                    }
                });

                hodler.getSubView(R.id.ll_user).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        DELETE_ID = position;
                        DialogManager.getInstance().show(mDeleteDialog);
                        return false;
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

    private void initDeleteDialog() {
        mDeleteDialog = DialogManager.getInstance().initView(getActivity(), R.layout.dialog_delete, Gravity.CENTER);
        tv_delete = mDeleteDialog.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);
    }

    /**
     * 查詢好友
     *
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
                        //滚动
                        hodler.getSubView(R.id.tv_desc).setSelected(true);
                        mImUserList.add(user);
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
                    if (mList != null && mList.size() > 0) {
                        mList.clear();
                    }
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
        getFriends();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                IMLog.i("DELETE_ID:" + DELETE_ID);
                if (DELETE_ID >= 0) {
                    if (mList.size() > DELETE_ID) {
                        IMSDK.deleteFriend(mList.get(DELETE_ID), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mList.remove(DELETE_ID);
                                    mAdapter.notifyDataSetChanged();
                                    CommonUtils.Toast(getActivity(), getString(R.string.str_toast_delete_success));
                                    IMLog.i("delete friend");
                                } else {
                                    CommonUtils.Toast(getActivity(), getString(R.string.str_toast_delete_fail));
                                    IMLog.e(e.toString());
                                }
                            }
                        });
                    }
                }
                DialogManager.getInstance().hide(mDeleteDialog);
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
            case EventManager.EVENT_TYPE_FRIEND_LIST:
                getFriends();
                break;
        }
    }
}
