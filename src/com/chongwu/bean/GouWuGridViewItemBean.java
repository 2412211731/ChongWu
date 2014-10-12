package com.chongwu.bean;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class GouWuGridViewItemBean implements Serializable {

	private static final long serialVersionUID = 20131127L;

	private String title;
	private Bitmap bitmap;

	public GouWuGridViewItemBean(String title, Bitmap b) {
		super();
		this.title = title;
		bitmap = b;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
