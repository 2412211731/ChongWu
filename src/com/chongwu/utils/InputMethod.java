package com.chongwu.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethod {

	/**
	 * 显示数字键盘
	 */
	public static void showInputMethod(final Context context, int time) {
		/**
		 * 用一个定时器控制当打开这个Activity的时候就出现软键盘
		 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, time);
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideInputMethod(Context context, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void hideInputMethod(View v,Activity activity){
	// try{
	// InputMethodManager imm = (InputMethodManager)
	// activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	// if(imm.isActive()){
	// boolean hide = imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	// if(!hide){
	// activity.onBackPressed();
	// }
	// }else{
	// activity.onBackPressed();
	// }
	// }catch (Exception e) {
	// }
	// }

}