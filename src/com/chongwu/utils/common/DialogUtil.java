package com.chongwu.utils.common;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DialogUtil {

	/**
	 * 此方法为Service用于显示系统的dialog：注意不要在线程调用此方法，那样获取不到主线程的handler
	 * @param mContext
	 * @param layoutSourceId
	 * @param callback
	 */
	public static void showSystemDialog(Context mContext, int layoutSourceId, Callback<AlertDialog> callback) {
		/* create ui */
		View v = View.inflate(mContext, layoutSourceId, null);
		AlertDialog.Builder b = new AlertDialog.Builder(mContext);
		b.setView(v);
		final AlertDialog d = b.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
		d.show();

		/* set size & pos */
		WindowManager.LayoutParams lp = d.getWindow().getAttributes();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (display.getHeight() > display.getWidth()) {
			lp.width = (int) (display.getWidth() * 1.0);
		} else {
			lp.width = (int) (display.getWidth() * 0.5);
		}
		d.getWindow().setAttributes(lp);

		if(null != callback){
			callback.handle(d);
		}
	}

}
