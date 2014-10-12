package com.chongwu.config;

import com.chongwu.bean.User;

public class Config {
	
    public static final String app_key = "21682376"; // TOP分配给应用的AppKey
    public static final String app_secret = "b0d0017bdfd69050c0acaac80c3fd660"; // TOP分配给应用的secret
    
    /**用户给应用配置的回调地址，该参数和淘宝平台上创建应用时候配置的回调地址一样**/
    public static final String app_callback_url = "com.taobao.top.android.sample://";
    
    
    private String method = ""; // API接口名称
    private String session = ""; // TOP分配给用户的SessionKey
    private String timestamp = ""; // 时间戳(格式为yyyy-MM-dd HH:mm:ss)
    private String format = "";
    private String v = "2.0"; // API协议版本，可选值:2.0。
    private String sign = ""; // API输入参数签名结果
    private String sign_method = "md5"; // 参数的加密方法选择，可选值是：md5,hmac。这个参数只存在于2.0中。    
    private User user = new User(); // 个人信息
    private String status;
    
    //lhy家:百度地图key
    public static final String BAIDU_MAP_KEY = "pFSC4iitcqWrads2yAeU32gs"; 
    //lhy:公司
//    public static final String BAIDU_MAP_KEY = "wz9DW0Hm4h65wq1kFRAytv35";

}
