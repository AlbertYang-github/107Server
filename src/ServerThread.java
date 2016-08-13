import com.google.gson.Gson;
import reqtype.Login;
import reqtype.Register;
import utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/11.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private Gson gson;

    public ServerThread(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                //获取客户端发送的数据 (Json格式的字符串)
                InputStream in = socket.getInputStream();
                String data = StreamUtils.readString(in);

                String head = data.substring(0, 3);
                String body = data.substring(3);

                System.out.println("head=" + head);
                System.out.println("body=" + body);

                //判断任务类型
                switch (head) {
                    //注册
                    case Constants.REGISTER:
                        System.out.println("执行注册任务");
                        new Register(socket).register(body);
                        break;

                    //登录
                    case Constants.LOGIN:
                        System.out.println("执行登录任务");
                        new Login(socket).login(body);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
