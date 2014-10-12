package com.chongwu.activity;

import java.util.ArrayList;
import com.chongwu.R;
import com.chongwu.activity.Fragment.GouWuFragment;
import com.chongwu.activity.Fragment.JiaoLiuFragment;
import com.chongwu.activity.Fragment.ShangTuFragment;
import com.chongwu.activity.Fragment.MoreFragment;
import com.chongwu.activity.Fragment.TitleFragment;
import com.chongwu.adapter.MainPagerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener, OnPageChangeListener {
	private Context context;

	private ViewPager viewPager;
	private ArrayList<Fragment> arrayList = new ArrayList<Fragment>();

	private FragmentManager fragmentManager;
	private TitleFragment titleFragment;
	private GouWuFragment gouWuFragment;
	private JiaoLiuFragment jiaoLiuFragment;
	private ShangTuFragment shangTuFragment;
	private MoreFragment moreFragment; 
 
	private RadioGroup footRadioGroup;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initData();
		initListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * 初始化参数
	 */
	public void initData() {
		context = getBaseContext();
		fragmentManager = getSupportFragmentManager();

		titleFragment = new TitleFragment();
		// 注意：此时还不能调用titleFragment.setTitleText(str),因为还没有执行该fragment的onCreatView方法，此时title的textView引用为空
		titleFragment.setTitleStr(getResources().getString(R.string.gouwu));
		titleFragment.setBackBtnVisible(View.GONE);
		
//		titleFragment.setBackBtnVisible(View.GONE);
		fragmentManager.beginTransaction().add(R.id.title_frag, titleFragment).commit();

		gouWuFragment = new GouWuFragment();
		jiaoLiuFragment = new JiaoLiuFragment();
		shangTuFragment = new ShangTuFragment();
		moreFragment = new MoreFragment();
		
		arrayList.add(gouWuFragment);
		arrayList.add(jiaoLiuFragment);
		arrayList.add(shangTuFragment);
		arrayList.add(moreFragment);

		viewPager = (ViewPager) findViewById(R.id.viewPager_main_activity);
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(new MainPagerAdapter(fragmentManager,arrayList));
		viewPager.setOnPageChangeListener(this);

		footRadioGroup = (RadioGroup) findViewById(R.id.footRadioGroup);
	}

	/**
	 * 给控件添加监听器
	 */
	public void initListener() {
		footRadioGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * 把fragment从view断开关联
	 * 
	 * @param fragment
	 *            需要断开关联的fragment
	 * @param fragmentTransaction
	 *            事务
	 */
	public void detachFragmentFromView(Fragment fragment, FragmentTransaction fragmentTransaction) {
		if (null != fragment) {
			fragmentTransaction.detach(fragment);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gouWu_radioBtn:
			titleFragment.setTitleText(getResources().getString(R.string.gouwu));
			// fragmentManager.beginTransaction().replace(R.id.content_fragment,
			// gouWuFragment).commit();
			viewPager.setCurrentItem(0);
			break;
		case R.id.jiaoLiu_radioBtn:
			titleFragment.setTitleText(getResources().getString(R.string.jiaoliu));
			// getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,
			// jiaoLiuFragment).commit();
			viewPager.setCurrentItem(1);
			break;
		case R.id.shangTu_radioBtn:
			titleFragment.setTitleText(getResources().getString(R.string.shangtu));
			// getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,
			// shangTuFragment).commit();
			viewPager.setCurrentItem(2);
			break;
		case R.id.more_radioBtn:
			titleFragment.setTitleText(getResources().getString(R.string.more));
			// getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,
			// moreFragment).commit();
			viewPager.setCurrentItem(3);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			footRadioGroup.check(R.id.gouWu_radioBtn);
			break;
		case 1:
			footRadioGroup.check(R.id.jiaoLiu_radioBtn);
			break;
		case 2:
			footRadioGroup.check(R.id.shangTu_radioBtn);
			break;
		case 3:
			footRadioGroup.check(R.id.more_radioBtn);
			break;
		default:
			break;
		}

	}
}
