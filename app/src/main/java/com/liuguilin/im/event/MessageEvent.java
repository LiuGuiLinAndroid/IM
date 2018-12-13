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

    public int getType() {
        return type;
    }

    public MessageEvent(int type) {
        this.type = type;
    }
}
