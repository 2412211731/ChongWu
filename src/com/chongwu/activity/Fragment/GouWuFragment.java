package com.chongwu.activity.Fragment;

import java.util.ArrayList;
import com.chongwu.R;
import com.chongwu.adapter.GouWuGridViewAdapter;
import com.chongwu.bean.GouWuGridViewItemBean;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 产品分类列表
 * 
 * @author Kaka
 * 
 */
public class GouWuFragment extends Fragment implements OnCheckedChangeListener {
	private Context context;

	private RadioGroup radioGroup;
    private GridView gridView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getBaseContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.production_kind_fragment,
				container, false);
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		
		ArrayList<GouWuGridViewItemBean> datas = getGouwoProductions();

		gridView = (GridView) view.findViewById(R.id.gridView_gouwu_fragment);
		gridView.setAdapter(new GouWuGridViewAdapter(datas, context));
		return view;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.chuxingyongpin_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getGouLiangProductions(), context));
			break;
		case R.id.richangyongpin_radioBtn: 
			gridView.setAdapter(new GouWuGridViewAdapter(getYifuProductions(), context));
			break;
		case R.id.gougoushipin_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getGouwoProductions(), context));
			break;
		case R.id.gougouwanju_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getWanjuProductions(), context));
			break;
		case R.id.meirongqingjie_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getWanjuProductions(), context));
			break;
		case R.id.yingyangbaojian_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getWanjuProductions(), context));
			break;
		case R.id.fuzhuangshipin_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getWanjuProductions(), context));
			break;
		case R.id.xiangquanpidai_radioBtn:
			gridView.setAdapter(new GouWuGridViewAdapter(getWanjuProductions(), context));
			break;
		default:
			break;
		}

	}
	
	//重写此方法，从网络获取数据
	public ArrayList<GouWuGridViewItemBean> getGouwoProductions() {
		ArrayList<GouWuGridViewItemBean> datas = new ArrayList<GouWuGridViewItemBean>();
		for(int i=0;i<12;i++){
			datas.add(new GouWuGridViewItemBean("gw", BitmapFactory.decodeResource(getResources(), R.drawable.pic023_on)));
		}
		return datas;
	}
	
	//重写此方法，从网络获取数据
	public ArrayList<GouWuGridViewItemBean> getYifuProductions() {
		ArrayList<GouWuGridViewItemBean> datas = new ArrayList<GouWuGridViewItemBean>();
		for(int i=0;i<12;i++){
			datas.add(new GouWuGridViewItemBean("yf", BitmapFactory.decodeResource(getResources(), R.drawable.pic023_on)));
		}
		return datas;
	}
	
	//重写此方法，从网络获取数据
	public ArrayList<GouWuGridViewItemBean> getGouLiangProductions() {
		ArrayList<GouWuGridViewItemBean> datas = new ArrayList<GouWuGridViewItemBean>();
		for(int i=0;i<12;i++){
			datas.add(new GouWuGridViewItemBean("gl", BitmapFactory.decodeResource(getResources(), R.drawable.pic023_on)));
		}
		return datas;
	}
	
	//重写此方法，从网络获取数据
	public ArrayList<GouWuGridViewItemBean> getWanjuProductions() {
		ArrayList<GouWuGridViewItemBean> datas = new ArrayList<GouWuGridViewItemBean>();
		for(int i=0;i<12;i++){
			datas.add(new GouWuGridViewItemBean("玩具", BitmapFactory.decodeResource(getResources(), R.drawable.pic023_on)));
		}
		return datas;
	}
}
