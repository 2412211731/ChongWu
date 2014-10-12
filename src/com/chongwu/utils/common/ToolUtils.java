package com.chongwu.utils.common;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ToolUtils {
	
	/**
	 * 判断应用是否在前台显示
	 * @param context 上下文
	 * @param packageName  应用的包名
	 * @return
	 */
	public static boolean isTopActivity(Context context,String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			Log.d("xyw", "---------------包名-----------"
					+ tasksInfo.get(0).topActivity.getPackageName());
			// 应用程序位于堆栈的顶
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}

		return false;
	}
}
