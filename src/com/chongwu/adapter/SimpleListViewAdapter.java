package com.chongwu.adapter;

import java.util.List;

import com.chongwu.R;
import com.chongwu.bean.GouWuGridViewItemBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author shiner
 */
public class SimpleListViewAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<String> datas;

	public SimpleListViewAdapter(List<String> datas, Context ctx) {
		super();
		this.context = ctx;
		this.datas = datas;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public String getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TextView textView;
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.simple_listview_grid_item, parent, false);
			textView = (TextView) convertView.findViewById(R.id.textView_item);

			holder = new ViewHolder();
			holder.textView = textView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			textView = holder.textView;
		}

		textView.setText(datas.get(position));
		return convertView;
	}

	class ViewHolder {
		public TextView textView;
	}

}
