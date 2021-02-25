package priv.liheyu.chat.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import priv.liheyu.chat.ChatServer;
import priv.liheyu.chat.entity.ChatStatus;
import priv.liheyu.chat.entity.TransferInfo;
import priv.liheyu.chat.io.IOStream;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xunmi
 * @Title: ServerUI
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/7/1 15:57
 */
public class ServerUI extends Stage {

    Label numText = new Label();

    Label nameText = new Label();

    Label ipText = new Label();

    Label portText = new Label();

    public TextArea info = new TextArea();

    //用户列表
    VBox itemList = new VBox();

    TextArea sendText = new TextArea();

    boolean isStart = false;

    boolean isStartPass = false;

    Thread ServerThread;

    ChatServer chatServer;

    static ServerUI instance = new ServerUI();

    int len = 3000;

    public static ServerUI getInstance() {
        return instance;
    }

    public ServerUI() {
//        第一页
        Label num = new Label();
        num.setText("在线人数:");
        num.setFont(Font.font(18));

        Label name = new Label();
        name.setText("服务器名称:");
        name.setFont(Font.font(18));

        Label ip = new Label();
        ip.setText("服务器ip:");
        ip.setFont(Font.font(18));

        Label port = new Label();
        port.setText("服务器端口:");
        port.setFont(Font.font(18));


        numText.setFont(Font.font(16));
        nameText.setFont(Font.font(16));
        ipText.setFont(Font.font(16));
        portText.setFont(Font.font(16));


        VBox left = new VBox();
        left.setStyle("-fx-background-color: #87CEFF");
        left.setSpacing(8);
        left.setPadding(new Insets(2));
        left.getChildren().addAll(num, numText, name, nameText, ip, ipText, port, portText);
        left.setPadding(new Insets(80, 20, 50, 20));
        left.setPrefWidth(200);
        left.setMinWidth(200);
        left.setPrefHeight(450);

        info.setMaxWidth(410);
        info.setPrefHeight(260);
        info.setFont(Font.font(15));
        info.setWrapText(true);

        Label title = new Label();
        title.setText("服务器日志");
        title.setFont(Font.font(20));

        Button start = new Button();
        start.setText("开启");
        start.setStyle("-fx-background-color: #4bb2ff;" +
                "-fx-pref-width: 65; -fx-pref-height: 30; -fx-font-size: 15; ");
        start.setOnAction(new StartServer());

        Button end = new Button();
        end.setText("关闭");
        end.setStyle("-fx-background-color: #4bb2ff;" +
                "-fx-pref-width: 65; -fx-pref-height: 30; -fx-font-size: 15; ");
        end.setOnAction(new EndServer());

        HBox bottom = new HBox();
        bottom.setSpacing(130);
        bottom.setPadding(new Insets(0, 0, 0, 70));
        bottom.getChildren().addAll(end, start);


        VBox right = new VBox();
        right.setPadding(new Insets(20, 0, 0, 35));
        right.setSpacing(10);
        right.getChildren().addAll(title, info, bottom);


        HBox hbox = new HBox();
        hbox.getChildren().addAll(left, right);

        AnchorPane pane1 = new AnchorPane();
        pane1.getChildren().add(hbox);
        pane1.setMaxWidth(695);

//        第二页
        itemList.setPrefWidth(230);
        itemList.setMaxWidth(230);
        itemList.setSpacing(10);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(280);
        scrollPane.setContent(itemList);
        scrollPane.setPadding(new Insets(8, 0, 10, 25));


        HBox pane2 = new HBox();
        pane2.setPadding(new Insets(30));
        pane2.setStyle("-fx-background-color: #C1FFC1");

        VBox left2 = new VBox();
        left2.setSpacing(10);
        Label title2 = new Label("用户列表");
        title2.setFont(Font.font("SimHei", FontWeight.BLACK, 23));
        title2.setPadding(new Insets(0, 0, 0, 60));
        left2.getChildren().addAll(title2, scrollPane);


        Button sendBt = new Button("发送系统消息");
        sendBt.setStyle("-fx-background-color: #4bb2ff;" +
                "-fx-pref-width: 120;-fx-pref-height: 30; -fx-font-size: 15; ");
        sendBt.setOnAction(new SendInfo());


        sendText.setMaxWidth(330);
        sendText.setFont(Font.font(18));

        Label title3 = new Label("系统消息发送框");
        title3.setFont(Font.font("SimHei", FontWeight.BLACK, 23));
        title3.setPadding(new Insets(0, 0, 0, 70));

        VBox right2 = new VBox();
        right2.setPadding(new Insets(20, 0, 20, 40));
        right2.getChildren().addAll(title3, sendText, new StackPane(sendBt));
        right2.setSpacing(10);

        pane2.getChildren().addAll(left2, right2);

        Tab tab1 = new Tab();
        tab1.setContent(pane1);
        tab1.setText("服务器信息");

        Tab tab2 = new Tab();
        tab2.setContent(pane2);
        tab2.setText("在线列表");

        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tp.getTabs().addAll(tab1, tab2);

        this.setScene(new Scene(tp));
        this.setWidth(700);
        this.setHeight(460);
        this.setResizable(false);

//        info.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                info.setScrollTop(len += 30);
//                System.out.println(info.getScrollTop());
//            }
//        });

    }

