package com.liuguilin.im.im;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * FileName: DeleteFriendMessage
 * Founder: LiuGuiLin
 * Create Date: 2018/12/18 11:04
 * Email: lgl@szokl.com.cn
 * Profile: 删除好友
 */
public class DeleteFriendMessage extends BmobIMExtraMessage {

    public static final String DELETE = "delete";

    @Override
    public String getMsgType() {
        return DELETE;
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
