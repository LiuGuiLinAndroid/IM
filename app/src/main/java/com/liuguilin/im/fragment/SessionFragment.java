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
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.im.AgreeAddFriendMessage;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.FormatCurrentUtils;
import com.liuguilin.im.utils.IMLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * FileName: SessionFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 12:20
 * Email: lgl@szokl.com.cn
 * Profile: 会话
 */
public class SessionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mSessionRyView;
    private List<BmobIMConversation> mList = new ArrayList<>();
    private List<BmobIMConversation> mDataList = new ArrayList<>();
    private UniversalAdapter<BmobIMConversation> mSessionAdapter;
    private SwipeRefreshLayout mSwLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mSessionRyView = (RecyclerView) view.findViewById(R.id.mSessionRyView);
        mSwLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwLayout);
        mSwLayout.setOnRefreshListener(this);

        mSessionRyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSessionAdapter = new UniversalAdapter<>(mDataList, new UniversalAdapter.OnBindDataInterface<BmobIMConversation>() {
            @Override
            public void onBindData(final BmobIMConversation model, UniversalViewHolder hodler, int type, int position) {
                //设置数量
                long unReadSize = IMSDK.getUnReadCount(model.getConversationId());
                IMLog.i("unReadSize:" + unReadSize);
                if (unReadSize > 0) {
                    hodler.setText(R.id.tv_size, unReadSize + "");
                    hodler.setVisibility(R.id.tv_size, View.VISIBLE);
                } else {
                    hodler.setVisibility(R.id.tv_size, View.INVISIBLE);
                }
                //设置时间
                long time = model.getUpdateTime();
                hodler.setText(R.id.tv_time, FormatCurrentUtils.getTimeRange(getActivity(), time, System.currentTimeMillis() / 1000));
                //设置消息
                setMessage(model, hodler);

                //设置头像和标题
                setPhotoAndTitle(model.getConversationId(), hodler);


                //点击事件
                hodler.getSubView(R.id.ll_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.startActivityForBundle(getActivity(), model);
                    }
                });
            }

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.view_list_chat_list;
            }
        });
        mSessionRyView.setAdapter(mSessionAdapter);
    }

    /**
     * 设置头像和标题
     *
     * @param id
     * @param hodler
     */
    private void setPhotoAndTitle(String id, final UniversalViewHolder hodler) {

        IMSDK.queryFriend("objectId", id, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        IMUser imUser = list.get(0);
                        IMLog.e(imUser.toString());
                        hodler.setText(R.id.tv_niname, TextUtils.isEmpty(imUser.getNickname()) ? imUser.getUsername() : imUser.getNickname());
                        BmobFile bmobFile = imUser.getAvatar();
                        if (bmobFile != null) {
                            String url = bmobFile.getFileUrl();
                            if (!TextUtils.isEmpty(url)) {
                                hodler.setImageUrl(getActivity(), R.id.iv_user, R.drawable.img_def_photo, url);
                            }
                        }
                    }
                } else {
                    IMLog.e(e.toString());
                }
            }
        });
    }

    /**
     * 设置消息
     *
     * @param model
     * @param hodler
     */
    private void setMessage(BmobIMConversation model, UniversalViewHolder hodler) {
        List<BmobIMMessage> mMessageList = model.getMessages();
        String msg = "";
        if (mMessageList != null && mMessageList.size() > 0) {
            //获取最后一条消息(倒序)
            BmobIMMessage message = mMessageList.get(0);
            if (message != null) {
                String content = message.getContent();
                IMLog.i("content:" + content);
                String msgType = message.getMsgType();
                if (msgType.equals(BmobIMMessageType.TEXT.getType()) || msgType.equals(AgreeAddFriendMessage.AGREE)) {
                    msg = content;
                } else if (msgType.equals(BmobIMMessageType.IMAGE.getType())) {
                    msg = getString(R.string.str_chat_image);
                } else if (msgType.equals(BmobIMMessageType.LOCATION.getType())) {
                    msg = getString(R.string.str_chat_location);
                } else if (msgType.equals(BmobIMMessageType.VIDEO.getType())) {
                    msg = getString(R.string.str_chat_video);
                } else if (msgType.equals(BmobIMMessageType.VOICE.getType())) {
                    msg = getString(R.string.str_chat_voice);
                } else {
                    msg = getString(R.string.str_chat_unknow);
                }
            }
        }
        hodler.setText(R.id.tv_msg, msg);
        //滚动
        hodler.getSubView(R.id.tv_msg).setSelected(true);
    }

    /**
     * 加载对话
     */
    private void loadAllConversation() {
        mSwLayout.setRefreshing(true);
        IMLog.i("loadAllConversation");

        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
        }

        List<BmobIMConversation> msgList = IMSDK.loadAllConversation();
        if (msgList != null) {
            mList.addAll(msgList);
        }

        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                BmobIMConversation conversation = mList.get(i);
                IMLog.e(conversation.getMessages().toString());
                List<BmobIMMessage> mMessList = conversation.getMessages();
                if (mMessList.size() > 0) {
                    mDataList.add(conversation);
                }
            }
            mSessionAdapter.notifyDataSetChanged();
        }
        mSwLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_MSG_EVENT:
                //如果收到消息，则刷新
                loadAllConversation();
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadAllConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllConversation();
        //更新总未读
        EventManager.post(EventManager.EVENT_TYPE_MAIN_SIZE);
    }
}
