package com.liuguilin.im.bean;

/**
 * FileName: ImChatBean
 * Founder: LiuGuiLin
 * Create Date: 2018/12/17 13:21
 * Email: lgl@szokl.com.cn
 * Profile:聊天列表
 */
public class ImChatBean {

    //时间
    public static final int MSG_TIME = 0;
    //提示文本
    public static final int MSG_TIPS = 1;
    //消息文本
    public static final int MSG_LEFT_TEXT = 2;
    public static final int MSG_RIGHT_TEXT = 3;
    //图片
    public static final int MSG_LEFT_IMG = 4;
    public static final int MSG_RIGHT_IMG = 5;
    //位置
    public static final int MSG_LEFT_LOCATION = 6;
    public static final int MSG_RIGHT_LOCATION = 7;
    //语音
    public static final int MSG_LEFT_VOICE = 8;
    public static final int MSG_RIGHT_VOICE = 9;
    //视频
    public static final int MSG_LEFT_VIDEO = 10;
    public static final int MSG_RIGHT_VIDEO = 11;
    //文件
    public static final int MSG_LEFT_FILE = 12;
    public static final int MSG_RIGHT_FILE = 13;

    //对话类型
    private int type;
    //时间
    private String time;
    //提示文本
    private String tips;
    //文字消息
    private String msgText;
    //图片消息
    private String msgImg;
    //位置消息
    private String msgLocation;
    //语音消息
    private String msgVoice;
    //视频消息
    private String msgVideo;
    //文件消息
    private String msgFile;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgImg() {
        return msgImg;
    }

    public void setMsgImg(String msgImg) {
        this.msgImg = msgImg;
    }

    public String getMsgLocation() {
        return msgLocation;
    }

    public void setMsgLocation(String msgLocation) {
        this.msgLocation = msgLocation;
    }

    public String getMsgVoice() {
        return msgVoice;
    }

    public void setMsgVoice(String msgVoice) {
        this.msgVoice = msgVoice;
    }

    public String getMsgVideo() {
        return msgVideo;
    }

    public void setMsgVideo(String msgVideo) {
        this.msgVideo = msgVideo;
    }

    public String getMsgFile() {
        return msgFile;
    }

    public void setMsgFile(String msgFile) {
        this.msgFile = msgFile;
    }

    @Override
    public String toString() {
        return "ImChatBean{" +
                "type=" + type +
                ", time='" + time + '\'' +
                ", tips='" + tips + '\'' +
                ", msgText='" + msgText + '\'' +
                ", msgImg='" + msgImg + '\'' +
                ", msgLocation='" + msgLocation + '\'' +
                ", msgVoice='" + msgVoice + '\'' +
                ", msgVideo='" + msgVideo + '\'' +
                ", msgFile='" + msgFile + '\'' +
                '}';
    }
}
