package com.chongwu.utils.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ShortCut {

	/**
	 * 创建应用快捷方式
	 * @param context
	 * @param appName 应用名字 如'百度应用'
	 * @param iconSource 图标资源id， 如：R.drawable.*
	 * @param packageName 应用包名,如： com.easy
	 * @param className activity名  如：com.easy.mainActivity
	 */
	public static void createShortCut(Context context,String appName,int iconSource,String packageName,String className) {
			Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
			shortcutIntent.putExtra("duplicate", false);
			Intent intent = new Intent();
//			intent.setComponent(new ComponentName("com.nxgoods", "com.nxgoods.MainActivity"));
			intent.setComponent(new ComponentName(packageName, className));

			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					Intent.ShortcutIconResource.fromContext(context, iconSource));
			context.sendBroadcast(shortcutIntent);
	}
	
	/**
	 *  删除应用快捷方式	  
	 * @param cx
	 * @param appName 应用名字 如'百度应用'
	 * @param packageName  应用包名,如： com.easy
	 * @param className activity名  如：com.easy.mainActivity
	 */
	public static void delShortcut(Context cx,String appName,String packageName,String className) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(packageName, className));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		cx.sendBroadcast(shortcut);
	}

}
