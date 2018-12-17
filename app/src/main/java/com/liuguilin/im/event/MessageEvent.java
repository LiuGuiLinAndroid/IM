package com.liuguilin.im.event;

/**
 * FileName: MessageEvent
 * Founder: LiuGuiLin
 * Create Date: 2018/12/13 15:05
 * Email: lgl@szokl.com.cn
 * Profile: 消息传递
 */
public class MessageEvent {

    private int type;
    //消息事件
    private cn.bmob.newim.event.MessageEvent messageEvent;

    public int getType() {
        return type;
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public cn.bmob.newim.event.MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(cn.bmob.newim.event.MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }
}
