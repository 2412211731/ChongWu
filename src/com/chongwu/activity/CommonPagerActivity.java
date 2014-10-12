package com.chongwu.activity;

import com.chongwu.R;
import com.chongwu.adapter.CommonFragmentPagerAdapter;
import com.chongwu.adapter.SampleFragmentPagerAdapter;
import com.chongwu.widget.common.InsideViewPager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;

/**
 * 公共具有左右切换的页面
 * 
 * @author Kaka
 * 
 */
public abstract class CommonPagerActivity extends BaseActivity implements
		OnCheckedChangeListener, OnPageChangeListener {

	private Context context;
	private RadioGroup radioGroup;
	private InsideViewPager viewPager;

	private int[] btns;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		initView();
	}

	private void initView() {
		btns = getBtns();
		//
		viewPager = (InsideViewPager) findViewById(R.id.viewPager);
		viewPager.removeAllViewsInLayout();
		viewPager.setViewParent(viewPager.getParent());
		viewPager.setOffscreenPageLimit(btns.length);
		//设置viewPager内容
		viewPager.setAdapter(getAdapter());
		viewPager.setOnPageChangeListener(this);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for(int i=0;i<btns.length;i++){
			if(radioGroup.getCheckedRadioButtonId() == btns[i]){
				viewPager.setCurrentItem(i);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
			radioGroup.check(btns[arg0]);
	}
	
	public abstract CommonFragmentPagerAdapter getAdapter();
	
	/**
	 * 获取btn列表
	 * @return
	 */
	public abstract int[] getBtns();
}