    public void setItemList(String[] item) {
        itemList.getChildren().clear();
        for (String i : item) {
            Label tmp = new Label(i);
            tmp.setFont(Font.font(18));
            HBox row = new HBox();
            row.setSpacing(30);
            MyButton bt = new MyButton("下线");
            bt.setStyle("-fx-background-color: #4bb2ff;" +
                    "-fx-pref-width: 60;-fx-pref-height: 25; -fx-font-size: 13; ");
            bt.setOnAction(new Logout());
            bt.setData(i);
            row.getChildren().addAll(tmp, bt);
            itemList.getChildren().add(row);
        }
    }

    private class Logout implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            MyButton bt = (MyButton) event.getSource();
            Socket target = ChatServer.getUserSocketMap().get(bt.getData());

            TransferInfo tfi = new TransferInfo();
            tfi.setStatusEnum(ChatStatus.QUIT);
            tfi.setUserName(bt.getData());
            tfi.setSender(bt.getData());
//            ChatServer.getUserThreadMap().get(target.toString()).loginOut(tfi);
            System.out.println("发送下线消息");
            IOStream.writeMessage(target, tfi);
        }
    }

    private class SendInfo implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String info = sendText.getText();
            if (!info.equals("") && chatServer != null) {
                TransferInfo tfi = new TransferInfo();
                tfi.setNotice(info);
                sendText.setText("");
                tfi.setSender("Administrators");
                tfi.setReceiver("All");
                tfi.setStatusEnum(ChatStatus.NOTICE);
                chatServer.sendAll(tfi);
            }
        }
    }

    private class StartServer implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (isStartPass) {
                addInfo("服务器只能启动一次！请重启程序！");
            } else {
                if (isStart) {
                    addInfo("服务器已经启动！请勿重复开启！");
                } else {
                    chatServer = new ChatServer();
                    ServerThread = new Thread(chatServer);
                    ServerThread.start();
                    isStart = !isStart;
                    isStartPass = !isStartPass;
                }
            }


        }
    }


    private class EndServer implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (isStart) {
                ServerThread.interrupt();
                addInfo("服务器已关闭！");
                isStart = !isStart;
            } else {
                addInfo("服务器已关闭！请勿重复关闭！");
            }
        }
    }

    public void setNumText(String numText) {
        this.numText.setText(numText);
    }

    public void setNameText(String nameText) {
        this.nameText.setText(nameText);
    }

    public void setIpText(String ipText) {
        this.ipText.setText(ipText);
    }

    public void setPortText(String portText) {
        this.portText.setText(portText);
    }

    public void addInfo(String info) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        String tmp = this.info.getText();
        this.info.setText(tmp + dateStr + "\n" + info + "\n");
        this.info.setScrollTop(len += 40);
    }
}

