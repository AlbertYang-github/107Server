package launcher;

import com.google.gson.Gson;
import constants.Constants;
import reqtype.EventAdd;
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
public class ServerThreadJson implements Runnable {
    private Socket socket;
    private Gson gson;

    public ServerThreadJson(Socket socket) {
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

                String header = data.substring(0, 3);
                String body = data.substring(3);

                System.out.println("header=" + header);
                System.out.println("body=" + body);

                //判断任务类型
                switch (header) {
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

                    //添加107端上传的事件
                    case Constants.ADD_EVENT:
                        System.out.println("添加107端上传的事件");
                        new EventAdd(socket).addEvent(body);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } //注意：这里不能立刻关闭流资源，因为直接用传统方式关闭流后socket也会被关闭
        }
    }
}
