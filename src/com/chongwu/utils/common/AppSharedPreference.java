package com.chongwu.utils.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存整个应用的Share数据
 * @author Administrator
 *
 */
public class AppSharedPreference {
	public static final String APP_SHARED_PREFERENCE = "AppSharedPreferences";
	
    public static void saveString(Context context,String key,String value){
    	SharedPreferences.Editor sh = context.getSharedPreferences(APP_SHARED_PREFERENCE,Context.MODE_PRIVATE).edit();
    	sh.putString(key,value);
    	sh.commit();
    }
    
    public static void saveInt(Context context,String key,int value){
    	SharedPreferences.Editor sh = context.getSharedPreferences(APP_SHARED_PREFERENCE,Context.MODE_PRIVATE).edit();
    	sh.putInt(key,value);
    	sh.commit();
    }
    
    public static String getString(Context context,String key){
    	SharedPreferences sh = context.getSharedPreferences(APP_SHARED_PREFERENCE,Context.MODE_PRIVATE);
    	return sh.getString(key,"");
    }
    
    public static int getInt(Context context,String key){
    	SharedPreferences sh = context.getSharedPreferences(APP_SHARED_PREFERENCE,Context.MODE_PRIVATE);
    	return sh.getInt(key,0);
    }
}
