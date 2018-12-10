package com.liuguilin.im.im;

import cn.bmob.v3.BmobUser;

/**
 * FileName: IMUser
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:39
 * Email: lgl@szokl.com.cn
 * Profile: 用户
 */
public class IMUser extends BmobUser {

    //昵称
    private String nickname;
    //年龄
    private int age;
    //性别
    private boolean sex;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
