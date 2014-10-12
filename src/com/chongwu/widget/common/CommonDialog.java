package com.chongwu.widget.common;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public abstract class CommonDialog extends Dialog {
	private Context context;
	private Dialog self;

	public CommonDialog(Context context, int layout, int style) {
		super(context, style);
		setContentView(layout);
		this.context = context;
		self = this;
		// set window params
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		// set width,height by density and gravity
//		float density = getDensity(context);
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		initListener();
	}

	public abstract void initListener();

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}
	
	public void closeDialog(){
		self.dismiss();
	}
}
