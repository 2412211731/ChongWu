package com.taobao.top.android;

import android.content.Context;

public abstract class JNIUtils {

	static {
		System.loadLibrary("top-sdk");
	}
	//注意：此jni方法移动到其他包会有问题，因为其包名必须
	public native static String getTrackId(Context context, String appkey,
			String appsecret);

	//注意：此jni方法移动到其他包会有问题，因为其包名必须
	public native static String getSDKVersion();

}
