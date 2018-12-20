package com.liuguilin.im.event;

import org.greenrobot.eventbus.EventBus;

/**
 * FileName: EventManager
 * Founder: LiuGuiLin
 * Create Date: 2018/12/13 15:06
 * Email: lgl@szokl.com.cn
 * Profile: 消息管理
 */
public class EventManager {

    //新朋友
    public static final int EVENT_TYPE_NEW_FIREND = 0;
    //新朋友消息已读
    public static final int EVENT_TYPE_NEW_FIREND_UN = 1;
    //刷新好友列表
    public static final int EVENT_TYPE_FRIEND_LIST = 2;
    //接收到消息事件
    public static final int EVENT_TYPE_MSG_EVENT = 3;
    //刷新主页未读
    public static final int EVENT_TYPE_MAIN_SIZE = 4;
    //退出主页
    public static final int EVENT_TYPE_MAIN_EXIT = 5;
    //退出设置
    public static final int EVENT_TYPE_SETTING_EXIT = 6;

    //刷新語言
    public static final int EVENT_TYPE_UPDATE_LANGUAUE = 7;

    /**
     * 消息类型
     * @param type
     */
    public static void post(int type) {
        EventBus.getDefault().post(new MessageEvent(type));
    }

    /**
     * 消息事件
     * @param type
     * @param event
     */
    public static void postMessageEvent(int type, cn.bmob.newim.event.MessageEvent event){
        MessageEvent messageEvent = new MessageEvent(type);
        messageEvent.setMessageEvent(event);
        EventBus.getDefault().post(messageEvent);
    }
}
