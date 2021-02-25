package priv.liheyu.chat.entity;

import java.io.Serializable;

/**
 * @author xunmi
 * @Title: TransferInfo
 * @ProjectName back
 * @Description: TODO
 * @date 2019/6/30 15:04
 */
public class TransferInfo implements Serializable {
    private static final long serialVersionUID = 6543722756249559791L;

    private String userName;
    private String password;

    //聊天消息内容
    private String content;

    //系统消息
    private String notice;

    //登录成功标志
    private Boolean loginSuccessFlag = false;

    //消息类型枚举
    private ChatStatus statusEnum;

    //在线的用户列表
    private String[] userOnlineArray;

    //发送人
    private String sender;

    //接收人
    private String receiver;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Boolean getLoginSuccessFlag() {
        return loginSuccessFlag;
    }

    public void setLoginSuccessFlag(Boolean loginSuccessFlag) {
        this.loginSuccessFlag = loginSuccessFlag;
    }

    public ChatStatus getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(ChatStatus statusEnum) {
        this.statusEnum = statusEnum;
    }

    public String[] getUserOnlineArray() {
        return userOnlineArray;
    }

    public void setUserOnlineArray(String[] userOnlineArray) {
        this.userOnlineArray = userOnlineArray;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}
