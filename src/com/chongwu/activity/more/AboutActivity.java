package com.chongwu.activity.more;

import com.chongwu.R;
import com.chongwu.activity.BaseActivity;
import android.os.Bundle;

/**
 * 关于我们页面
 * @author Kaka
 *
 */
public class AboutActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.about_activity);
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		
	}

	@Override
	protected String showTitle() {
		return "关于我们";
	}
}
