package com.liuguilin.im.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.BlockedNumberContract;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.bean.ImChatBean;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.list.TimeComparison;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.utils.PermissionUtils;
import com.liuguilin.im.utils.PictureUtils;
import com.liuguilin.im.view.DialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * FileName: ChatActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 17:23
 * Email: lgl@szokl.com.cn
 * Profile: 聊天
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {

    private static int TAKEPHOTO = 1100;
    private static int TAKEALBUM = 1101;

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private ImageView iv_voice;
    private EditText et_text;
    private TextView tv_send_voice;
    private ImageView iv_emoji;
    private ImageView iv_more;
    private LinearLayout ll_camera;
    private LinearLayout ll_album;
    private LinearLayout ll_location;
    private LinearLayout ll_file;
    private LinearLayout ll_more;
    private LinearLayout ll_emoji;
    private Button btn_send;
    private RecyclerView mChatRyView;
    private SwipeRefreshLayout mSwLayout;

    private DialogView mVoiceDialog;

    private BmobIMConversation mConversationManager;

    private UniversalAdapter<ImChatBean> mAdapter;
    private List<ImChatBean> mList = new ArrayList<>();
    private List<BmobIMMessage> mMessage = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    //聊天对象的头像
    private static String chatPhotoUrl = "";

    private File tempFile;
    private Uri imageUri;
    private String uploadPhotoPath;

    private Uri uri;
    private String path = "";

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initChat();
        initView();
    }

    /**
     * 初始化聊天组件
     */
    private void initChat() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            BmobIMConversation conversationEntrance = (BmobIMConversation) bundle.getSerializable("c");
            mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
            String id = mConversationManager.getConversationId();
            IMSDK.queryFriend("objectId", id, new FindListener<IMUser>() {
                @Override
                public void done(List<IMUser> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            IMUser imUser = list.get(0);
                            IMLog.e(imUser.toString());
                            include_title_text.setText(TextUtils.isEmpty(imUser.getNickname()) ? imUser.getUsername() : imUser.getNickname());
                            BmobFile bmobFile = imUser.getAvatar();
                            if (bmobFile != null) {
                                String url = bmobFile.getFileUrl();
                                if (!TextUtils.isEmpty(url)) {
                                    chatPhotoUrl = url;
                                }
                            }
                        }
                    } else {
                        IMLog.e(e.toString());
                    }
                }
            });
        }
    }

    private void initView() {

        initVoiceDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);

        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_send_voice = (TextView) findViewById(R.id.tv_send_voice);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_file = (LinearLayout) findViewById(R.id.ll_file);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        ll_emoji = (LinearLayout) findViewById(R.id.ll_emoji);
        mChatRyView = (RecyclerView) findViewById(R.id.mChatRyView);
        mSwLayout = (SwipeRefreshLayout) findViewById(R.id.mSwLayout);

        include_title_iv_back.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        tv_send_voice.setOnTouchListener(this);
        btn_send.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_album.setOnClickListener(this);
        mSwLayout.setOnRefreshListener(this);

        title_right_text.setText(getString(R.string.str_chat_more_right_text));

        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                    iv_more.setVisibility(View.GONE);
                } else {
                    btn_send.setVisibility(View.GONE);
                    iv_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLinearLayoutManager = new LinearLayoutManager(this);
        mChatRyView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new UniversalAdapter<>(mList, new UniversalAdapter.OnMultiTypeBindDataInterface<ImChatBean>() {
            @Override
            public int getItemViewType(int postion) {
                return mList.get(postion).getType();
            }

            @Override
            public void onBindData(final ImChatBean model, final UniversalViewHolder hodler, int type, int position) {
                int msgType = model.getType();
                switch (msgType) {
                    case ImChatBean.MSG_TIME:
                        hodler.setText(R.id.tv_time, model.getTime());
                        break;
                    case ImChatBean.MSG_TIPS:
                        hodler.setText(R.id.tv_tips, model.getTips());
                        break;
                    case ImChatBean.MSG_LEFT_TEXT:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        hodler.setText(R.id.tv_left_text, model.getMsgText());
                        break;
                    case ImChatBean.MSG_RIGHT_TEXT:
                        hodler.setText(R.id.tv_right_text, model.getMsgText());
                        setUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_LEFT_IMG:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        setMsgImg(model.getMsgImg(), hodler, R.id.iv_left_img);
                        break;
                    case ImChatBean.MSG_RIGHT_IMG:
                        setUserPhoto(hodler, R.id.iv_photo);
                        setMsgImg(model.getMsgImg(), hodler, R.id.iv_right_img);
                        break;
                    case ImChatBean.MSG_LEFT_LOCATION:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_RIGHT_LOCATION:
                        setUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_LEFT_VOICE:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_RIGHT_VOICE:
                        setUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_LEFT_VIDEO:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        IMLog.e("left:" + model.getMsgVideo());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Bitmap bitmap = CommonUtils.createVideoThumb(model.getMsgVideo(), 200, 150);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hodler.setImageBitmap(R.id.iv_left_video, bitmap);
                                    }
                                });
                            }
                        }).start();
                        break;
                    case ImChatBean.MSG_RIGHT_VIDEO:
                        setUserPhoto(hodler, R.id.iv_photo);
                        IMLog.e("right:" + model.getMsgVideo());
                        hodler.setImageBitmap(R.id.iv_right_video, CommonUtils.createVideoThumb(model.getMsgVideo()));
                        break;
                    case ImChatBean.MSG_LEFT_FILE:
                        setChatUserPhoto(hodler, R.id.iv_photo);
                        break;
                    case ImChatBean.MSG_RIGHT_FILE:
                        setUserPhoto(hodler, R.id.iv_photo);
                        break;
                }
            }

            @Override
            public int getItemLayoutId(int viewType) {
                if (viewType == ImChatBean.MSG_TIME) {
                    return R.layout.view_list_chat_time;
                } else if (viewType == ImChatBean.MSG_TIPS) {
                    return R.layout.view_list_chat_tips;
                } else if (viewType == ImChatBean.MSG_LEFT_TEXT) {
                    return R.layout.view_list_chat_left_text;
                } else if (viewType == ImChatBean.MSG_RIGHT_TEXT) {
                    return R.layout.view_list_chat_right_text;
                } else if (viewType == ImChatBean.MSG_LEFT_IMG) {
                    return R.layout.view_list_chat_left_img;
                } else if (viewType == ImChatBean.MSG_RIGHT_IMG) {
                    return R.layout.view_list_chat_right_img;
                } else if (viewType == ImChatBean.MSG_LEFT_LOCATION) {
                    return R.layout.view_list_chat_left_location;
                } else if (viewType == ImChatBean.MSG_RIGHT_LOCATION) {
                    return R.layout.view_list_chat_right_location;
                } else if (viewType == ImChatBean.MSG_LEFT_VOICE) {
                    return R.layout.view_list_chat_left_voice;
                } else if (viewType == ImChatBean.MSG_RIGHT_VOICE) {
                    return R.layout.view_list_chat_right_voice;
                } else if (viewType == ImChatBean.MSG_LEFT_VIDEO) {
                    return R.layout.view_list_chat_left_video;
                } else if (viewType == ImChatBean.MSG_RIGHT_VIDEO) {
                    return R.layout.view_list_chat_right_video;
                } else if (viewType == ImChatBean.MSG_LEFT_FILE) {
                    return R.layout.view_list_chat_left_file;
                } else if (viewType == ImChatBean.MSG_RIGHT_FILE) {
                    return R.layout.view_list_chat_right_file;
                }
                return 0;
            }
        });
        mChatRyView.setAdapter(mAdapter);

        queryMessage(null);
    }

    /**
     * 设置聊天图片
     *
     * @param msgImg
     */
    private void setMsgImg(String msgImg, UniversalViewHolder hodler, int viewId) {
        IMLog.i("msgImg" + msgImg + "viewId:" + viewId);
        if (!TextUtils.isEmpty(msgImg)) {
            if (msgImg.contains("&")) {
                String[] list = msgImg.split("&");
                if (list != null) {
                    if (list.length > 0) {
                        if (!TextUtils.isEmpty(list[0])) {
                            hodler.setImagePath(ChatActivity.this, viewId, R.drawable.img_load_img, list[0]);
                        } else {
                            if (list.length > 1) {
                                if (!TextUtils.isEmpty(list[1])) {
                                    hodler.setImageUrl(ChatActivity.this, viewId, R.drawable.img_load_img, list[1]);
                                }
                            }
                        }
                    }
                }
            } else {
                if (msgImg.startsWith("http:")) {
                    hodler.setImageUrl(ChatActivity.this, viewId, R.drawable.img_load_img, msgImg);
                } else {
                    hodler.setImagePath(ChatActivity.this, viewId, R.drawable.img_load_img, msgImg);
                }
            }
        }
    }

    /**
     * 设置聊天对象的头像
     *
     * @param viewId
     */
    private void setChatUserPhoto(UniversalViewHolder holder, int viewId) {
        if (!TextUtils.isEmpty(chatPhotoUrl)) {
            holder.setImageUrl(ChatActivity.this, viewId, R.drawable.img_def_photo, chatPhotoUrl);
        }
    }

    /**
     * 设置头像
     *
     * @param holder
     * @param viewId
     */
    private void setUserPhoto(UniversalViewHolder holder, int viewId) {
        IMUser imUser = IMSDK.getCurrentUser();
        BmobFile bmobFile = imUser.getAvatar();
        if (bmobFile != null) {
            String url = bmobFile.getFileUrl();
            if (!TextUtils.isEmpty(url)) {
                holder.setImageUrl(ChatActivity.this, viewId, R.drawable.img_def_photo, url);
            }
        }
    }

    /**
     * `
     * 查询聊天记录
     */
    private void queryMessage(BmobIMMessage msg) {
        IMLog.i("queryMessage");
        IMSDK.queryMessage(mConversationManager, msg, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                mSwLayout.setRefreshing(false);
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            BmobIMMessage message = list.get(i);
                            mMessage.add(message);
                            IMLog.i(message.toString());
                            executeMessage(message);
                        }
                    }
                } else {
                    IMLog.e(e.toString());
                }
            }
        });
    }

    /**
     * 解析消息
     *
     * @param message
     */
    private void executeMessage(BmobIMMessage message) {
        //加一层判断 是否是出于这个对话场景

        //根据发送的Id来判断
        String id = message.getFromId();
        //相等 自己
        if (IMSDK.getCurrentUser().getObjectId().equals(id)) {
            switch (message.getMsgType()) {
                case "txt":
                    insertText(message.getCreateTime(), ImChatBean.MSG_RIGHT_TEXT, message.getContent());
                    break;
                case "image":
                    insertImg(message.getCreateTime(), ImChatBean.MSG_RIGHT_IMG, message.getContent());
                    break;
                case "video":
                    insertVideo(message.getCreateTime(), ImChatBean.MSG_RIGHT_VIDEO, message.getContent());
                    break;
            }
        } else {
            switch (message.getMsgType()) {
                case "agree":
                    insertTips(message.getCreateTime(), message.getContent());
                    break;
                case "txt":
                    insertText(message.getCreateTime(), ImChatBean.MSG_LEFT_TEXT, message.getContent());
                    break;
                case "image":
                    insertImg(message.getCreateTime(), ImChatBean.MSG_LEFT_IMG, message.getContent());
                    break;
                case "video":
                    insertVideo(message.getCreateTime(), ImChatBean.MSG_LEFT_VIDEO, message.getContent());
                    break;
            }
        }
    }

    private void initVoiceDialog() {
        mVoiceDialog = DialogManager.getInstance().initView(this, R.layout.dialog_voice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.iv_voice:
                if (et_text.getVisibility() == View.VISIBLE) {
                    iv_voice.setImageResource(R.drawable.img_key);
                    tv_send_voice.setVisibility(View.VISIBLE);
                    et_text.setVisibility(View.GONE);

                    iv_emoji.setImageResource(R.drawable.img_emoji);
                    if (ll_emoji.getVisibility() == View.VISIBLE) {
                        ll_emoji.setVisibility(View.GONE);
                    }
                    if (ll_more.getVisibility() == View.VISIBLE) {
                        ll_more.setVisibility(View.GONE);
                    }
                    if (btn_send.getVisibility() == View.VISIBLE) {
                        btn_send.setVisibility(View.GONE);
                    }

                } else {
                    iv_voice.setImageResource(R.drawable.img_voice);
                    tv_send_voice.setVisibility(View.GONE);
                    et_text.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_emoji:
                iv_voice.setImageResource(R.drawable.img_voice);
                tv_send_voice.setVisibility(View.GONE);
                et_text.setVisibility(View.VISIBLE);

                if (ll_emoji.getVisibility() == View.VISIBLE) {
                    iv_emoji.setImageResource(R.drawable.img_emoji);
                    ll_emoji.setVisibility(View.GONE);
                } else {
                    iv_emoji.setImageResource(R.drawable.img_key);
                    ll_emoji.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_more:
                iv_emoji.setImageResource(R.drawable.img_emoji);
                if (ll_emoji.getVisibility() == View.VISIBLE) {
                    ll_emoji.setVisibility(View.GONE);
                }
                iv_voice.setImageResource(R.drawable.img_voice);
                tv_send_voice.setVisibility(View.GONE);
                et_text.setVisibility(View.VISIBLE);

                if (ll_more.getVisibility() == View.VISIBLE) {
                    ll_more.setVisibility(View.GONE);
                } else {
                    ll_more.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_send:
                String msgText = et_text.getText().toString().trim();
                sendTextMsg(msgText);
                break;
            case R.id.ll_camera:
                toCamera();
                break;
            case R.id.ll_album:
                toAlbum();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                IMLog.i("ACTION_DOWN");
                DialogManager.getInstance().show(mVoiceDialog);
                break;
            case MotionEvent.ACTION_MOVE:
                IMLog.i("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                IMLog.i("ACTION_UP");
                DialogManager.getInstance().hide(mVoiceDialog);
                break;
        }
        return false;
    }

    /**
     * 插入时间
     *
     * @param time
     */
    private void insertTime(long updateTime, String time) {
        ImChatBean bean = new ImChatBean();
        bean.setType(ImChatBean.MSG_TIME);
        bean.setTime(time);
        insertListData(bean, updateTime);
    }

    /**
     * 插入提示语
     *
     * @param tips
     */
    private void insertTips(long updateTime, String tips) {
        ImChatBean bean = new ImChatBean();
        bean.setType(ImChatBean.MSG_TIPS);
        bean.setTips(tips);
        insertListData(bean, updateTime);
    }

    /**
     * 插入文本
     *
     * @param type 左右
     * @param text 文本
     */
    private void insertText(long updateTime, int type, String text) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgText(text);
        insertListData(bean, updateTime);
    }

    /**
     * 插入图片
     *
     * @param type    左右
     * @param imgPath 图片地址
     */
    private void insertImg(long updateTime, int type, String imgPath) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgImg(imgPath);
        insertListData(bean, updateTime);
    }

    /**
     * 插入地址
     *
     * @param type     左右
     * @param location 地址
     */
    private void insertLocation(long updateTime, int type, String location) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgLocation(location);
        insertListData(bean, updateTime);
    }

    /**
     * 插入语音
     *
     * @param type  左右
     * @param voice 语音地址
     */
    private void insertVoice(long updateTime, int type, String voice) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgVoice(voice);
        insertListData(bean, updateTime);
    }

    /**
     * 插入视频
     *
     * @param type  左右
     * @param video 视频地址
     */
    private void insertVideo(long updateTime, int type, String video) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgVideo(video);
        insertListData(bean, updateTime);
    }

    /**
     * 插入文件
     *
     * @param type
     * @param file
     */
    private void insertFile(long updateTime, int type, String file) {
        ImChatBean bean = new ImChatBean();
        bean.setType(type);
        bean.setMsgFile(file);
        insertListData(bean, updateTime);
    }

    /**
     * 插入数据并刷新
     *
     * @param bean
     */
    private void insertListData(ImChatBean bean, long updateTime) {
        bean.setUpdateTime(updateTime);
        mList.add(bean);
        //先按创建时间排序
        Collections.sort(mList, new TimeComparison());
        mAdapter.notifyDataSetChanged();
        //滚动底部
        mLinearLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
    }

    /**
     * 消息发送监听器
     */
    private MessageSendListener mMessageSendListener = new MessageSendListener() {

        @Override
        public void onStart(BmobIMMessage bmobIMMessage) {
            super.onStart(bmobIMMessage);
            IMLog.i("onStart");
        }

        @Override
        public void onProgress(int i) {
            super.onProgress(i);
            IMLog.i("onProgress:" + i);
        }

        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            IMLog.i("done");
            et_text.setText("");
            if (e != null) {
                IMLog.i(e.toString());
            }
        }
    };

    /**
     * 发送文本消息
     *
     * @param text
     */
    private void sendTextMsg(String text) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        insertText(getSyTime(),ImChatBean.MSG_RIGHT_TEXT, text);
        mConversationManager.sendMessage(msg, mMessageSendListener);
    }

    /**
     * 发送图片
     *
     * @param path
     */
    private void sendImgMsg(String path) {
        BmobIMImageMessage image = new BmobIMImageMessage(path);
        insertImg(getSyTime(),ImChatBean.MSG_RIGHT_IMG, path);
        mConversationManager.sendMessage(image, mMessageSendListener);
    }

    /**
     * 发送视频文件
     *
     * @param path
     */
    private void sendVideoMsg(String path) {
        BmobIMVideoMessage video = new BmobIMVideoMessage(path);
        insertVideo(getSyTime(),ImChatBean.MSG_RIGHT_VIDEO, path);
        mConversationManager.sendMessage(video, mMessageSendListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_MSG_EVENT:
                executeMessage(event.getMessageEvent().getMessage());
                break;
        }
    }

    /**
     * 相机
     */
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        tempFile = new File(Environment.getExternalStorageDirectory(), filename + ".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 从文件中创建uri
            imageUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intent, TAKEPHOTO);
    }

    /**
     * 相册
     */
    private void toAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("video/*;image/*");
        startActivityForResult(photoPickerIntent, TAKEALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKEPHOTO) {
                if (!TextUtils.isEmpty(tempFile.getPath())) {
                    uploadPhotoPath = tempFile.getPath();
                }
            } else if (requestCode == TAKEALBUM) {
                if (data != null) {
                    uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        path = this.uri.getPath();
                        path = PictureUtils.getPath_above19(this, uri);
                    } else {
                        path = PictureUtils.getFilePath_below19(this, this.uri);
                    }
                    if (!TextUtils.isEmpty(path)) {
                        uploadPhotoPath = path;
                    }
                }
            }
            if (!TextUtils.isEmpty(uploadPhotoPath)) {
                //根据后缀判断文件
                if (uploadPhotoPath.endsWith(".mp4")) {
                    sendVideoMsg(uploadPhotoPath);
                } else if (uploadPhotoPath.endsWith(".jpg") || uploadPhotoPath.endsWith(".png")) {
                    sendImgMsg(uploadPhotoPath);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        if (mMessage != null && mMessage.size() > 0) {
            IMLog.e(mMessage.toString());
            queryMessage(mMessage.get(0));
        }
    }

    private long getSyTime() {
        return System.currentTimeMillis();
    }
}
