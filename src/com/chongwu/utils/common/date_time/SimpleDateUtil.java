package com.chongwu.utils.common.date_time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateUtil {
	public static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static boolean compareTime(long date, long date2) {
		return getTimeDiff(date, date2) > 0;
	}

	public static boolean compareTime(String date, String date2) {
		return getTimeDiff(date, date2) > 0;
	}

	public static boolean compareTime(Date date, Date date2) {
		return getTimeDiff(date, date2) > 0;
	}

	public static long getTimeDiff(String date, String date2) {
		try {
			long c = f.parse(date).getTime();
			long u = f.parse(date2).getTime();
			return c - u;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getTimeDiff(long date, long date2) {
		try {
			return date - date2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getTimeDiff(Date date, Date date2) {
		try {
			long c = date.getTime();
			long u = date2.getTime();
			return c - u;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
