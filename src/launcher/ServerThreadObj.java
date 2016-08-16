package launcher;

import bean.BinaryBean;
import com.google.gson.Gson;
import constants.Constants;
import reqtype.EventAddBin;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/16.
 */
public class ServerThreadObj implements Runnable {
    private Socket socket;
    private Gson gson;

    public ServerThreadObj(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                //获取客户端发送的数据 (Json格式的字符串)
                InputStream in = socket.getInputStream();
                ObjectInputStream objIn = new ObjectInputStream(in);
                BinaryBean binaryBean = (BinaryBean) objIn.readObject();

                //获取头信息
                String header = binaryBean.getHeader();

                //判断任务类型
                switch (header) {
                    //上传事件的二进制文件
                    case Constants.ADD_BIN:
                        System.out.println("上传事件的二进制文件");
                        new EventAddBin(socket, binaryBean, in, objIn).addBin();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }  //注意：这里不能立刻关闭流资源，因为直接用传统方式关闭流后socket也会被关闭
        }
    }
}
