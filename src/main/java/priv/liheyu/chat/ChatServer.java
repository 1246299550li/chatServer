package priv.liheyu.chat;

import javafx.application.Platform;
import priv.liheyu.chat.constants.Constants;
import priv.liheyu.chat.entity.ServerInfoBean;
import priv.liheyu.chat.entity.TransferInfo;
import priv.liheyu.chat.io.IOStream;
import priv.liheyu.chat.ui.ServerUI;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xunmi
 * @Title: ChatServer
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/6/30 13:46
 */
public class ChatServer implements Runnable {
    static Map<String, Socket> userSocketMap = new HashMap<>();
    private static Map<String, ServerHandler> userThreadMap = new HashMap<>();

    public ChatServer() {

    }

    public static Map<String, Socket> getUserSocketMap() {
        return userSocketMap;
    }

    public static Map<String, ServerHandler> getUserThreadMap() {
        return userThreadMap;
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        try {
            //建立服务器的Socket监听
            ServerSocket sso = new ServerSocket(Constants.SERVER_PORT);
            //初始化服务器参数信息
            loadServerInfo(getServerIP());
            while (true) {
                //等待连接，阻塞实现，会得到一个客户端的连接
                Socket socket = sso.accept();
                ServerHandler serverHandler = new ServerHandler(socket);
                serverHandler.start();
                userThreadMap.put(socket.toString(), serverHandler);
                Platform.runLater(() -> ServerUI.getInstance().addInfo("服务器接受到客户端的连接：" + socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化加载服务器参数
     */
    private void loadServerInfo(ServerInfoBean serverInfo) {
        Platform.runLater(() -> {
            ServerUI.getInstance().setNumText("0");
            ServerUI.getInstance().setIpText(serverInfo.getIp());
            ServerUI.getInstance().setNameText(serverInfo.getHostName());
            ServerUI.getInstance().setPortText(serverInfo.getPort().toString());
            ServerUI.getInstance().addInfo("服务器已经启动...");
        });
    }

    /**
     * 获取服务器的主机名和IP地址
     */
    private ServerInfoBean getServerIP() {
        ServerInfoBean sib = null;
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            sib = new ServerInfoBean();
            sib.setIp(serverAddress.getHostAddress());
            sib.setHostName(serverAddress.getHostName());
            sib.setPort(Constants.SERVER_PORT);
        } catch (Exception e) {
            System.out.println("Cound not get Server IP." + e);
        }
        return sib;
    }

    public void sendAll(TransferInfo tfi) {
        for (Map.Entry<String, Socket> entry : userSocketMap.entrySet()) {
            IOStream.writeMessage(entry.getValue(), tfi);
        }
        Platform.runLater(() -> ServerUI.getInstance().addInfo(tfi.getSender() + "发消息给所有人"));
    }

}
