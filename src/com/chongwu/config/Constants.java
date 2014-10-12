package com.chongwu.config;

public class Constants {
	// 登陆返回的信息
	public static final String USERID = "userId";

	/** SDK版本号 */
	public static final String SDK_VERSION = "top-sdk-android-2011-10-20";

	/** TOP JSON 应格式 */
	public static final String FORMAT_JSON = "json";
	/** TOP XML 应格式 */
	public static final String FORMAT_XML = "xml";

	public static final String MURL = "http://api.m.taobao.com/rest/api2.do"; // TOP认证过程使用的URL
	public static final String APIURL = "http://gw.api.taobao.com/router/rest"; // API调用的过程中使用的URL
	public static final String TTID = "400000_21670038@muki_android_1.0.0";
	/*
	 * 下面这两个值瞎编就行，哈哈
	 */
	public static String IMEI = "89014103211118510720";
	public static String IMSI = "310260000000000";

	public static String v = "*";

	// 获取登陆成功后的URL和KEY
	public static String LOGINURL; // 可以跳转到授权页面的登陆URL
	public static String LOGINKEY;

	// 获取用户会话信息
	public static String TOPSESSION;
	public static String SID;
	public static String ECODE;
	public static String TOKEN;
	public static String NICK;

	public static class BroadCastName {
		/**
		 * 获取到经纬度信息的广播
		 */
		public static String GET_LOCATION_INFO = "get_location_info";
	}

	/** 服务种类 **/
	public static class ServerKind {
		/** 不限 **/
		public static String BUXIAN = "不限";
		/** 美容 **/
		public static String MEIRONG = "美容";
		/** 寄养 **/
		public static String JIYANG = "寄养";
		/** 商店 **/
		public static String SHANGDIAN = "商店";
		/** 医院 **/
		public static String YIYUAN = "医院";
		/** 训练 **/
		public static String XULIAN = "训练";
		/** 娱乐 **/
		public static String YULE = "娱乐";
		/** 托运 **/
		public static String TUOYUN = "托运";
		/** 摄影 **/
		public static String SHEYING = "摄影";
		/** 酒店 **/
		public static String JIUDIAN = "酒店";
		/** 狗证 **/
		public static String GOUZHENG = "狗证";
		/** 殡葬 **/
		public static String BINGZANG = "殡葬";
	}

}
