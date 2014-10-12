package com.chongwu.utils.common;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SIMUtil {

	/**sim卡是否可读
	 * @param context
	 * @return
	 */
	public static boolean isCanUseSim(Context context) {
		try {
			TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取sim卡的运营商名字
	 * @param context
	 * @return
	 */
	public static String getProvidersName(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String IMSI = telephonyManager.getSubscriberId();
		String ProvidersName = "";
		if (null == IMSI || "".equals(IMSI)) {
			android.widget.Toast.makeText(context, "请检查您的SIM卡是否可用", Toast.LENGTH_SHORT).show();
		} else {
			try {
				if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
					ProvidersName = "中国移动";
				} else if (IMSI.startsWith("46001")) {
					ProvidersName = "中国联通";
				} else if (IMSI.startsWith("46003")) {
					ProvidersName = "中国电信";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ProvidersName;
	}
	
}
