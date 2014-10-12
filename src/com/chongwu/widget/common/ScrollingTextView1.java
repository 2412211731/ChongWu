package com.chongwu.widget.common;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class ScrollingTextView1 extends TextView {

	private int mViewWidth;
	private int textWidth;
	private int pianyi;
	
	public ScrollingTextView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScrollingTextView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScrollingTextView1(Context context) {
		super(context);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}

	//文本变了得重新计算宽和高
	@Override
	public void addTextChangedListener(TextWatcher watcher) {
		super.addTextChangedListener(watcher);
	}
	
	
	//计算宽度
    public void initWidth(){
    	Paint paint = this.getPaint();
		String str = this.getText().toString();
		
		textWidth = (int) paint.measureText(str);
		mViewWidth = this.getWidth();

		if (textWidth > mViewWidth) {// 文字最大宽度为屏幕宽度
			textWidth = mViewWidth;
		} else {
			if (this.getGravity() == Gravity.CENTER) {
				pianyi = (mViewWidth - textWidth) / 2;
			} else if (this.getGravity() == Gravity.RIGHT) {
				pianyi = mViewWidth - textWidth;
			} else if (this.getGravity() == Gravity.LEFT) {
				pianyi = 0;
			}else{
				pianyi = 0;
			}
		}
    }
    
    
    
}
