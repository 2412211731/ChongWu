package com.chongwu.utils.common.date_time;

import java.util.Calendar;

public class DateUtil {

	// 将当前时间归一化为天
	public static long day(long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	// 将当前时间归一化为本周星期一
	public static long week(long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		int d = c.get(Calendar.DAY_OF_WEEK);
		d = d == 1 ? 7 : d - 1;
		c.add(Calendar.DATE, -d + 1);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	// 将当前时间归一化为本月第一天
	public static long month(long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

}
