package com.chongwu.activity;

import java.util.ArrayList;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
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
import com.chongwu.config.Config;
import com.chongwu.config.Constants;
import com.chongwu.config.Constants.BroadCastName;
import com.chongwu.service.LocationService;
import com.chongwu.utils.InputMethod;
import com.chongwu.utils.common.AppLog;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 搜索地图页面
 * 
 * @author Kaka
 * 
 */
public class MapActivity extends BaseActivity {
	private Context context;
	
	private MapView mapView;
	private MapController mapController;
	private PupopOverlay overlay;
	private LocationData locData = new LocationData();
	private locationOverlay myLocationOverlay = null;
	private MKSearch mSearch;

	private Button locationBtn;// 定位按钮
	private View searchLayout;
	private EditText searchEditText;
	private ListView searchListView;
	private Button searchBtn;
	private Button searchListBtn;
	private ImageView searchClear;

	// 选中图标出现的弹出框
	private View window;
	private ImageView windowHead;
	private TextView windowName;
	private TextView windowsContent;

	// 广播接收器
	private Reciever reciever;

	// 交互进度条
	private ProgressDialog progressDialog;

	// 搜索结果列表
	private boolean isShowList = true;
	private ArrayAdapter<String> adapter;
	private ArrayList<GeoPoint> poiList;
	// 地图上图标列表
	ArrayList<InfoBean> infoList = new ArrayList<InfoBean>();
	private InfoBean bean = new InfoBean();// 选中图标的信息
	private OverlayItem item;// 选中的图标项

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.map_ativity);
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

		// if (LocationService.lng > 0 && LocationService.lat > 0) {// 获取到经纬度
		showMyLoctionIcon();
		// }
	}

	/**
	 * 显示我所在位置的图标
	 */
	private void showMyLoctionIcon() {
		mapController.animateTo(new GeoPoint(LocationService.lat, LocationService.lng));
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
		// 搜索框
		searchLayout = findViewById(R.id.map_search_layout);
		searchEditText = (EditText) findViewById(R.id.map_search_in);
		searchListBtn = (Button) findViewById(R.id.map_search_btn);
		searchClear = (ImageView) findViewById(R.id.map_search_clear);
		// 搜索结果列表
		adapter = new ArrayAdapter<String>(context, R.layout.option_item, R.id.item_text);
		poiList = new ArrayList<GeoPoint>();
		searchListView = (ListView) findViewById(R.id.map_search_list);
		searchListView.setAdapter(adapter);
		//
		locationBtn = (Button) findViewById(R.id.map_location_btn);
		searchBtn = (Button) findViewById(R.id.map_serach_btn);
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

		// 搜索按钮
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayout.setVisibility(View.VISIBLE);
			}
		});

		searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = searchEditText.getText().toString().length();
				if (length > 0) {
					searchListBtn.setText("搜索");
					searchClear.setVisibility(View.VISIBLE);
				} else {
					searchListBtn.setText("隐藏");
					searchClear.setVisibility(View.GONE);
					searchListView.setVisibility(View.GONE);
				}
			}
		});

		searchListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethod.hideInputMethod(context, searchEditText);

				int visible = searchClear.getVisibility();
				if (visible == View.GONE) {// 此时为隐藏按钮
					searchLayout.setVisibility(View.GONE);
				} else {// 此时为搜索按钮
					progressDialog.show();
					mSearch.poiSearchInCity(LocationService.cityName, searchEditText.getText().toString());
				}

			}
		});

		searchClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
			}
		});

		// 选中某项的时候去搜索
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				isShowList = false;
				TextView textView = (TextView) arg1.findViewById(R.id.item_text);
				searchEditText.setText(textView.getText());
				searchEditText.setSelection(textView.length());
				searchListView.setVisibility(View.GONE);

				GeoPoint p = poiList.get(arg2);

				// 此处应该改为获取到数据才滚动过去
				mapController.animateTo(p);
				getNetQs(p);
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
								Toast.makeText(context, "请输入正确的地址", Toast.LENGTH_SHORT).show();
								return;
							}
							// getNetQs(lat, lng, NOTSEARTCH);
						} catch (Exception e) {
							progressDialog.dismiss();
							Toast.makeText(context, "请输入正确的地址", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					progressDialog.dismiss();
					Toast.makeText(context, "请输入正确的地址", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}

			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

			}

			public void onGetPoiDetailSearchResult(int arg0, int arg1) {

			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) { // 错误号可参考MKEvent中的定义
				AppLog.LogA("******error:" + error);
				progressDialog.dismiss();

				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}

				adapter.clear();
				poiList.clear();
				isShowList = true;
				for (MKPoiInfo info : res.getAllPoi()) {
					if (info.name != null) {
						adapter.add(info.name);
						poiList.add(info.pt);
						System.out.println(" info.key : " + info.name);
						System.out.println(" info.address : " + info.address);
					}
				}
				adapter.notifyDataSetChanged();
				searchListView.setVisibility(View.VISIBLE);
			}

			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {

			}

			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

			}
		});

	}

	@Override
	protected void onDestroy() {
		if(mSearch != null){
			mSearch.destory();
		}
		if(mapView != null){
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

	@Override
	protected String showTitle() {
		return "地图";
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
				if (null != bean.getImageUrl() && !"".equals(bean.getImageUrl())) {
					// SampleApplication.imageLoader.loadImage(bean.getImageUrl(),
					// windowHead);
				}
			} catch (Exception e) {
			}

			MapView.LayoutParams layoutParam = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, item.getPoint(), 0, -40, MapView.LayoutParams.BOTTOM_CENTER);

			window.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "待添加跳转", Toast.LENGTH_SHORT).show();
					// Intent intent = new Intent();
					// intent.setClass(context, UserReported.class);
					// intent.putExtra("id", String.valueOf(window.getTag()));
					// startActivity(intent);
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
			if (intent.getAction().equals(Constants.BroadCastName.GET_LOCATION_INFO)) {// 获取到位置信息
				showMyLoctionIcon();
				progressDialog.dismiss();
			}
		}
	}

	private void getNetQs(GeoPoint p) {
		progressDialog.show();
		infoList.clear();
		overlay.removeAll();
		mapController.animateTo(new GeoPoint(p.getLatitudeE6(), p.getLongitudeE6()));

		// 构造虚拟数据
		for (int i = 0; i < 10; i++) {
			InfoBean bean = new InfoBean();
			bean.setImageUrl("www.baidu.com");
			bean.setName("宠友" + i);
			infoList.add(bean);
		}

		for (int i = 0; i < 10; i++) {
			OverlayItem oItem = new OverlayItem(new GeoPoint((int) (p.getLatitudeE6() + Math.random() * 10000), (int) (p.getLongitudeE6() + Math.random() * 10000)), infoList.get(i).getName(), "");
			oItem.setMarker(getResources().getDrawable(R.drawable.map_icon));
			overlay.addItem(oItem);
		}

		mapView.refresh();
		progressDialog.dismiss();

	}

}
