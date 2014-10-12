package com.chongwu.adapter;

import com.chongwu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter1 extends BaseAdapter {
    private int[] mBtnTop;
    private String[] mTitles;
    private LayoutInflater mInflater;
    
	public GridViewAdapter1(Context context, int[] btnTop,String[] titles) {
		super();
		mBtnTop = btnTop;
		mTitles = titles;
		mInflater = LayoutInflater.from(context);
	}
    
	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView title;

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.gridview_item, parent, false);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			title = (TextView) convertView.findViewById(R.id.textView); 

			holder = new ViewHolder();
			holder.mImageView = imageView;
			holder.mTitle = title;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			imageView = holder.mImageView;
			title = holder.mTitle;
		}

		imageView.setBackgroundResource(mBtnTop[position]);
		title.setText(mTitles[position]);
		return convertView;
	}
	
	class ViewHolder {
		public ImageView mImageView;
		public TextView mTitle;
	}

}
