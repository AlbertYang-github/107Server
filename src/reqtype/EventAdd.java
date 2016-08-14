package reqtype;

import bean.EventBean;
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

    //每个语音文件的路径
    private ArrayList<String> voiceList;
    //每个图片文件的路径
    private ArrayList<String> picList;
    //每个视频文件的路径
    private ArrayList<String> videoList;

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
        EventBean eventBean = gson.fromJson(body, EventBean.class);
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
            //添加成功
            result = "1";
            System.out.println("添加成功");

            //设置二进制文件路径
            if (setBinaryPath(voice, picture, video, id) != 0) {
                //将客户端上传的二进制文件存放在本地
                addbinary(socket, id);
            } else {
                result = "0";
            }

        } else {
            //添加失败
            result = "0";
            System.out.println("添加失败");
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
     * 将客户端上传的二进制文件存放在本地
     *
     * @param socket
     */
    public void addbinary(Socket socket, int id) {

        //以返回的id在本地创建目录 (该目录中存放该事件的媒体文件)
        File file = new File(Constants.PERPATH + id);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 设置二进制文件路径
     *
     * @param voice
     * @param picture
     * @param video
     */
    private int setBinaryPath(String voice, String picture, String video, int id) {

        String[] voiceArr = StringUtils.getArrayFromString(voice);
        String[] picArr = StringUtils.getArrayFromString(picture);
        String[] videoArr = StringUtils.getArrayFromString(video);

        voiceList = new ArrayList<>();
        picList = new ArrayList<>();
        videoList = new ArrayList<>();

        for (int i = 0; i < voiceArr.length; i++) {
            voiceList.add(Constants.PERPATH + id + "/" + voiceArr[i]);
        }

        for (int i = 0; i < picArr.length; i++) {
            picList.add(Constants.PERPATH + id + "/" + picArr[i]);
        }

        for (int i = 0; i < videoArr.length; i++) {
            videoList.add(Constants.PERPATH + id + "/" + videoArr[i]);
        }

        String voicePath = StringUtils.getStringFromArrayList(voiceList);
        String picPath = StringUtils.getStringFromArrayList(picList);
        String videoPath = StringUtils.getStringFromArrayList(videoList);

        //将路径添加到数据库中
        int rows = 0;
        try {
            rows = new EventDao().addBinaryPath(voicePath, picPath, videoPath, id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
}
