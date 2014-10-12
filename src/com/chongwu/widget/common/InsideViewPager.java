package com.chongwu.widget.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * @author shiner
 * @version2.0
 * changed by Brook Xu, fixed a bug with null pointer
 */
public class InsideViewPager extends ViewPager {
	ViewParent viewParent;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (viewParent != null)
			viewParent.requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(arg0);
	}

	float lastX = 0;

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("mmm:down");
			// 嵌套的ViewPager，不会进入action_down
		case MotionEvent.ACTION_MOVE:
			System.out.println("mmm:move:" + arg0.getX() + ",curr:" + getCurrentItem());
			if (viewParent != null)
				viewParent.requestDisallowInterceptTouchEvent(true);
			if (getCurrentItem() == 0) {
				if (lastX != 0 && lastX < arg0.getX()) {
					System.out.println("mmm:end:one");
					onEndSide();
				}
			}
			if (getCurrentItem() == getAdapter().getCount() - 1) {
				if (lastX != 0 && lastX > arg0.getX()) {
					System.out.println("mmm:end:two");
					onEndSide();
				}
			}
			lastX = arg0.getX();
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("mmm:up");
		case MotionEvent.ACTION_CANCEL:
			System.out.println("mmm:cancel");
			lastX = 0;
			if (viewParent != null)
				viewParent.requestDisallowInterceptTouchEvent(false);
		default:
			break;
		}
		return super.onTouchEvent(arg0);
	}

	/**
	 * 到达边界
	 */
	private void onEndSide() {
		if (viewParent != null)
			viewParent.requestDisallowInterceptTouchEvent(false);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (viewParent != null)
			viewParent.requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	public InsideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewParent getViewParent() {
		return viewParent;
	}

	public void setViewParent(ViewParent viewParent) {
		this.viewParent = viewParent;
	}

}
