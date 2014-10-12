package com.chongwu.utils.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class AppLog {

	public static boolean DEBUG = true;
	public static String ET_TAG_A = "chongwu";
	public static String ET_TAG_S = "chongwu_service";
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static void LogA(String info) {
		if (DEBUG) {
			Log.i(ET_TAG_A, ">>==" + info);
		}
	}

	public static void LogA(String tag, String info) {
		if (DEBUG) {
			Log.i(tag, ">>==" + info);
		}
	}

	public static void LogS(String info) {
		if (DEBUG) {
			Log.i(ET_TAG_S, ">>==" + info);
		}
	}

	public static void LogS(String tag, String info) {
		if (DEBUG) {
			Log.d(tag, ">>==" + info);
		}
	}

	public static void r(String simpleName, String string, Throwable paramThrowable) {

		saveCrashInfo2File(simpleName, string, paramThrowable);
	}

	private static synchronized String saveCrashInfo2File(String simpleName, String string, Throwable ex) {

		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-p-a" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory() + "/com.chongwu/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(simpleName, "an error occured while writing file...", e);
		}
		return null;
	}

}
