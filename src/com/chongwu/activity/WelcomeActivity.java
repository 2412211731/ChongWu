package com.chongwu.activity;

import com.chongwu.R;
import com.chongwu.utils.common.AppSharedPreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class WelcomeActivity extends Activity {
    private Context context;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(isNewUser()){
				Intent intent = new Intent(context, NewUserIndex.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
				//下次不提示
				AppSharedPreference.saveInt(context, "isNewUser", 1);
			}else{
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
 			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		　/**全屏设置，隐藏窗口所有装饰*/
//		　　getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN，
//		　　WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		  
		setContentView(R.layout.welcome_activity);
		context = getBaseContext();
		//延迟几秒跳转到主页
		handler.sendEmptyMessageDelayed(0, 500);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 判断是否为第一次使用应用
	 * @return
	 */
	public boolean isNewUser(){
		 int isNewUser = AppSharedPreference.getInt(context, "isNewUser");
		 if(isNewUser > 0){
			 return false;
		 }else{
			 return true;
		 }
	}

}
