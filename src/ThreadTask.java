import bean.UserRegData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import loginReg.UserReg;
import utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * Created by Yohann on 2016/8/11.
 */
public class ThreadTask implements Runnable {
    private Socket socket;
    private Gson gson;

    public ThreadTask(Socket socket) {
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

                //提取taskId的值
                UserRegData userRegData = gson.fromJson(data, UserRegData.class);
                int taskId = userRegData.getTaskId();

                //判断任务类型
                switch (taskId) {
                    /**
                     * 注册
                     */
                    case Constants.REGISTER:
                        System.out.println("执行注册任务");

                        //用户名
                        String username = userRegData.getUsername();
                        //密码
                        String password = userRegData.getPassword();
                        //注册
                        boolean result = new UserReg().register(username, password);
                        JsonObject jObject = new JsonObject();
                        jObject.addProperty("result", result);
                        String json = gson.toJson(jObject);

                        System.out.println("result = " + result);

//                        //向客户端返回注册结果
//                        OutputStream out = socket.getOutputStream();
//                        StreamUtils.writeString(out, json);

                        break;

                    /**
                     * 登录
                     */
                    case Constants.LOGIN:
                        System.out.println("执行登录任务");
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
