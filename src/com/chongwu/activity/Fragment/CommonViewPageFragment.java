package com.chongwu.activity.Fragment;

import com.chongwu.R;
import com.chongwu.adapter.CommonFragmentPagerAdapter;
import com.chongwu.widget.common.InsideViewPager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * 赏图片段
 * 
 * @author Kaka
 * 
 */
public abstract class CommonViewPageFragment extends Fragment implements
		OnPageChangeListener, android.widget.RadioGroup.OnCheckedChangeListener {
	private InsideViewPager viewPager;
	private FragmentActivity parentActivity;
	private FragmentManager fragmentManager;

	private RadioGroup radioGroup;
	private View view;

	private int[] btns;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parentActivity = getActivity();
		fragmentManager = parentActivity.getSupportFragmentManager();

		btns = getBtns();
	}

	/**
	 * 重写此方法来定义Fragment的view内容
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(view);
			}
			return view;
		} else {
			view = inflater.inflate(R.layout.common_viewpage, container, false);
			//
			viewPager = (InsideViewPager) view.findViewById(R.id.viewPager);
			viewPager.removeAllViewsInLayout();
			viewPager.setViewParent(viewPager.getParent());
			viewPager.setOffscreenPageLimit(btns.length);
			// 设置viewPager内容
			viewPager.setAdapter(getAdapter());
			viewPager.setOnPageChangeListener(this);
			radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
			radioGroup.setOnCheckedChangeListener(this);
			return view;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < btns.length; i++) {
			if (radioGroup.getCheckedRadioButtonId() == btns[i]) {
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
	 * 
	 * @return
	 */
	public abstract int[] getBtns();

}
