/**
 * 
 */
package com.chongwu;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.chongwu.config.Config;
import com.chongwu.service.LocationService;
import com.chongwu.utils.common.Image.ImageLoader;
import com.taobao.top.android.TopAndroidClient;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * @author junyan.hj
 * 
 */
public class SampleApplication extends Application {
	private static Context context;

	// 百度地图服务
	public static BMapManager mBMapManager = null;

	public static ImageLoader imageLoader;
	public static LocationService locationService;

	@Override
	public void onCreate() {
		super.onCreate();
		TopAndroidClient.registerAndroidClient(getApplicationContext(), Config.app_key, Config.app_secret, Config.app_callback_url);
		context = getApplicationContext();
		imageLoader = new ImageLoader();

		initEngineManager(this);

		locationService = new LocationService(context);
		locationService.start();
	}

	@Override
	public void onTerminate() {
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();

	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(Config.BAIDU_MAP_KEY, new MyGeneralListener())) {
			Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(context, "您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(context, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(context, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
			}
		}
	}

}
