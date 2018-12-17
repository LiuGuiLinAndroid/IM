package com.liuguilin.im.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.db.DBHelper;
import com.liuguilin.im.entity.Constants;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.im.Friend;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.im.NewFriend;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * FileName: NewFriendActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/13 15:51
 * Email: lgl@szokl.com.cn
 * Profile: 新朋友
 */
public class NewFriendActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private RecyclerView mNewFriendRyView;
    private SwipeRefreshLayout mSwLayout;

    private List<NewFriend> mList;
    private List<NewFriend> mDateList = new ArrayList<>();
    private UniversalAdapter<NewFriend> mAdapter;

    private DialogView mDeleteDialog;
    private TextView tv_delete;
    private static int DELETE_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
    }

    private void initView() {

        initDeleteDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        mSwLayout = (SwipeRefreshLayout) findViewById(R.id.mSwLayout);
        mNewFriendRyView = (RecyclerView) findViewById(R.id.mNewFriendRyView);

        include_title_iv_back.setOnClickListener(this);
        mSwLayout.setOnRefreshListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_me_new_friend));

        EventManager.post(EventManager.EVENT_TYPE_NEW_FIREND_UN);

        mNewFriendRyView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UniversalAdapter<>(mDateList, new UniversalAdapter.OnBindDataInterface<NewFriend>() {
            @Override
            public void onBindData(final NewFriend model, UniversalViewHolder hodler, int type, final int position) {
                if (!TextUtils.isEmpty(model.getAvatar())) {
                    hodler.setImageUrl(NewFriendActivity.this, R.id.iv_user, model.getAvatar());
                }
                hodler.setText(R.id.tv_niname, model.getName());
                hodler.setText(R.id.tv_desc, model.getMsg());

                setOpText((Button) hodler.getSubView(R.id.btn_op), model.getStatus());

                hodler.getSubView(R.id.btn_op).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IMLog.i("status:" + model.getStatus());
                        if (model.getStatus() == Constants.MSG_READ) {
                            //发送同意消息
                            agreeAddFriend(model);
                        }
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
                return R.layout.view_list_new_friend;
            }
        });
        mNewFriendRyView.setAdapter(mAdapter);
        mSwLayout.setRefreshing(true);
        getNewFriendList();
    }

    /**
     * 同意添加好友
     *
     * @param model
     */
    private void agreeAddFriend(final NewFriend model) {
        IMLog.i("agreeAddFriend");
        //查询是否已经是好友
        IMSDK.queryFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        boolean isFriend = false;
                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
                            IMUser friendImUser = friend.getImUserFriend();
                            //好友的所有Id
                            String friendObjectId = friendImUser.getObjectId();
                            IMLog.i("friendObjectId:" + friendObjectId);
                            String id = String.valueOf(model.getId());
                            IMLog.i("id:" + id);
                            if (friendObjectId.equals(id)) {
                                isFriend = true;
                                break;
                            }
                        }
                        IMLog.i("isFriend:" + isFriend);
                        if (isFriend) {
                            CommonUtils.Toast(NewFriendActivity.this, getString(R.string.str_toast_to_friend));
                            model.setStatus(Constants.MSG_ADD);
                            DBHelper.update(model);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            addFriend(model);
                        }
                    }else{
                       IMLog.i("size null");
                       //没有好友
                        addFriend(model);
                    }
                } else {
                    CommonUtils.Toast(NewFriendActivity.this, getString(R.string.str_toast_op_fail));
                    IMLog.e(e.toString());
                }
            }
        });
    }

    /**
     * 添加好友
     *
     * @param model
     */
    private void addFriend(final NewFriend model) {
        //添加到好友列表
        IMUser imUser = new IMUser();
        imUser.setObjectId(model.getUid());
        IMLog.i("uid:" + model.getUid());
        IMSDK.addFriend(imUser, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    IMSDK.sendAgreeAddFriendMessage(
                            NewFriendActivity.this, model, new MessageSendListener() {
                                @Override
                                public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                    if (e == null) {
                                        IMLog.i("sendAgreeAddFriendMessage");
                                        //更新此条记录
                                        model.setStatus(Constants.MSG_ADD);
                                        DBHelper.update(model);
                                        mAdapter.notifyDataSetChanged();
                                        //刷新好友
                                        EventManager.post(EventManager.EVENT_TYPE_FRIEND_LIST);
                                        IMLog.i("add friend done");
                                    } else {
                                        IMLog.e(e.toString());
                                    }
                                }
                            });
                } else {
                    IMLog.e(e.toString());
                }
            }
        });
    }

    private void initDeleteDialog() {
        mDeleteDialog = DialogManager.getInstance().initView(this, R.layout.dialog_delete, Gravity.CENTER);
        tv_delete = mDeleteDialog.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);
    }

    private void getNewFriendList() {
        IMLog.i("getNewFriendList");
        mList = (List<NewFriend>) DBHelper.query(NewFriend.class);
        IMLog.i(mList.size() + "");
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                NewFriend newFriend = mList.get(i);
                IMLog.e(newFriend.toString());
                //请求好友的UID
                IMLog.i(newFriend.getUid() + "");
                //自己的UID
                IMLog.i(IMSDK.getCurrentUser().getObjectId() + "");
                if (!newFriend.getUid().equals(IMSDK.getCurrentUser().getObjectId())) {
                    if (newFriend.getStatus() == Constants.MSG_UNREAD) {
                        newFriend.setStatus(Constants.MSG_READ);
                    }
                    DBHelper.update(newFriend);
                    //本帐号下的
                    mDateList.add(newFriend);
                }
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mSwLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.tv_delete:
                IMLog.i("DELETE_ID:" + DELETE_ID);
                if (DELETE_ID >= 0) {
                    if (mDateList.size() > DELETE_ID) {
                        IMLog.i("size:" + mDateList.size());
                        DBHelper.delete(mDateList.get(DELETE_ID));
                        mDateList.remove(DELETE_ID);
                        mAdapter.notifyDataSetChanged();
                        IMLog.i("delete msg");
                    }
                }
                DialogManager.getInstance().hide(mDeleteDialog);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
        if (mDateList != null && mDateList.size() > 0) {
            mDateList.clear();
        }
        getNewFriendList();
    }

    /**
     * 设置状态的文字
     *
     * @param tv
     * @param type
     */
    private void setOpText(Button tv, int type) {
        switch (type) {
            case Constants.MSG_UNREAD:
                tv.setText(getString(R.string.str_new_friend_unread));
                tv.setBackgroundResource(android.R.color.transparent);
                tv.setTextColor(Color.BLACK);
                break;
            case Constants.MSG_READ:
                tv.setText(getString(R.string.str_new_friend_ages));
                tv.setBackgroundResource(R.drawable.shape_all_button_bg);
                tv.setTextColor(Color.WHITE);
                break;
            case Constants.MSG_ADD:
                tv.setText(getString(R.string.str_new_friend_add));
                tv.setBackgroundResource(android.R.color.transparent);
                tv.setTextColor(Color.BLACK);
                break;
        }
    }
}
