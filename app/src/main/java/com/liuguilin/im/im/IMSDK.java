package com.liuguilin.im.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.liuguilin.im.MainActivity;
import com.liuguilin.im.R;
import com.liuguilin.im.entity.Constants;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * FileName: IMSDK
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 9:59
 * Email: lgl@szokl.com.cn
 * Profile: IM
 */
public class IMSDK {


    /**
     * 获取当前用户信息
     *
     * @return
     */
    public static IMUser getCurrentUser() {
        IMUser user = BmobUser.getCurrentUser(IMUser.class);
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param imUser
     * @param listener
     */
    public static void updateUser(IMUser imUser, UpdateListener listener) {
        imUser.update(listener);
    }

    /**
     * 获取短信
     *
     * @param phone
     * @param listener
     */
    public static void requestSMSCode(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone, "DataSDK", listener);
    }

    /**
     * 验证短信验证码
     *
     * @param phone
     * @param code
     * @param listener
     */
    public static void verifySmsCode(String phone, String code, UpdateListener listener) {
        BmobSMS.verifySmsCode(phone, code, listener);
    }

    /**
     * 验证码重置密码
     *
     * @param code
     * @param newPassword
     * @param listener
     */
    public static void resetPasswordBySMSCode(String code, String newPassword, UpdateListener listener) {
        BmobUser.resetPasswordBySMSCode(code, newPassword, listener);
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @param listener 回调
     */
    public static void regUser(Context mContext, String username, String password, SaveListener<IMUser> listener) {
        IMUser imUser = new IMUser();
        imUser.setUsername(username);
        imUser.setPassword(password);
        imUser.setDesc(mContext.getString(R.string.str_reg_no_desc));
        imUser.signUp(listener);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param listener 回调
     */
    public static void login(String username, String password, SaveListener<IMUser> listener) {
        IMUser imUser = new IMUser();
        imUser.setUsername(username);
        imUser.setPassword(password);
        imUser.login(listener);
    }

    /**
     * 连接IM服务器
     *
     * @param listener
     */
    public static void connect(ConnectListener listener) {
        IMUser imUser = getCurrentUser();
        if (imUser != null) {
            if (!TextUtils.isEmpty(imUser.getObjectId())) {
                BmobIM.connect(imUser.getObjectId(), listener);
            } else {
                IMLog.e("user error");
            }
        }
    }

    /**
     * 断开连接
     */
    public static void disConnect() {
        BmobIM.getInstance().disConnect();
    }

    /**
     * 监听连接状态
     *
     * @param listener
     */
    public static void setOnConnectStatusChangeListener(ConnectStatusChangeListener listener) {
        BmobIM.getInstance().setOnConnectStatusChangeListener(listener);
    }

    /**
     * 查询全部会话
     */
    public static List<BmobIMConversation> loadAllConversation() {
        return BmobIM.getInstance().loadAllConversation();
    }

    /**
     * 查询指定会话的未读数量
     *
     * @param conversationId
     * @return
     */
    public static long getUnReadCount(String conversationId) {
        return BmobIM.getInstance().getUnReadCount(conversationId);
    }

    /**
     * 查询全部未读数量
     *
     * @return
     */
    public static long getAllUnReadCount() {
        return BmobIM.getInstance().getAllUnReadCount();
    }

    /**
     * 删除会话
     *
     * @param c
     */
    public static void deleteConversation(BmobIMConversation c) {
        BmobIM.getInstance().deleteConversation(c);
    }

    /**
     * 清理全部会话
     */
    public static void clearAllConversation() {
        BmobIM.getInstance().clearAllConversation();
    }

    /**
     * 查询用户
     *
     * @param key
     * @param values
     * @param listener
     */
    public static void queryFriend(String key, String values, FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key, values);
        query.findObjects(listener);
    }

    /**
     * 查询好友列表
     *
     * @param listener
     */
    public static void queryFriends(FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        IMUser imUser = getCurrentUser();
        query.addWhereEqualTo("imUser", imUser);
        query.findObjects(listener);
    }


    /**
     * 发送添加好友消息
     *
     * @param info     会话
     * @param content  留言
     * @param listener
     */
    public static void sendAddFriendMessage(BmobIMUserInfo info, String content, MessageSendListener listener) {
        if (info != null) {
            //创建一个暂态的会话入口
            BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
            //根据会话入口获取消息管理
            BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
            AddFriendMessage msg = new AddFriendMessage();
            IMUser imUser = getCurrentUser();
            //给对方的一个留言信息
            msg.setContent(content);
            Map<String, Object> map = new HashMap<>();
            //发送者姓名
            map.put("name", imUser.getUsername());
            //发送者的头像
            map.put("avatar", imUser.getAvatar().getFileUrl());
            //发送者的uid
            map.put("uid", imUser.getObjectId());
            msg.setExtraMap(map);
            messageManager.sendMessage(msg, listener);
        }
    }

    /**
     * 发送同意添加好友的消息
     *
     * @param mContext
     * @param add
     * @param listener
     */
    public static void sendAgreeAddFriendMessage(Context mContext, NewFriend add, MessageSendListener listener) {
        //临时会话
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        IMUser imUser = getCurrentUser();
        msg.setContent(mContext.getString(R.string.str_add_friend_desc));
        Map<String, Object> map = new HashMap<>();
        map.put("msg", imUser.getUsername() + mContext.getString(R.string.str_add_friend_msg));
        map.put("uid", add.getUid());
        map.put("time", add.getTime());
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, listener);
    }

