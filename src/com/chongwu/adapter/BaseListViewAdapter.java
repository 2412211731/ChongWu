package com.chongwu.adapter;

import java.util.List;

import org.json.JSONObject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListViewAdapter extends BaseAdapter{

	private List<JSONObject> list;
	public BaseListViewAdapter(List<JSONObject> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = getView();
		}
		initView(convertView, list.get(position));
		return convertView;
	}
	
	public abstract View getView();
	
	public abstract void initView(View v,JSONObject jsonObj);

}
