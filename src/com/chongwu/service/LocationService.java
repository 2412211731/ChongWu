package com.chongwu.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.chongwu.config.Constants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationService {
	private Context context;

	private int sleepTime = 10 * 60 * 1000;// 单位毫秒：每10分钟自动获取一次经纬度
	public LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	// 经纬度
	public static int lat;// 纬度
	public static int lng;// 经度
	public static String addressName;// 位置名字
	public static String cityName;

	public LocationService(Context context) {
		this.context = context;

		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(sleepTime);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}

	/**
	 * 开始服务
	 */
	public void start() {
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	/**
	 * 请求经纬度
	 */
	public void requestLocation() {
		mLocationClient.requestLocation();
	}

	/**
	 * 结束获取经纬度服务
	 */
	public void stop() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			showBackInfo(location);
			lat = (int) (location.getLatitude() * 1000000);
			lng = (int) (location.getLongitude() * 1000000);
			addressName = location.getAddrStr();
			cityName = location.getCity();
			// 发送广播
			Intent intent = new Intent(
					Constants.BroadCastName.GET_LOCATION_INFO);
			context.sendBroadcast(intent);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	/**
	 * 显示返回的数据
	 * 
	 * @param location
	 */
	private void showBackInfo(BDLocation location) {
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\ndirection : ");
			sb.append(location.getDirection());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			// 运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
		}
		Log.i("BaiduLocationApiDem", sb.toString());
	}

}