    /**
     * 更新用户信息
     *
     * @param event
     */
    public static void updateUserInfo(MessageEvent event, final OnResultListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        final String avatar = info.getAvatar();
        String title = conversation.getConversationTitle();
        String icon = conversation.getConversationIcon();
        if (username.equals(title)) {
            if (!TextUtils.isEmpty(avatar)) {
                if (avatar.equals(icon)) {
                    queryFriend("objectId", info.getUserId(), new FindListener<IMUser>() {
                        @Override
                        public void done(List<IMUser> list, BmobException e) {
                            if (list != null && list.size() == 1) {
                                IMUser imUser = list.get(0);
                                String name = imUser.getUsername();
                                BmobFile file = imUser.getAvatar();
                                if (file != null) {
                                    String avatar = file.getFileUrl();
                                    info.setAvatar(avatar);
                                    conversation.setConversationIcon(avatar);
                                }
                                info.setName(name);
                                conversation.setConversationTitle(name);
                                //更新用戶資料
                                BmobIM.getInstance().updateUserInfo(info);
                                if (!msg.isTransient()) {
                                    BmobIM.getInstance().updateConversation(conversation);
                                }
                                listener.done(null);
                            } else {
                                IMLog.e(e.toString());
                                listener.done(e);
                            }
                        }
                    });
                } else {
                    listener.done(null);
                }
            } else {
                listener.done(null);
            }
        } else {
            listener.done(null);
        }
    }

    /**
     * 结果接口
     */
    public interface OnResultListener {
        void done(BmobException e);
    }

    /**
     * 添加好友
     *
     * @param imUserFriend
     * @param listener
     */
    public static void addFriend(IMUser imUserFriend, SaveListener<String> listener) {
        Friend friend = new Friend();
        friend.setImUser(getCurrentUser());
        friend.setImUserFriend(imUserFriend);
        friend.save(listener);
    }

    /**
     * 查询指定会话的聊天记录
     *
     * @param mConversationManager
     * @param msg
     * @param listener
     */
    public static void queryMessage(BmobIMConversation mConversationManager, BmobIMMessage msg, MessagesQueryListener listener) {
        mConversationManager.queryMessages(msg, 10, listener);
    }

    /**
     * 刪除好友
     *
     * @param f
     * @param listener
     */
    public static void deleteFriend(Friend f, UpdateListener listener) {
        Friend friend = new Friend();
        friend.delete(f.getObjectId(), listener);
    }

    /**
     * 显示通知
     *
     * @param mContext 上下文
     * @param cls      跳转
     * @param mBitmap  头像
     * @param title
     * @param msg
     * @param content
     */
    public static void showNotificaition(Context mContext, Class<?> cls, Bitmap mBitmap, String title, String msg, String content) {
        Intent pendingIntent = new Intent(mContext, cls);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        BmobNotificationManager.getInstance(mContext).showNotification(mBitmap, title, msg, content, pendingIntent);
    }

    /**
     * 修改密码
     * @param oldPw
     * @param newPw
     * @param listener
     */
    public static void changePw(String oldPw, String newPw, UpdateListener listener) {
        BmobUser.updateCurrentUserPassword(oldPw, newPw, listener);
    }

    /**
     * 退出登录
     */
    public static void exitUser() {
        BmobUser.logOut();
    }
}

