package com.chongwu.utils.common.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {
	/**
	 * 设置闹钟
	 * @param ctx 上下文
	 * @param className 类名
	 * @param alarmId 闹钟id
	 * @param type 1表示跳转到activity  2表示跳转到service 3表示跳转到reciever
	 * @param triggerAtTime 相对于当前时间的延迟时间
	 */
	public static void set(Context ctx,String className, int alarmId,int type,long triggerAtTime,boolean setAlarm) {
		try {
			Intent intent = new Intent(ctx, Class.forName(className));
			intent.putExtra("alarmId", alarmId);
			PendingIntent pendingIntent = null;
			switch(type){
				case 1://跳转activity
					pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
					break;
				case 2://跳转service
					pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
					break;
				case 3://跳转reciever
					pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
					break;
				default:
					break;
			}
			
			AlarmManager alarmManager = ((AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE));
			if(setAlarm){
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+triggerAtTime, pendingIntent);
			}else{
				alarmManager.cancel(pendingIntent);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
