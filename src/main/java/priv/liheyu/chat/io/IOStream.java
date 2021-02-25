package priv.liheyu.chat.io;

import java.io.*;
import java.net.Socket;

/**
 * @author xunmi
 * @Title: IOStream
 * @ProjectName javaback
 * @Description: TODO
 * @date 2019/6/30 15:02
 */
public class IOStream {
    /**
     * 从Socket中读取对象
     */
    public static Object readMessage(Socket socket) {
        Object obj = null;
        try {
            //我们需要判断，读取出来的数据是什么类型的，对象
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            //对象数据所占的字节数
            byte[] objByte = new byte[dis.readInt()];
            dis.readFully(objByte);
            obj = ByteObjectConvert.byteToObject(objByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 根据Socket管道写出消息
     */
    public static void writeMessage(Socket socket , Object obj) {
        try {
            byte[] objByte = ByteObjectConvert.objectToByte(obj);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            //写出一个对象大小 对象
            dos.writeInt(objByte.length);
            dos.write(objByte);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

