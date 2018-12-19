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

    private int type;
    private String nickName;
    private String letter;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
