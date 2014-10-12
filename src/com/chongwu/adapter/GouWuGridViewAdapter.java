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
public class GouWuGridViewAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	List<GouWuGridViewItemBean> datas;

	public GouWuGridViewAdapter(List<GouWuGridViewItemBean> datas, Context ctx) {
		super();
		this.context = ctx;
		this.datas = datas;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public GouWuGridViewItemBean getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TextView textView;
			final ImageButton imageButton;
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gouwu_grid_item, parent,false);
				imageButton = (ImageButton) convertView.findViewById(R.id.gouwu_grid_btn);
				textView = (TextView) convertView.findViewById(R.id.gouwu_grid_text);
				
				holder = new ViewHolder();
				holder.imageButton = imageButton;
				holder.textView = textView;
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
				imageButton = holder.imageButton;
				textView = holder.textView;
			}
			
			imageButton.setImageBitmap(datas.get(position).getBitmap());
			textView.setText(datas.get(position).getTitle());

			return convertView;
	}
	
	class ViewHolder{
		public TextView textView;
		public ImageButton imageButton;
	}

}
