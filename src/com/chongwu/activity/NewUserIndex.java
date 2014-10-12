package com.chongwu.activity;

import java.util.ArrayList;
import java.util.List;

import com.chongwu.R;
import common.CirclePageIndicator.CirclePageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class NewUserIndex extends Activity {
	
	private Context context;
	private ViewPager mViewPager;	
	private CirclePageIndicator mIndicator;
	private ArrayList<View> viewList = new ArrayList<View>();
	private int[] drawableList = new int[]{R.drawable.yindao2,R.drawable.yindao3,R.drawable.yindao4};
	private int lastX;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现  
        setContentView(R.layout.new_user_index_activity);
        initData();
        initListener();
    }    
    
    private void initListener() {
    	mViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					if ((lastX - event.getX()) > 100 && (mViewPager.getCurrentItem() == viewList.size() - 1)) {
						Intent intent = new Intent(context,MainActivity.class);
						startActivity(intent);
						NewUserIndex.this.finish();
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 初始化参数
	 */
	public void initData(){
		context = getBaseContext();
		
		initViewList();
		MyPagerAdapter mAdapter = new MyPagerAdapter(context, viewList);
		mViewPager = (ViewPager)findViewById(R.id.new_user_index_viewPager);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.new_user_index_circlePageIndicator);
        mIndicator.setViewPager(mViewPager);
        
//        btn = (Button) findViewById(R.id.new_user_index_Btn);
	}

	private void initViewList() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		for(int i:drawableList){
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(layoutParams);
			imageView.setImageResource(i);
			viewList.add(imageView);
		}
	}
	
	class MyPagerAdapter extends PagerAdapter {
		private Context ctx;
		private List<View> viewList;

		public MyPagerAdapter(Context ctx, List<View> viewList) {
			super();
			this.ctx = ctx;
			this.viewList = viewList;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = viewList.get(position);
			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
