package com.example.network.http;

/**
 * 所有请求相关的地址
 */
public class HttpConstants {

//    public static final String ROOT_URL = "http://zhudonggang.com/api";
    public static final String ROOT_URL = "http://imooc.com/api";

    /**
     * 请求本地产品列表
     */
    public static String PRODUCT_LIST = ROOT_URL + "/fund/search.php";

    /**
     * 本地产品列表更新时间和请求
     */
    public static String PRODUCT_LATESAT_UPDATE = ROOT_URL + "/fund/upsearch.php";

    /**
     * 登陆接口
     */
    public static String LOGIN = ROOT_URL + "/User/login_phone.php";

    /**
     * 检查更新接口
     */
    public static String CHECK_UPDATE = ROOT_URL + "/config/check_update.php";

    /**
     * 首页产品请求接口
     */
    public static String HOME_RECOMMAND = ROOT_URL + "/product/home_recommand.php";
//    public static String HOME_RECOMMAND = ROOT_URL ;

    /**
     * 课程详情接口
     */
    public static String COURSE_DETAIL = ROOT_URL + "/product/course_detail.php";


    //登录时用的ip
    public static String LOGIN_IP = "192.168.2.10";
    //端口号 ，要求客户端与服务器端一致
    public static int LOGIN_PORT = 9124;
    public static int READBUFFER_SIZE = 10240;















}
