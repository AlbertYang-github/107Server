import bean.UserLoginData;
import bean.UserRegData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import loginReg.UserLogin;
import loginReg.UserReg;
import utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                    /**
                     * 注册
                     */
                    case Constants.REGISTER:
                        System.out.println("执行注册任务");

                        UserRegData userRegData = gson.fromJson(body, UserRegData.class);
                        //用户名
                        String reg_username = userRegData.getUsername();
                        //密码
                        String reg_password = userRegData.getPassword();
                        //注册
                        boolean reg_result = new UserReg().register(reg_username, reg_password);
                        JsonObject reg_jObject = new JsonObject();
                        reg_jObject.addProperty("result", reg_result);
                        String reg_json = gson.toJson(reg_jObject);

                        System.out.println("result = " + reg_result);

                        //向客户端返回注册结果
                        OutputStream reg_out = socket.getOutputStream();
                        StreamUtils.writeString(reg_out, reg_json);
                        socket.shutdownOutput();

                        //关闭流和socket
                        StreamUtils.close();
                        socket.close();

                        break;

                    /**
                     * 登录
                     */
                    case Constants.LOGIN:
                        System.out.println("执行登录任务");

                        UserLoginData userLoginData = gson.fromJson(body, UserLoginData.class);
                        //用户名
                        String login_username = userLoginData.getUsername();
                        //密码
                        String login_password = userLoginData.getPassword();
                        //登录
                        boolean login_result = new UserLogin().login(login_username, login_password);
                        JsonObject login_jObject = new JsonObject();
                        login_jObject.addProperty("result", login_result);
                        String login_json = gson.toJson(login_jObject);

                        System.out.println("result = " + login_result);

                        //向客户端返回注册结果
                        OutputStream out = socket.getOutputStream();
                        StreamUtils.writeString(out, login_json);
                        socket.shutdownOutput();

                        //关闭流和socket
                        StreamUtils.close();
                        socket.close();

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
