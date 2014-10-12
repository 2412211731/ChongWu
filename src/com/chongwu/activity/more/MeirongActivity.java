package com.chongwu.activity.more;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.chongwu.R;
import com.chongwu.activity.BaseMapActivity;
import com.chongwu.activity.MapActivity.PupopOverlay;
import com.chongwu.activity.MapActivity.locationOverlay;
import com.chongwu.adapter.BaseListViewAdapter;
import com.chongwu.adapter.GridViewAdapter1;
import com.chongwu.config.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 美容页面
 * 
 * @author Kaka
 * 
 */
public class MeirongActivity extends BaseMapActivity {
	private Context context;
	private ImageButton backBtn;
	private CheckBox rightCheckBox;
	private CheckBox centerCheckBox;
	private View sort_layout;
	private View sortlist;
	private TextView tuijian;
	private ListView listView;
	private View mapLayout;// 地图
	// 服务列表
	private GridView gridView;
	int[] btnTop = new int[] { R.drawable.icon111, R.drawable.icon111,
			R.drawable.icon111, R.drawable.icon111, R.drawable.icon111,
			R.drawable.icon111, R.drawable.icon111, R.drawable.icon111,
			R.drawable.icon111, R.drawable.icon111, R.drawable.icon111,
			R.drawable.icon111 };
	String[] titles = new String[] { "不限", "美容", "寄养", "商店", "医院", "训练", "娱乐",
			"托运", "摄影", "酒店", "狗证", "殡葬" };
	// 选中的服务种类
	private String serverKind = Constants.ServerKind.BUXIAN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.meirong_activity);
		super.onCreate(savedInstanceState);
		context = this;
		// 初始化服务类型
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			serverKind = bundle.getString("serverKind");
		}

		//
		initView();
		initListener();
		// 获取查询数据 
		mSearch.poiSearchNearBy(""+serverKind, new GeoPoint(30593188,104081146), 5000);//单位米
	}

	private void initView() {
		backBtn = (ImageButton) findViewById(R.id.back_btn);
		centerCheckBox = (CheckBox) findViewById(R.id.checkBox1);
		rightCheckBox = (CheckBox) findViewById(R.id.checkBox2);
		rightCheckBox.setText(serverKind);
		tuijian = (TextView) findViewById(R.id.tuijian);
		// 初始化服务类型
		gridView = (GridView) findViewById(R.id.gridView);
		BaseAdapter adapter = new GridViewAdapter1(context, btnTop, titles);
		gridView.setAdapter(adapter);

		// 数据列表
		listView = (ListView) findViewById(R.id.listView);
		BaseListViewAdapter listAdapter = new BaseListViewAdapter(
				getListViewJsonArray()) {
			@Override
			public void initView(View v, JSONObject jsonObj) {
				try {
					((TextView) v.findViewById(R.id.title)).setText(jsonObj
							.getString("title"));
					((TextView) v.findViewById(R.id.phone)).setText(jsonObj
							.getString("phone"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public View getView() {
				return LayoutInflater.from(context).inflate(
						R.layout.listview_item1, null);
			}
		};
		listView.setAdapter(listAdapter);
		// 排序弹出框
		sort_layout = findViewById(R.id.sort_layout);
		sortlist = findViewById(R.id.sortlist);

		// 地图
		mapLayout = findViewById(R.id.mapLayout);

	}

	private void initListener() {
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MeirongActivity.this.finish();
			}
		});

		centerCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							listView.setVisibility(View.GONE);
							mapLayout.setVisibility(View.VISIBLE);
							// .startAnimation(AnimationUtils
							// .loadAnimation(context,
							// R.anim.slide_in_from_up));
						} else {
							mapLayout.setVisibility(View.GONE);
							listView.setVisibility(View.VISIBLE);
						}
					}
				});
		rightCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					showGridView();
				} else {
					closeGridView();
				}
			}

		});

		tuijian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sort_layout.getVisibility() == View.GONE) {
					showSortListDialog();
				} else {
					closeSortListDialog();
				}
			}
		});

		sort_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeSortListDialog();
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				rightCheckBox.setText(titles[arg2]);
				rightCheckBox.setChecked(false);
			}
		});
	}

	/**
	 * 显示排序选择列表
	 */
	public void showSortListDialog() {
		sort_layout.setVisibility(View.VISIBLE);
		sortlist.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_down));
	}

	/**
	 * 关闭排序选择列表
	 */
	public void closeSortListDialog() {
		sortlist.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_out_to_down));
		sortlist.getAnimation().setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				sort_layout.setVisibility(View.GONE);
			}
		});
	}

	private void closeGridView() {
		gridView.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_out_to_up));
		gridView.setVisibility(View.GONE);
	}

	private void showGridView() {
		gridView.setVisibility(View.VISIBLE);
		gridView.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_up));
	}

	public List<JSONObject> getListViewJsonArray() {
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			for (int i = 0; i < 5; i++) {
				JSONObject obj = new JSONObject();
				obj.put("title", "星星宠物医院");
				obj.put("phone", "18048576066");
				list.add(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
