package com.example.onestep.calendar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.onestep.R;

public class WeekdayAdapter extends BaseAdapter {
	private ArrayList<WeekdayInfo> mWeekdayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;
    
	public WeekdayAdapter(Activity activity, int weekday, ArrayList<WeekdayInfo> mWeekdayList) {
		this.mContext = activity;
		this.mResource = weekday;
		this.mWeekdayList = mWeekdayList;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mWeekdayList.size();
	}
	@Override
	public Object getItem(int pos) {
		return mWeekdayList.get(pos);
	}
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null)
		{
			convertView = mInflater.inflate(mResource, parent, false);
			
			TextView view = (TextView) convertView.findViewById(R.id.day_cell_tv_weekday);
			view.setText(mWeekdayList.get(position).getWeekday());
		}
		
		return convertView;
	}

}
