package priv.liheyu.chat;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import priv.liheyu.chat.ui.ServerUI;


/**
 * @author xunmi
 * @Title: StartServer
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/7/1 23:52
 */
public class StartServer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerUI serverUI = ServerUI.getInstance();
        serverUI.setOnCloseRequest((WindowEvent event) -> {
            serverUI.close();
            System.exit(1);
        });
        serverUI.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
