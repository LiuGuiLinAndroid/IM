package com.liuguilin.im.im;

import cn.bmob.v3.BmobObject;

/**
 * FileName: Friend
 * Founder: LiuGuiLin
 * Create Date: 2018/12/14 10:51
 * Email: lgl@szokl.com.cn
 * Profile: 好友
 */
public class Friend extends BmobObject {

    //自己
    private IMUser imUser;

    //好友
    private IMUser imUserFriend;

    public IMUser getImUser() {
        return imUser;
    }

    public void setImUser(IMUser imUser) {
        this.imUser = imUser;
    }

    public IMUser getImUserFriend() {
        return imUserFriend;
    }

    public void setImUserFriend(IMUser imUserFriend) {
        this.imUserFriend = imUserFriend;
    }

}
