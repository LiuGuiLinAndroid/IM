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

    /**
     * 消息类型
     * @param type
     */
    public static void post(int type) {
        EventBus.getDefault().post(new MessageEvent(type));
    }
}
