package priv.liheyu.chat.entity;

/**
 * @author xunmi
 * @Title: ChatStatus
 * @ProjectName back
 * @Description: TODO
 * @date 2019/6/30 16:30
 */
public enum ChatStatus {
    LOGIN(1, "登录消息"),
    REGISTER(2,"注册消息"),
    NOTICE(3, "全体消息"),
    CHAT(4, "私人消息"),
    ULIST(6, "在线用户列表"),
    QUIT(7, "退出消息");
    private Integer status;
    private String desc;

    ChatStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
