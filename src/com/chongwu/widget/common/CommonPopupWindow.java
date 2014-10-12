package com.chongwu.widget.common;

import com.chongwu.R;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.PopupWindow;

public class CommonPopupWindow {
	private Activity activity;
	private PopupWindow popupWindow;

	public CommonPopupWindow(Activity activity, int layoutXml) {
		super();
		this.activity = activity;
		ViewGroup layout = (ViewGroup) LayoutInflater.from(activity).inflate(
				layoutXml, null);
		// 初始化PopupWindow
		popupWindow = new PopupWindow(activity);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
	}

	/**
	 * 
	 * @param viewId
	 * @param x popupWindow会根据x值 
	 * @param y
	 * @param animationStyle
	 */
	public void showAtLocation(int viewId, int x, int y,int animationStyle) {
		if(animationStyle != 0){
			popupWindow.setAnimationStyle(animationStyle);
		}
		popupWindow.showAtLocation(activity.findViewById(viewId),
				Gravity.NO_GRAVITY, x, y);// 需要指定Gravity，默认情况是center.
	}

	public void dismiss() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param activity
	 * @return
	 */
	public int getFrameHeight() {
		// 状态栏的高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * 获取view的位置
	 * 
	 * @return int[0]:左； int[1]:上； int[2]:右； int[3]下；
	 */
	public int[] getViewLocation(int viewId) {
		// int[0]:左； int[1]:上； int[2]:右； int[3]下；
		int[] location = new int[4];
		activity.findViewById(viewId).getLocationOnScreen(location);
		return location;
	}


	/**
	 * 获取View下边沿y值
	 * 
	 * @param v
	 * @return
	 */
	public int getViewBottom(int viewId) {
		return activity.findViewById(viewId).getBottom();
	}

	/**
	 * 获取View上边沿y值
	 * 
	 * @param v
	 * @return
	 */
	public int getViewTop(int viewId) {
		return activity.findViewById(viewId).getTop();
	}

	/**
	 * 获取View左边沿x值
	 * 
	 * @param v
	 * @return
	 */
	public int getViewLeft(int viewId) {
		return activity.findViewById(viewId).getLeft();
	}

	/**
	 * 获取View右边沿x值
	 * 
	 * @return
	 */
	public int getViewRight(int viewId) {
		return activity.findViewById(viewId).getRight();
	}
	
	
}
