package com.chongwu.activity;

import com.chongwu.R;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity {

	protected ImageButton backButton;
	protected TextView title;
	protected FragmentActivity parentActivity;
	protected FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parentActivity = this;
		fragmentManager = parentActivity.getSupportFragmentManager();

		title = (TextView) findViewById(R.id.title_textView);
		title.setText(showTitle());

		if (isShowBackButton()) {
			backButton = (ImageButton) findViewById(R.id.back_btn);
			backButton.setVisibility(View.VISIBLE);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseActivity.this.finish();
				}
			});
		}

	}

	/**
	 * 显示标题
	 */
	protected abstract String showTitle();

	/**
	 * 是否显示返回按钮
	 */
	protected boolean isShowBackButton() {
		return true;
	}

	/**
	 * 返回左边按钮文本，如果文本为null,左边按钮不显示
	 * 
	 * @return
	 */
	protected String showLeftButtonText() {
		return null;
	}

}
