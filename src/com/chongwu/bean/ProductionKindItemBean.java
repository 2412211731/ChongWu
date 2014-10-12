package com.chongwu.bean;

import java.io.Serializable;

public class ProductionKindItemBean implements Serializable{
    private String text;
    private int imgOn;
    private int imgOff;
    
	public ProductionKindItemBean(String str,int imgOn,int imgOff) {
		 this.text = text;
		 this.imgOn = imgOn;
		 this.imgOff = imgOff;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getImgOn() {
		return imgOn;
	}

	public void setImgOn(int imgOn) {
		this.imgOn = imgOn;
	}

	public int getImgOff() {
		return imgOff;
	}

	public void setImgOff(int imgOff) {
		this.imgOff = imgOff;
	}

}
