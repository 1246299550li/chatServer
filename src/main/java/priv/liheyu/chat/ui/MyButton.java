package priv.liheyu.chat.ui;

import javafx.scene.control.Button;

/**
 * @author xunmi
 * @Title: MyButton
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/7/2 12:40
 */
class MyButton extends Button {
    private String data;

    MyButton(String text) {
        super(text);
    }

    String getData() {
        return data;
    }

    void setData(String data) {
        this.data = data;
    }
}