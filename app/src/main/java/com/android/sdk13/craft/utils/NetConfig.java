package com.android.sdk13.craft.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.Response;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class NetConfig {
    public final static String MOBILE_IP = "192.168.43.50:8080";
    public final static String NET_IP = "129.211.26.221:8080";
    public final static String ROOM_IP = "10.9.169.35:8080";
    public final static String URL = "http://" + MOBILE_IP + "/PictureServer_war_exploded";

    // 用户模块
    private final static String USER_URL = URL + "/user";
    public final static String USER_LOGIN = USER_URL + "/login";
    public final static String USER_REG = USER_URL + "/register";
    public final static String UPLOAD_USER_INFO = USER_URL + "/modify";
    public final static String GET_USER_INFO = USER_URL + "/getInfo";
    public final static String FOCUS_ADD = USER_URL + "/addFocus";
    public final static String FOCUS_CANCEL = USER_URL + "/cancelFocus";
    public final static String FOCUS_JUDGE = USER_URL + "/isFocus";
    public final static String FOCUS_LIST = USER_URL + "/getFocus";
    public final static String FOCUSED_LIST = USER_URL + "/getFocused";

    // 分享模块
    private final static String UPLOAD_URL = URL + "/upload";
    public final static String UPLOAD_COVER = UPLOAD_URL + "/cover";
    public final static String UPLOAD_PIC = UPLOAD_URL + "/picture";
    public final static String UPLOAD_VIDEO = UPLOAD_URL + "/video";
    public final static String UPLOAD_ARTICLE = UPLOAD_URL + "/article";

    // 学习模块
    private final static String STUDY_URL = URL + "/study";
    private final static String COURSE_LIST = URL + "/courseList";

    // 首页模块
    private final static String HomePage = URL + "/home";
    public final static String HOME_ANNOUNCE = URL + "/announce/getAll";

    // 展示模块
    public final static String SHOW_URL = URL + "/show";

    // 文章板块
    public final static String ARTICLE = URL + "/article";
    public final static String ARTICLE_DEPLOY = ARTICLE + "/deploy";
    public final static String ARTICLE_LIST = ARTICLE + "/getList";
    public final static String ARTICLE_USER_LIST = ARTICLE + "/getUserArticle";
    public final static String ARTICLE_DETAIL = ARTICLE + "/detail";
    public final static String ARTICLE_SEND_COMMENT = ARTICLE + "/sendComment";
    public final static String ARTICLE_GET_COMMENTS = ARTICLE + "/getComments";

    // 视频板块
    public final static String VIDEO = URL + "/video";
    public final static String VIDEO_DEPLOY = VIDEO + "/deploy";
    public final static String VIDEO_LIST = VIDEO + "/getList";
    public final static String VIDEO_DETAIL = VIDEO + "/detail";
    public final static String VIDEO_USER_LIST = VIDEO + "/getUserVideo";

    // 活动板块
    public final static String ACTIVITY = URL + "/activity";
    public final static String ACTIVITY_DEPLOY = ACTIVITY + "/deploy";
    public final static String ACTIVITY_LIST = ACTIVITY + "/getList";
    public final static String ACTIVITY_DETAIL = ACTIVITY + "/detail";
    public final static String ACTIVITY_USER_LIST = ACTIVITY + "/getUserActivity";
    public final static String ACTIVITY_JOIN = ACTIVITY + "/join";
    public final static String ACTIVITY_EXIT = ACTIVITY + "/exit";
    public final static String ACTIVITY_IS_JOIN = ACTIVITY + "/isJoin";


    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static int getCode(String json){
        JSONObject object = JSON.parseObject(json);
        int code = object.getInteger( "code" );
        return code;
    }

    public static boolean judgeCode(Response<String> response){
        JSONObject object = JSON.parseObject(response.body());
        int code = object.getInteger( "code" );
        return response.code() == 200 && code == 200;
    }

    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }
}
