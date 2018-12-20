package com.liuguilin.im.receiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.liuguilin.im.MainActivity;
import com.liuguilin.im.R;
import com.liuguilin.im.db.DBHelper;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.im.AddFriendMessage;
import com.liuguilin.im.im.AgreeAddFriendMessage;
import com.liuguilin.im.im.Friend;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.im.NewFriend;
import com.liuguilin.im.ui.NewFriendActivity;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.utils.SharedPreUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * FileName: IMHandlerReceiver
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 16:58
 * Email: lgl@szokl.com.cn
 * Profile: 消息接收器
 */
public class IMHandlerReceiver extends BmobIMMessageHandler {

    private Context mContext;
    private Bitmap mBitmap;

    public IMHandlerReceiver(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 在线消息
     *
     * @param messageEvent
     */
    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        executeMessage(messageEvent);
    }

    /**
     * 离线消息
     *
     * @param offlineMessageEvent
     */
    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        IMLog.i("onOfflineReceive:" + map.size());
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                executeMessage(list.get(i));
            }
        }
    }

    /**
     * 处理消息
     *
     * @param messageEvent
     */
    private void executeMessage(final MessageEvent messageEvent) {
        IMLog.i("executeMessage");
        IMSDK.updateUserInfo(messageEvent, new IMSDK.OnResultListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    BmobIMMessage msg = messageEvent.getMessage();
                    if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {
                        //自定义消息类型
                        processCustomMessage(msg, messageEvent.getFromUserInfo());
                    } else {
                        //内部消息类型
                        processSDKMessage(msg, messageEvent);
                    }
                }
            }
        });
    }

    /**
     * 处理SDK消息类型
     *
     * @param msg
     * @param messageEvent
     */
    private void processSDKMessage(BmobIMMessage msg, MessageEvent messageEvent) {
        IMLog.i("processSDKMessage:" + msg.toString());
        EventManager.postMessageEvent(EventManager.EVENT_TYPE_MSG_EVENT, messageEvent);
        BmobIMUserInfo fromUserInfo = messageEvent.getFromUserInfo();
        //并且弹出通知栏
        showNotify(MainActivity.class, mContext.getString(R.string.str_toast_have_msg), fromUserInfo.getName(), msg.getContent(), msg);
    }

    /**
     * 处理自定义消息类型
     *
     * @param msg
     * @param fromUserInfo
     */
    private void processCustomMessage(final BmobIMMessage msg, final BmobIMUserInfo fromUserInfo) {
        IMLog.i("processCustomMessage:" + msg.toString());
        //消息类型
        String type = msg.getMsgType();
        switch (type) {
            //本地做查询记录
            case AddFriendMessage.ADD:
                final NewFriend friend = AddFriendMessage.convert(msg);
                DBHelper.save(friend);
                EventManager.post(EventManager.EVENT_TYPE_NEW_FIREND);
                showNotify(NewFriendActivity.class, mContext.getString(R.string.str_toast_quest_friend), friend.getName(), friend.getMsg(), msg);
                break;
            //云端操作
            case AgreeAddFriendMessage.AGREE:
                final AgreeAddFriendMessage message = AgreeAddFriendMessage.convert(msg);
                //添加好友
                IMUser imUser = new IMUser();
                imUser.setObjectId(msg.getFromId());
                IMSDK.addFriend(imUser, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            showNotify(MainActivity.class, mContext.getString(R.string.str_toast_agree_friend), fromUserInfo.getName(), message.getMsg(), msg);
                            EventManager.post(EventManager.EVENT_TYPE_FRIEND_LIST);
                        } else {
                            IMLog.e(e.toString());
                        }
                    }
                });
                break;
        }
    }

    /**
     * 显示通知
     *
     * @param text    消息内容
     * @param name    消息名称
     * @param userMsg 用户消息
     * @param msg     消息实体
     */
    private void showNotify(final Class<?> cls, final String text, final String name, final String userMsg, final BmobIMMessage msg) {

        if (!SharedPreUtils.getInstance().getBool("newMsg", true)) {
            return;
        }

        IMSDK.queryFriend("objectId", msg.getFromId(), new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                String title = "";
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        //昵称
                        IMUser imUser = list.get(0);
                        String nick = imUser.getNickname();
                        if (!TextUtils.isEmpty(nick)) {
                            title = nick;
                        } else {
                            title = imUser.getUsername();
                        }
                        //头像
                        BmobFile bmobFile = imUser.getAvatar();
                        if (bmobFile != null) {
                            String fileUrl = bmobFile.getFileUrl();
                            mBitmap = returnBitMap(fileUrl);
                        }
                    } else {
                        title = name;
                    }
                } else {
                    IMLog.e(e.toString());
                    title = name;
                }
                if (mBitmap == null) {
                    mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_def_photo);
                }
                //显示通知
                IMSDK.showNotificaition(mContext, cls, mBitmap, title, text, userMsg);
            }
        });
    }

    /**
     * 转换
     *
     * @param url
     * @return
     */
    public Bitmap returnBitMap(final String url) {
        mBitmap = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    mBitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return mBitmap;
    }
}
