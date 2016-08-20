package reqtype;

import bean.EventBean;
import com.google.gson.Gson;

import java.net.Socket;

/**
 * 客户端请求事件文本信息
 * Created by Yohann on 2016/8/19.
 */
public class EventReqText {
    private Socket socket;
    private EventBean eventBean;
    private Gson gson;

    public EventReqText(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    public void HandleReq(String data) {
        EventBean eventBean = gson.fromJson(data, EventBean.class);
        Integer id = eventBean.getId();

    }
}
