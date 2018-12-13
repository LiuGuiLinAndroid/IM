package com.liuguilin.im.im;

import android.text.TextUtils;

import com.liuguilin.im.utils.IMLog;

import org.json.JSONObject;

import java.util.logging.Logger;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * FileName: AddFriendMessage
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 14:06
 * Email: lgl@szokl.com.cn
 * Profile: 添加好友消息
 */
public class AddFriendMessage extends BmobIMExtraMessage {

    @Override
    public String getMsgType() {
        return "add";
    }

    /**
     * 转换
     *
     * @param msg
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg) {
        NewFriend add = new NewFriend();
        String content = msg.getContent();
        add.setMsg(content);
        add.setTime(msg.getCreateTime());
        add.setStatus(0);
        try {
            String extra = msg.getExtra();
            if (!TextUtils.isEmpty(extra)) {
                JSONObject json = new JSONObject(extra);
                String name = json.getString("name");
                add.setName(name);
                String avatar = json.getString("avatar");
                add.setAvatar(avatar);
                add.setUid(json.getString("uid"));
            } else {
                IMLog.e("extra null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }

    @Override
    public boolean isTransient() {
        return true;
    }
}
