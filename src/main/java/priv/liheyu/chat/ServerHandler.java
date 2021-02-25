package priv.liheyu.chat;

import javafx.application.Platform;
import priv.liheyu.chat.entity.ChatStatus;
import priv.liheyu.chat.entity.TransferInfo;
import priv.liheyu.chat.io.IOStream;
import priv.liheyu.chat.ui.ServerUI;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xunmi
 * @Title: ServerHandler
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/6/30 13:53
 */
public class ServerHandler extends Thread {
    private Socket socket;
    private static List<String> onlineUsers = new ArrayList<>();
    private static List<Socket> onlineSockets = new ArrayList<>();

    ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //模拟一直拿消息，产生阻塞
                Object obj = IOStream.readMessage(socket);
                if (obj instanceof TransferInfo) {
                    TransferInfo tfi = (TransferInfo) obj;
                    if (tfi.getStatusEnum() == ChatStatus.LOGIN) {
                        loginHandler(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.CHAT) {
                        chatHandler(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.NOTICE) {
                        sendAll(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.REGISTER) {
                        register(tfi);
                        Thread.sleep(1000);
                        socket.close();
                        this.interrupt();
                        break;
                    } else if (tfi.getStatusEnum() == ChatStatus.QUIT) {
                        loginOut(tfi);
                        Thread.sleep(1000);
                        socket.close();
                        this.interrupt();
                        break;
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理用户下线
     */
    private void loginOut(TransferInfo tfi) {
        String userName = tfi.getUserName();
        //将该用户从集合移除
        onlineUsers.removeIf(s -> s.equals(userName));
        onlineSockets.removeIf(next -> socket == next);
        ChatServer.userSocketMap.remove(userName);

        //刷新服务器面板的用户列表
        flushOnlineUserList();

        //给所有在线的用户发送下线消息 刷新用户列表
        tfi.setStatusEnum(ChatStatus.NOTICE);
        tfi.setSender("Administrators");
        tfi.setReceiver("All");
        tfi.setNotice(userName + "下线了");
        sendAll(tfi);
        tfi.setUserOnlineArray(onlineUsers.toArray(new String[0]));
        tfi.setStatusEnum(ChatStatus.ULIST);
        sendAll(tfi);
        log(userName + "下线了");
    }

    /**
     * 处理客户端聊天请求
     */
    private void chatHandler(TransferInfo tfi) {
        String receiver = tfi.getReceiver();
        String sender = tfi.getSender();
        //根据receiver拿到Socket管道
        //通过用户名为键，管道为值取做map
        Socket socketReceiver = ChatServer.userSocketMap.get(receiver);
        IOStream.writeMessage(socketReceiver, tfi);

        Socket socketSender = ChatServer.userSocketMap.get(sender);
        IOStream.writeMessage(socketSender, tfi);
        log(tfi.getSender() + "发消息给" + tfi.getReceiver());
    }

    /**
     * 处理客户端的登录请求
     */
    private void loginHandler(TransferInfo tfi) {
        boolean flag = checkUserLogin(tfi);
        tfi.setLoginSuccessFlag(false);
        if (flag) {
            //返回登录成功给客户端
            tfi.setLoginSuccessFlag(true);
            tfi.setStatusEnum(ChatStatus.LOGIN);
            IOStream.writeMessage(socket, tfi);
            String userName = tfi.getUserName();

            //统计在线人数
            onlineUsers.add(userName);
            onlineSockets.add(socket);
            ChatServer.userSocketMap.put(userName, socket);

            //发系统消息给客户端，该用户已上线
            tfi = new TransferInfo();
            tfi.setStatusEnum(ChatStatus.NOTICE);
            tfi.setNotice(userName + " 上线了");
            tfi.setSender("Administrators");
            tfi.setReceiver("All");
            sendAll(tfi);

            //准备最新用户列表给当前客户端
            tfi = new TransferInfo();
            String[] strArr = new String[onlineUsers.size()];
            tfi.setUserOnlineArray(onlineUsers.toArray(strArr));
            tfi.setStatusEnum(ChatStatus.ULIST);
            sendAll(tfi);

            //刷新在线用户列表
            flushOnlineUserList();
            log(userName + " 上线了");
        } else {
            //返回登录失败给客户端
            IOStream.writeMessage(socket, tfi);
            log(tfi.getUserName() + "登录失败");
        }
    }

    /**
     * 日志记录
     */
    private void log(String log) {
        Platform.runLater(() -> {
            ServerUI.getInstance().addInfo(log);
        });
    }

    /**
     * 刷新服务端用户列表
     */
    private void flushOnlineUserList() {
        String[] strList = new String[onlineUsers.size()];
        String[] userArray = onlineUsers.toArray(strList);
        Platform.runLater(() -> {
            ServerUI.getInstance().setItemList(userArray);
            ServerUI.getInstance().setNumText(userArray.length + "");
        });
    }

    /**
     * 发送消息
     */
    private void sendAll(TransferInfo tfi) {
        for (Socket tempSocket : onlineSockets) {
            IOStream.writeMessage(tempSocket, tfi);
        }
        log(tfi.getSender() + "发消息给所有人");
    }

    /**
     * 登录功能
     */
    private boolean checkUserLogin(TransferInfo tfi) {
        try {
            String userName = tfi.getUserName();
            String password = tfi.getPassword();
            URL url = Thread.currentThread().getContextClassLoader().getResource("data/user.txt");
            System.out.println(url);
            File fw = new File(Objects.requireNonNull(url).getFile());
            BufferedReader buf = new BufferedReader(new FileReader(fw));
            String row = null;
            while ((row = buf.readLine()) != null) {
                System.out.println(row);
                if ((userName + "|" + password).equals(row)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册功能
     */
    private void register(TransferInfo tfi) {
        boolean flag = checkUserLogin(tfi);
        String userName = tfi.getUserName();
        if (flag) {
            log(userName + "注册失败");
            tfi.setStatusEnum(ChatStatus.QUIT);
            IOStream.writeMessage(socket, tfi);
        } else {
            String password = tfi.getPassword();
            URL url = Thread.currentThread().getContextClassLoader().getResource("data/user.txt");
            File fw = new File(Objects.requireNonNull(url).getFile());
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(fw, true));
                String tmp = "\n" + userName + "|" + password;
                out.write(tmp);
                out.flush();
                out.close();
                log(userName + "注册成功");
                IOStream.writeMessage(socket, tfi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
