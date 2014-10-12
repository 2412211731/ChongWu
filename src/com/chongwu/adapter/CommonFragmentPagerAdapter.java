package com.chongwu.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public abstract class CommonFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private int page = 2;

	public CommonFragmentPagerAdapter(FragmentManager fm,int page) {
		super(fm);
		this.page = page;
	}

	@Override
	public int getCount() {
		return page;
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		return super.instantiateItem(arg0, arg1);
	}
	
	

}
