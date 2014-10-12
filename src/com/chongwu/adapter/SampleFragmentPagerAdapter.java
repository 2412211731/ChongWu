package com.chongwu.adapter;

import com.chongwu.activity.Fragment.TextFragment;
import com.chongwu.activity.Fragment.JiaoLiuFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private int page = 3;

	public SampleFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		switch (arg0) {
		case 0:
			fragment = new TextFragment();
			break;
		case 1:
			fragment = new TextFragment();
			break;
		case 2:
			fragment = new TextFragment();
			break;
		default:
			fragment = new TextFragment();
			break;
		}
		return fragment;
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
