package com.chongwu.activity.Fragment;

import java.util.ArrayList;
import java.util.List;
import com.chongwu.R;
import com.chongwu.adapter.SampleFragmentPagerAdapter;
import com.chongwu.utils.common.AppLog;
import com.chongwu.widget.common.InsideViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RadioGroup;

/**
 * 赏图页面
 * 
 * @author Kaka
 * 
 */
public class JiaoLiuFragment extends Fragment implements OnPageChangeListener {
	private InsideViewPager viewPager;
	private FragmentActivity parentActivity;
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parentActivity = getActivity();
		fragmentManager = parentActivity.getSupportFragmentManager();
	}

	/**
	 * 重写此方法来定义Fragment的view内容
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(view);
				AppLog.LogA("remove view");
			}
			return view;
		} else {
			view = inflater.inflate(R.layout.jiaoliu_fragment, container, false);
			List<Fragment> list = getFragments();
			viewPager = (InsideViewPager) view.findViewById(R.id.viewPager_jialiu);
			viewPager.removeAllViewsInLayout();
			viewPager.setViewParent(viewPager.getParent());
			viewPager.setOffscreenPageLimit(3);
			viewPager.setAdapter(new SampleFragmentPagerAdapter(getFragmentManager()));
			viewPager.setOnPageChangeListener(this);
			radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_jialiu);
			return view;
		}
	}

	// 修改这里返回不同的fragment
	private List<Fragment> getFragments() {
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new JiaoLiuFragment());
		list.add(new JiaoLiuFragment());
		list.add(new JiaoLiuFragment());
		return list;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
			radioGroup.check(R.id.btn1_jialiu);
			break;
		case 1:
			radioGroup.check(R.id.btn2_jialiu);
			break;
		case 2:
			radioGroup.check(R.id.btn3_jialiu);
			break;
		default:
			break;
		}

	}

/*	class ViewPagerOnTouchListener implements OnTouchListener {
		float x, y;
		// 移动超过该距离表示移动：非点击事件
		private int MOVE_MIN_POI = 30;
		private ViewPager viewPager;
		private int currentItemIndex;// 当前显示第几页

		public ViewPagerOnTouchListener(ViewPager view) {
			this.viewPager = view;
			currentItemIndex = viewPager.getCurrentItem();
		}

		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				AppLog.LogA("@move: " + "ACTION_DOWN");
				x = event.getX();
				y = event.getY();
				AppLog.LogA("@move: " + "x=" + x);
				AppLog.LogA("@move: " + "y=" + y);
				if (viewPagerSet != null) {
					viewPagerSet.setViewPagerRequestDisallowInterceptTouchEvent(true);
					setViewPagerRequestDisallowInterceptTouchEvent(false);
				}
				break;

			case MotionEvent.ACTION_UP:
				AppLog.LogA("@move: " + "ACTION_UP");
				AppLog.LogA("@move: " + "event.getX()-x = " + (event.getX() - x));
//				if ((event.getX() - x) > MOVE_MIN_POI) {// 向右滑动
//					viewPager.setCurrentItem(currentItemIndex + 1);
//				} else if ((event.getX() - x) < -MOVE_MIN_POI) {// 向左滑动
//					viewPager.setCurrentItem(currentItemIndex - 1);
//				}
				break;

			case MotionEvent.ACTION_MOVE:
				AppLog.LogA("@move: " + "ACTION_MOVE");
				AppLog.LogA("@move: " + "currentItemIndex = " + currentItemIndex);
				AppLog.LogA("@move: " + "viewPager.getChildCount() = " + viewPager.getChildCount());
				AppLog.LogA("@move: " + "event.getX()-x = " + (event.getX() - x));

				if (currentItemIndex == 0 && (event.getX() - x) > MOVE_MIN_POI) {// 向右滑动
					AppLog.LogA("@move: " + "currentItemIndex == 0");
					AppLog.LogA("@move: " + "向左滑动");
					if (viewPagerSet != null) {
						viewPagerSet.setViewPagerRequestDisallowInterceptTouchEvent(false);
					}
					setViewPagerRequestDisallowInterceptTouchEvent(true);
				} else if (currentItemIndex == viewPager.getChildCount() && (event.getX() - x) < -MOVE_MIN_POI) {// 向左滑动
					AppLog.LogA("@move: " + "向右滑动");
					if (viewPagerSet != null) {
						viewPagerSet.setViewPagerRequestDisallowInterceptTouchEvent(false);
					}
					setViewPagerRequestDisallowInterceptTouchEvent(true);
				} else {
					if (viewPagerSet != null) {
						viewPagerSet.setViewPagerRequestDisallowInterceptTouchEvent(true);
					}
					setViewPagerRequestDisallowInterceptTouchEvent(false);
					viewPager.scrollBy(-(int) (event.getX() - x), 0);
				}
				break;
			default:
				break;
			}

			return true;
		}
	}
*/
}
