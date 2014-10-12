package com.chongwu.activity.Fragment;


import com.chongwu.R;
import com.chongwu.utils.common.AppLog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 交流页面
 * @author Kaka
 *
 */
public class TextFragment extends Fragment{
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onCreate(savedInstanceState);
		
	}

	/**
	 * 重写此方法来定义Fragment的view内容
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		
		View view = inflater.inflate(R.layout.text_fragment, container,false);
		return view;
	}

	@Override
	public void onResume() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onResume();
	}

	@Override
	public void onStart() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onStart();
	}
	
	@Override
	public void onAttach(Activity activity) {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onDetach();
	}
	
	
	
	@Override
	public void onPause() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onPause();
	}


	@Override
	public void onStop() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
		super.onDestroy();
	}
}
