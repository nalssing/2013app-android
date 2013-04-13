package com.example.onestep.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.onestep.R;


public class MenuFragment extends Fragment {
	private boolean isAnimationRunning = false;
	private MenuAdapter ma;
	public MenuFragment() {
		super();
		new SparseBooleanArray();
	}
	public boolean isAnimationRunning() {
		return isAnimationRunning;
	}

	public void setAnimationRunning(boolean isAnimationRunning) {
		this.isAnimationRunning = isAnimationRunning;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ListView listview = (ListView) inflater.inflate(R.layout.menu_list, null);
		new MenuHolder();
		ma = new MenuAdapter(getActivity(), MenuHolder.menus, listview);
		listview.setAdapter(ma);
		return listview;
	}
	public void setSelected(String selected) {
		ma.setSelected(selected);
	}
	public String getSelected() {
		return ma.getSelected();
	}
}
