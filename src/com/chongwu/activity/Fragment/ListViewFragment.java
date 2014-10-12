package com.chongwu.activity.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.chongwu.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class ListViewFragment extends Fragment {
	private FragmentActivity parentActivity;
	private FragmentManager fragmentManager;
    private View view;
    private ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentActivity = getActivity();
		fragmentManager = parentActivity.getSupportFragmentManager();
	}

	/**
	 * 重写此方法来定义Fragment的view内容
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view != null){
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			if(viewGroup != null){
				viewGroup.removeView(view);
			}
			return view;
		}
		else{
			view = inflater.inflate(R.layout.listview_fragment, container, false);
			listView = (ListView) view.findViewById(R.id.listView);
//			List<String> list = new ArrayList<String>();
//			list.add("1");
//			list.add("2");
//			list.add("3");
//			ListAdapter adapter = new ArrayAdapter<String>(parentActivity, R.layout.listview_item, R.id.textView, list);
			listView.setAdapter(getAdapter());
			return view;
		}
	}

	public abstract BaseAdapter getAdapter() ;

}
