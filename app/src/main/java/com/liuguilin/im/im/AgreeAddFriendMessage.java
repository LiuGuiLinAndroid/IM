package com.liuguilin.im.im;

import android.text.TextUtils;

import com.liuguilin.im.utils.IMLog;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * FileName: AgreeAddFriendMessage
 * Founder: LiuGuiLin
 * Create Date: 2018/12/14 10:59
 * Email: lgl@szokl.com.cn
 * Profile: 同意
 */
public class AgreeAddFriendMessage extends BmobIMExtraMessage {

    public static final String AGREE = "agree";

    //最初的发送方
    private String uid;
    //时间
    private Long time;
    //用于通知栏显示的内容
    private String msg;

    public AgreeAddFriendMessage() {

    }

    public AgreeAddFriendMessage(BmobIMMessage msg) {
        super.parse(msg);
    }

    @Override
    public String getMsgType() {
        return AGREE;
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 消息转换
     *
     * @param msg
     * @return
     */
    public static AgreeAddFriendMessage convert(BmobIMMessage msg) {
        AgreeAddFriendMessage agree = new AgreeAddFriendMessage(msg);
        try {
            String extra = msg.getExtra();
            if (!TextUtils.isEmpty(extra)) {
                JSONObject json = new JSONObject(extra);
                Long time = json.getLong("time");
                String uid = json.getString("uid");
                String m = json.getString("msg");
                agree.setMsg(m);
                agree.setUid(uid);
                agree.setTime(time);
            } else {
                IMLog.i("extra null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agree;
    }
}
