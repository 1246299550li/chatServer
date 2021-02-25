package priv.liheyu.chat.entity;

/**
 * @author xunmi
 * @Title: ServerInfoBean
 * @ProjectName back
 * @Description: TODO
 * @date 2019/6/30 14:18
 */
public class ServerInfoBean {

    private String hostName;

    private String ip;

    private Integer port;


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
