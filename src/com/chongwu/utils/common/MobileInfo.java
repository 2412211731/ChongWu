package com.chongwu.utils.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 获取手机各种信息
 * @author Administrator
 *
 */
public class MobileInfo {

	private static MobileInfo instance = null;
	private Context context;

	private MobileInfo(Context context) {
		this.context = context;
	}

	static MobileInfo getInstance(Context context) {
		if (instance == null) {
			instance = new MobileInfo(context);
		}
		return instance;
	}

	public int getVerisonCode() {

		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		int version = 0;
		if (packInfo != null) {
			version = packInfo.versionCode;
		}
		return version;
	}
	
	public String getVerisonName() {
		
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = "";
		if (packInfo != null) {
			version = packInfo.versionName;
		}
		return version;
	}

	public String getImei() {
		String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return imei;
	}

	public String getSimCardTellNum() {
		String num = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		return num;
	}

}
