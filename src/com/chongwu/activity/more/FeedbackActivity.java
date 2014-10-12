package com.chongwu.activity.more;

import com.chongwu.R;
import com.chongwu.activity.BaseActivity;

import android.os.Bundle;

/** 
 * 意见反馈页面
 * @author Kaka
 *
 */
public class FeedbackActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.feedback_activity_layout);
		super.onCreate(savedInstanceState);
		showTitle();
		initView();
	}
	
	private void initView() {
		
	}

	@Override
	protected String showTitle() {
		return "意见反馈";
	}
	
}
