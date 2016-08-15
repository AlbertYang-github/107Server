package launcher;

import constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 启动类
 * <p>
 * 每当有客户端连接时，都会创建一个线程为其单独服务
 * <p>
 * Created by Yohann on 2016/8/11.
 */
public class Launcher {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            System.out.println("---------------- 程序已运行 ---------------");
            while (true) {
                //等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("### 连接成功 ###");
                //创建业务线程
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
