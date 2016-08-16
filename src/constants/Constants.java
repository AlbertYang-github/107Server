package constants;

/**
 * Created by Yohann on 2016/8/11.
 */
public class Constants {

    /**
     * 主机端口号(传输Json数据)
     */
    public static final int PORT_JSON = 20000;

    /**
     * 主机端口号(传输Java对象)
     */
    public static final int PORT_OBJ = 20001;

    /**
     * 每个事件的路径
     */
    public static final String EVENTS_PATH = "D:/107Server/107Files/Events/";

    /**
     * 用户个人目录
     */
    public static final String USERS_PATH = "D:/107Server/107Files/Users/";

    /**
     * 注册
     */
    public static final String REGISTER = "001";

    /**
     * 登录
     */
    public static final String LOGIN = "002";

    /**
     * 添加标记
     */
    public static final String ADD_EVENT = "003";

    /**
     * 获取标记
     */
    public static final String GET_EVENT = "004";

    /**
     * 上传二进制媒体文件
     */
    public static final String ADD_BIN = "005";
}
