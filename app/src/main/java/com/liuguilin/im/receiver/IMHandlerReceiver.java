package com.liuguilin.im.receiver;

import com.liuguilin.im.db.DBHelper;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.im.AddFriendMessage;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.NewFriend;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import org.litepal.crud.LitePalSupport;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.exception.BmobException;

/**
 * FileName: IMHandlerReceiver
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 16:58
 * Email: lgl@szokl.com.cn
 * Profile: 消息接收器
 */
public class IMHandlerReceiver extends BmobIMMessageHandler {

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
        IMLog.i("processSDKMessage");
    }

    /**
     * 处理自定义消息类型
     *
     * @param msg
     * @param fromUserInfo
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo fromUserInfo) {
        IMLog.i("processCustomMessage");
        //消息类型
        String type = msg.getMsgType();
        switch (type) {
            case AddFriendMessage.ADD:
                NewFriend friend = AddFriendMessage.convert(msg);
                DBHelper.save(friend);
                //通知刷新
                EventManager.post(EventManager.EVENT_TYPE_NEW_FIREND);
                break;
        }


    }


}
