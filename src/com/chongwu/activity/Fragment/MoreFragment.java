package com.chongwu.activity.Fragment;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.chongwu.R;
import com.chongwu.activity.CityListActivity;
import com.chongwu.activity.MapActivity;
import com.chongwu.activity.NewUserIndex;
import com.chongwu.activity.more.AboutActivity;
import com.chongwu.activity.more.FeedbackActivity;
import com.chongwu.activity.more.GouWuActivity;
import com.chongwu.activity.more.MeirongActivity;
import com.chongwu.activity.more.ShangTuActivity;
import com.chongwu.adapter.GridViewAdapter;
import com.chongwu.config.Constants;
import com.chongwu.utils.common.AppLog;

import common.PinterestLikeAdapterView.com.huewu.pla.sample.PullToRefreshSampleActivity;
import common.cropPicture.CropPicActivityExample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 更多页面
 * 
 * @author Kaka
 * 
 */
public class MoreFragment extends Fragment {
	private View mView = null;
	private GridView mGridView = null;

	private GifView location_gif;
	private int[] mIcons = {
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			R.drawable.more_item_about_us,
			// 老数据
			R.drawable.more_item_switch_account, R.drawable.more_item_use_help,
			R.drawable.more_item_feedback,
			R.drawable.more_item_software_update,
			R.drawable.more_item_about_us, R.drawable.more_item_about_us,
			R.drawable.more_item_about_us, R.drawable.more_item_about_us,
			R.drawable.more_item_about_us, R.drawable.more_item_about_us };

	private int[] mTextIds = { R.string.gouwu, R.string.meirong,
			R.string.yiyuan, R.string.xiangqin, R.string.jiyang,
			R.string.tuoyun,
			R.string.binzang,
			R.string.xunlian,
			// 老数据
			R.string.switchAccount, R.string.useHelp, R.string.feedback,
			R.string.softwareUpdate, R.string.aboutUs, R.string.cityList,
			R.string.setHeaderIcon, R.string.map, R.string.shangtu,
			R.string.share };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// AppLog.LogA("***"+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[2].getMethodName()+"()***");
	}
 
	/**
	 * 重写此方法来定义Fragment的view内容
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.more_fragment, container, false);
		location_gif = (GifView) view.findViewById(R.id.location_gif);
		location_gif.setGifImage(R.drawable.loc_loading);
		// 设置显示的大小，拉伸或者压缩     
		location_gif.setShowDimension(300, 300);      
		// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示      
		location_gif.setGifImageType(GifImageType.COVER);
		
		location_gif.showAnimation();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		String[] texts = new String[mTextIds.length];
		for (int i = 0; i < mTextIds.length; i++) {
			texts[i] = getActivity().getResources().getString(mTextIds[i]);
		}

		GridViewAdapter adapter = new GridViewAdapter(
				getActivity(), mIcons, texts);
		mGridView = (GridView) getActivity().findViewById(R.id.gridView);
		mGridView.setAdapter(adapter);
		initListener();
	}

	private void initListener() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View arg1,
					int position, long arg3) {
				switch (position) {

				// 购物
				case 0:
					gotoActivity(GouWuActivity.class);
					break;
				// 美容
				case 1:
					Bundle b = new Bundle();
					b.putString("serverKind", Constants.ServerKind.MEIRONG);
					gotoActivity(MeirongActivity.class,b);
					break;
				// 医院
				case 2:
					gotoActivity(NewUserIndex.class);
					break;
				// 相亲
				case 3:
					gotoActivity(NewUserIndex.class);
					break;
				// 寄养
				case 4:
					gotoActivity(NewUserIndex.class);
					break;
				// 托运
				case 5:
					gotoActivity(NewUserIndex.class);
					break;
				// 殡葬
				case 6:
					gotoActivity(NewUserIndex.class);
					break;
				// 训练/学校
				case 7:
					gotoActivity(NewUserIndex.class);
					break;

				// case 0:// 切换账号
				// Toast.makeText(getActivity(), "switch account",
				// Toast.LENGTH_LONG).show();
				// gotoActivity(NewUserIndex.class);
				// break;
				// case 1:// 使用帮助
				// Toast.makeText(getActivity(), "help",
				// Toast.LENGTH_LONG).show();
				// break;
				// case 2:// 意见反馈
				// gotoActivity(FeedbackActivity.class);
				// break;
				// case 3:// 软件升级
				// Toast.makeText(getActivity(), "software update",
				// Toast.LENGTH_LONG).show();
				// break;
				// case 4:// 关于我们
				// gotoActivity(AboutActivity.class);
				// break;
				// case 5:// 城市列表
				// gotoActivity(CityListActivity.class);
				// break;
				// case 6:// 设置头像
				// gotoActivity(CropPicActivityExample.class);
				// break;
				// case 7:// 地图
				// gotoActivity(MapActivity.class);
				// break;
				// case 8:// 赏图
				// gotoActivity(ShangTuActivity.class);
				// break;
				// case 9:// 分享
				// gotoActivity(ShangTuActivity.class);
				// break;
				default:
					break;
				}
			}
		});
	}

	public void gotoActivity(Class className) {
		Intent intent = new Intent(getActivity(), className);
		startActivity(intent);
	}
	
	public void gotoActivity(Class className,Bundle extras) {
		Intent intent = new Intent(getActivity(), className);
		intent.putExtras(extras);
		startActivity(intent);
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
}
