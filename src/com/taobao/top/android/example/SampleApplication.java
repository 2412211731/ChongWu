/**
 * 
 */
package com.taobao.top.android.example;

import com.taobao.top.android.TopAndroidClient;
//import com.taobao.top.android.TopAndroidClient.Env;

import android.app.Application;

/**
 * @author junyan.hj
 *
 */
public class SampleApplication extends Application {
	@Override  
	public void onCreate() {  
		super.onCreate();
		TopAndroidClient.registerAndroidClient(getApplicationContext(), "21682376", "b0d0017bdfd69050c0acaac80c3fd660", "com.taobao.top.android.sample://");
		//TopAndroidClient.registerAndroidClient(getApplicationContext(), "519255", "988d57871c1fb8767a9b0875b28e5c17", "callback://authresult",Env.DAILY);
		
	}
}
