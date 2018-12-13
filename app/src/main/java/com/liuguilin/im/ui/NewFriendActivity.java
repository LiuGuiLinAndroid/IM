package com.liuguilin.im.ui;

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
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.NewFriend;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.view.DialogView;

import java.util.ArrayList;
import java.util.List;

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
                        if (model.getStatus() == Constants.MSG_READ) {
                            //同意添加
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
                    newFriend.setStatus(Constants.MSG_READ);
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
                if (DELETE_ID > 0) {
                    if (mDateList.size() > DELETE_ID) {
                        DBHelper.delete(mDateList.get(DELETE_ID));
                        mDateList.remove(DELETE_ID);
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
                break;
            case Constants.MSG_READ:
                tv.setText(getString(R.string.str_new_friend_age));
                tv.setBackgroundResource(R.drawable.shape_all_button_bg);
                break;
            case Constants.MSG_ADD:
                tv.setText(getString(R.string.str_new_friend_add));
                break;
            case Constants.MSG_UNADD:
                tv.setText(getString(R.string.str_new_friend_unadd));
                break;
        }
    }
}
