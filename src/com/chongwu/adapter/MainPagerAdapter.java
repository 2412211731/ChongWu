package com.chongwu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chongwu.activity.Fragment.GouWuFragment;
import com.chongwu.activity.Fragment.MoreFragment;
import com.chongwu.activity.Fragment.ShangTuFragment;
import com.chongwu.activity.Fragment.TextFragment;
import com.chongwu.activity.Fragment.JiaoLiuFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
	
    private List<Fragment> list = new ArrayList<Fragment>();
    
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public MainPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
//		Fragment fragment = null;
//		switch (arg0) {
//		case 0:
//			fragment = new GouWuFragment();
//			break;
//		case 1:
//			fragment = new JiaoLiuFragment();
//			break;
//		case 2:
//			fragment = new ShangTuFragment();
//			break;
//		default:
//			fragment = new MoreFragment();
//			break;
//		}
//		return fragment;
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		return super.instantiateItem(arg0, arg1);
	}
	
	

}
