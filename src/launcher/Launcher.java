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

        /**
         * 创建线程在20000端口监听 (此端口用于传输Json格式的字符串)
         */
        new Thread() {
            @Override
            public void run() {
                ServerSocket serverSocketPortJson = null;
                try {
                    serverSocketPortJson = new ServerSocket(Constants.PORT_JSON);
                    System.out.println("---------------- 端口PORT_JSON(20000)启动监听 ---------------");
                    while (true) {
                        //等待客户端连接
                        Socket socket = serverSocketPortJson.accept();
                        System.out.println("########### 连接成功 端口PORT_JSON(20000) ###########");
                        //创建业务线程
                        new Thread(new ServerThreadJson(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        /**
         * 创建线程在20001端口监听 (此端口用于传输Java对象)
         */
        new Thread() {
            @Override
            public void run() {
                ServerSocket serverSocketPortObj = null;
                try {
                    serverSocketPortObj = new ServerSocket(Constants.PORT_OBJ);
                    System.out.println("---------------- 端口PORT_OBJ(20001)启动监听 ---------------");
                    while (true) {
                        //等待客户端连接
                        Socket socket = serverSocketPortObj.accept();
                        System.out.println("########### 连接成功 PORT_OBJ(20001) ###########");
                        //创建业务线程
                        new Thread(new ServerThreadObj(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
