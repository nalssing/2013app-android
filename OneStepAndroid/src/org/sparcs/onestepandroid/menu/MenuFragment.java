package org.sparcs.onestepandroid.menu;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MenuFragment extends Fragment {
	private boolean isAnimationRunning = false;
	private MenuAdapter ma;
	private Context context;
	private ListView listview;
	public MenuFragment() {
		super();
	}
	public static MenuFragment newInstance(Context context) {
		MenuFragment instance = new MenuFragment();
		instance.initialize(context);
		return instance;
	}
	public void initialize(Context context) {
		this.context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		listview = (ListView) inflater.inflate(R.layout.menu_list, null);
		
		return listview;
	}
	public void setSelected(String selected) {
		ma.setSelected(selected);
	}
	public String getSelected() {
		return ma.getSelected();
	}
	@Override
	public void onResume() {
		super.onResume();
		new MenuHolder();
		ma = new MenuAdapter(context, MenuHolder.menus, listview);
		listview.setAdapter(ma);
	}
}
