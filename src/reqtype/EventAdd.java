package reqtype;

import bean.EventBean;
import bean.EventId;
import com.google.gson.Gson;
import constants.Constants;
import dao.EventDao;
import utils.StreamUtils;
import utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * 添加107端上传的事件
 * <p>
 * Created by Yohann on 2016/8/13.
 */
public class EventAdd {
    private Socket socket;
    private EventBean eventBean;
    private Gson gson;

    //每个图片文件的路径
    private ArrayList<String> picList;
    private String voicePath;
    private String picPath;
    private String videoPath;

    public EventAdd(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    /**
     * 添加事件
     *
     * @param body
     */
    public void addEvent(String body) throws SQLException, ClassNotFoundException, IOException {

        //添加结果，"1"代表成功，"0"代表失败
        String result = null;

        //获取事件的各个信息
        eventBean = gson.fromJson(body, EventBean.class);
        String startLocation = eventBean.getStartLocation();
        String endLocation = eventBean.getEndLocation();
        Double startLongitude = eventBean.getStartLongitude();
        Double endLongitude = eventBean.getEndLongitude();
        Double startLatitude = eventBean.getStartLatitude();
        Double endLatitude = eventBean.getEndLatitude();
        String eventLabels = eventBean.getEventLabels();
        String eventTitle = eventBean.getEventTitle();
        String eventDesc = eventBean.getEventDesc();
        String voice = eventBean.getVoice();
        String picture = eventBean.getPicture();
        String video = eventBean.getVideo();
        Timestamp startTime = eventBean.getStartTime();

        //添加到数据库
        int id = new EventDao().insertEvent(
                startLocation,
                endLocation,
                startLongitude,
                endLongitude,
                startLatitude,
                endLatitude,
                eventLabels,
                eventTitle,
                eventDesc,
                startTime);

        if (id != 0) {
            //设置二进制文件路径，然后添加到数据库
            if (setBinaryPath(voice, picture, video, id) != 0) {
                result = "1";
            } else {
                result = "0";
            }
        } else {
            //添加失败
            result = "0";
        }

        //向客户端返回执行结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, result);
        socket.shutdownOutput();

        //关闭流和socket
        out.close();
        StreamUtils.close();
        socket.close();
    }

    /**
     * 设置二进制文件路径
     *
     * @param voice
     * @param picture
     * @param video
     */
    private int setBinaryPath(String voice, String picture, String video, int id) {
        picList = new ArrayList<>();

        String[] picArr = StringUtils.getArrayFromString(picture);
        if (picArr != null) {
            for (int i = 0; i < picArr.length; i++) {
                picList.add(Constants.EVENTS_PATH + id + "/" + picArr[i]);
            }
        }

        if (voice != null) {
            voicePath = Constants.EVENTS_PATH + id + "/" + voice;
        }
        if (video != null) {
            videoPath = Constants.EVENTS_PATH + id + "/" + video;
        }
        picPath = StringUtils.getStringFromArrayList(picList);

        //将路径添加到数据库中
        int rows = 0;
        try {
            rows = new EventDao().addBinaryPath(voicePath, picPath, videoPath, id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //将id和路径保存到EventId
        EventId.id = id;
        EventId.voicePath = voicePath;
        EventId.picPath = picPath;
        EventId.videoPath = videoPath;

        return rows;
    }
}
