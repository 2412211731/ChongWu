package com.chongwu.utils.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/**
 * 截屏
 * @author Administrator
 *
 */
public class ScreenShot {

	private static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		Bitmap b = Bitmap.createBitmap(b1, 0, 0, view.getWidth(), view.getHeight());
		view.destroyDrawingCache();
		return b;
	}

	private static void savePic(final Bitmap b, final String strFileName) {
		new Thread() {
			public void run() {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(strFileName);
					if (null != fos) {
						b.compress(Bitmap.CompressFormat.PNG, 90, fos);
						fos.flush();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fos) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public static void shoot(Activity a, String path) {
		ScreenShot.savePic(ScreenShot.takeScreenShot(a), path);
	}
}
