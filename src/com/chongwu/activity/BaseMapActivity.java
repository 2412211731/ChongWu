package com.chongwu.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.chongwu.R;
import com.chongwu.SampleApplication;
import com.chongwu.bean.InfoBean;
import com.chongwu.config.Constants;
import com.chongwu.service.LocationService;
import com.chongwu.utils.common.AppLog;

public class BaseMapActivity extends Activity {
	private Context context;
	protected MapView mapView;
	private MapController mapController;
	private PupopOverlay overlay;
	private LocationData locData = new LocationData();
	private locationOverlay myLocationOverlay = null;
	protected MKSearch mSearch;

	private Button locationBtn;// 定位按钮

	// 选中图标出现的弹出框
	private View window;
	private ImageView windowHead;
	private TextView windowName;
	private TextView windowsContent;

	// 广播接收器
	private Reciever reciever;

	// 交互进度条
	private ProgressDialog progressDialog;

	private ArrayList<GeoPoint> poiList;
	// 地图上图标列表
	ArrayList<InfoBean> infoList = new ArrayList<InfoBean>();
	private InfoBean bean = new InfoBean();// 选中图标的信息
	private OverlayItem item;// 选中的图标项

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		super.onCreate(savedInstanceState);
		context = this;

		// 此句放到需要获取地址的地方
		SampleApplication.locationService.start();
		initMapView();
		initView();
		initListener();

		// 注册广播接受器
		reciever = new Reciever();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BroadCastName.GET_LOCATION_INFO);
		registerReceiver(reciever, filter);
	}

	private void initMapView() {
		mapView = (MapView) findViewById(R.id.baidu_map);
		mapController = mapView.getController();
		mapController.enableClick(true);
		mapController.setZoom(15);

		overlay = new PupopOverlay(mapView);
		myLocationOverlay = new locationOverlay(mapView);

		showMyLoctionIcon();
	}

	/**
	 * 显示我所在位置的图标
	 */
	private void showMyLoctionIcon() {
		mapController.animateTo(new GeoPoint(LocationService.lat,
				LocationService.lng));
		// 我的位置
		locData.latitude = LocationService.lat / 1E6;
		locData.longitude = LocationService.lng / 1E6;
		myLocationOverlay.setData(locData);
		myLocationOverlay.setLocationMode(LocationMode.NORMAL);
		mapView.getOverlays().clear();
		mapView.getOverlays().add(myLocationOverlay);
		// 其他图标
		mapView.getOverlays().add(overlay);
		mapView.refresh();
	}

	private void initView() {
		// 进度条
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("请稍后...");
		//
		mSearch = new MKSearch();
		mapView = (MapView) findViewById(R.id.baidu_map);
		// 搜索结果列表
		poiList = new ArrayList<GeoPoint>();
		//
		locationBtn = (Button) findViewById(R.id.map_location_btn);
		// 弹出窗
		window = getLayoutInflater().inflate(R.layout.pupop_map_window, null);
		windowHead = (ImageView) window.findViewById(R.id.window_head);
		windowName = (TextView) window.findViewById(R.id.window_name);
		windowsContent = (TextView) window.findViewById(R.id.window_content);
	}

	private void initListener() {
		// 定位按钮
		locationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog.show();
				SampleApplication.locationService.requestLocation();
			}
		});

		mSearch = new MKSearch();
		mSearch.init(SampleApplication.mBMapManager, new MKSearchListener() {

			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				try {
					if (arg0.type == MKAddrInfo.MK_GEOCODE) { // 地理编码：通过地址检索坐标点
						try {
							progressDialog.dismiss();
							double lat = arg0.geoPt.getLatitudeE6() / 1E6D;
							double lng = arg0.geoPt.getLongitudeE6() / 1E6D;
							if (lat == 0 && lng == 0) {
								Toast.makeText(context, "请输入正确的地址",
										Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (Exception e) {
							progressDialog.dismiss();
							Toast.makeText(context, "请输入正确的地址",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					progressDialog.dismiss();
					Toast.makeText(context, "请输入正确的地址", Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				}
			}

			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {

			}

			public void onGetPoiDetailSearchResult(int arg0, int arg1) {

			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) { // 错误号可参考MKEvent中的定义
				AppLog.LogA("******error:" + error);
				progressDialog.dismiss();

				if (error != 0 || res == null) {
					Toast.makeText(context, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}

				poiList.clear();
				for (MKPoiInfo info : res.getAllPoi()) {
					if (info.name != null) {
						poiList.add(info.pt);
						System.out.println(" info.key : " + info.name);
						System.out.println(" info.address : " + info.address);
					}
				}
			}

			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {

			}

			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {

			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {

			}
		});

	}

	@Override
	protected void onDestroy() {
		if (mSearch != null) {
			mSearch.destory();
		}
		if (mapView != null) {
			mapView.destroy();
		}
		if (reciever != null) {
			unregisterReceiver(reciever);
		}
		// 此句放到需要获取地址的地方
		SampleApplication.locationService.stop();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	public class locationOverlay extends MyLocationOverlay {

		public locationOverlay(MapView mapView) {
			super(mapView);
		}

		@Override
		protected boolean dispatchTap() {
			return true;
		}

	}

	@SuppressWarnings("rawtypes")
	public class PupopOverlay extends ItemizedOverlay {

		public PupopOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		public PupopOverlay(MapView mapView) {
			this(null, mapView);
		}

		@Override
		public boolean onTap(int index) {

			item = getItem(index);
			bean = infoList.get(index);

			windowName.setText(bean.getName());
			// window.setTag(bean.getId());
			windowHead.setImageResource(R.drawable.head_icon);
			try {
				if (null != bean.getImageUrl()
						&& !"".equals(bean.getImageUrl())) {
					// SampleApplication.imageLoader.loadImage(bean.getImageUrl(),
					// windowHead);
				}
			} catch (Exception e) {
			}

			MapView.LayoutParams layoutParam = new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					item.getPoint(), 0, -40, MapView.LayoutParams.BOTTOM_CENTER);

			window.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "待添加跳转", Toast.LENGTH_SHORT).show();
				}
			});

			mapView.addView(window, layoutParam);

			return false;

		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {// 点击非弹出框的地方
			mapView.removeView(window);
			return false;
		}

	}

	/**
	 * 获取到经纬度
	 * 
	 * @author Kaka
	 * 
	 */
	public class Reciever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constants.BroadCastName.GET_LOCATION_INFO)) {// 获取到位置信息
				showMyLoctionIcon();
				progressDialog.dismiss();
			}
		}
	}

	private void getNetQs(GeoPoint p) {
		progressDialog.show();
		infoList.clear();
		overlay.removeAll();
		mapController.animateTo(new GeoPoint(p.getLatitudeE6(), p
				.getLongitudeE6()));

		// 构造虚拟数据
		for (int i = 0; i < 10; i++) {
			InfoBean bean = new InfoBean();
			bean.setImageUrl("www.baidu.com");
			bean.setName("宠友" + i);
			infoList.add(bean);
		}

		for (int i = 0; i < 10; i++) {
			OverlayItem oItem = new OverlayItem(new GeoPoint(
					(int) (p.getLatitudeE6() + Math.random() * 10000),
					(int) (p.getLongitudeE6() + Math.random() * 10000)),
					infoList.get(i).getName(), "");
			oItem.setMarker(getResources().getDrawable(R.drawable.map_icon));
			overlay.addItem(oItem);
		}

		mapView.refresh();
		progressDialog.dismiss();

	}
	

}
