package com.chongwu.activity.Fragment;

import com.chongwu.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Kaka
 * 
 */
public class TitleFragment extends Fragment {
	private ImageButton backBtn;
	private int backBtnVisible = View.VISIBLE;
	private TextView title;
    private String titleStr="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.title_fragment, container, false);

		backBtn = (ImageButton) view.findViewById(R.id.back_btn);
		backBtn.setVisibility(backBtnVisible);
		
		title = (TextView) view.findViewById(R.id.title_textView);
		title.setText(titleStr);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public ImageButton getBackBtn() {
		return backBtn;
	}
	
	public void setBackBtnOnclickListener(OnClickListener onClickListener) {
		 backBtn.setOnClickListener(onClickListener);
	}
	
	public void setBackBtnVisible(int visibility) {
		backBtnVisible = visibility;
	}

	public TextView getTitle() {
		return title;
	}
	
	public void setTitleText(String str) {
		try{
			 title.setText(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setTitleStr(String str){
		titleStr = str;
	}
	
	 
}
