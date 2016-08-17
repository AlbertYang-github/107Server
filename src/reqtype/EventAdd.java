package reqtype;

import bean.EventBean;
import bean.EventId;
import com.google.gson.Gson;
import constants.Constants;
import dao.EventDao;
import utils.StreamUtils;
import utils.StringUtils;

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
        //向客户端返回的数据
        String dataReturn = null;

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
        Long startTime = eventBean.getStartTime();

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

        if ("1".equals(result)) {
            //执行成功，想客户端返回Json数据

            EventBean eventBean = new EventBean();
            eventBean.setId(id);
            eventBean.setStartLocation(startLocation);
            eventBean.setEndLocation(endLocation);
            eventBean.setStartLongitude(startLongitude);
            eventBean.setEndLongitude(endLongitude);
            eventBean.setStartLatitude(startLatitude);
            eventBean.setEndLatitude(endLatitude);
            eventBean.setEventLabels(eventLabels);
            eventBean.setEventTitle(eventTitle);
            eventBean.setEventDesc(eventDesc);
            eventBean.setVoice(voice);
            eventBean.setPicture(picture);
            eventBean.setVideo(video);
            eventBean.setVoicePath(voicePath);
            eventBean.setPicPath(picPath);
            eventBean.setVideo(videoPath);
            eventBean.setStartTime(startTime);

            //生成Json串
            dataReturn = gson.toJson(eventBean);

        } else {
            //执行失败，返回"error"
            dataReturn = "error";
        }

        //向客户端返回执行结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, dataReturn);
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

        String[] picArr = StringUtils.getArrayFromString(picture);
        if (picArr != null) {
            picList = new ArrayList<>();
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
