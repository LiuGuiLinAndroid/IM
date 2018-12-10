package com.liuguilin.im.receiver;

import com.liuguilin.im.utils.IMLog;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

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
        IMLog.i("IMHandlerReceiver:" + messageEvent.toString());
    }

    /**
     * 离线消息
     *
     * @param offlineMessageEvent
     */
    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        IMLog.i("IMHandlerReceiver:" + offlineMessageEvent.toString());
    }
}
