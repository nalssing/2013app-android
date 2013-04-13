package com.example.onestep.noti;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onestep.R;

public class NotiAdapter extends ArrayAdapter<NotiItem> {
	private LayoutInflater inflater;
	private Context context;
	private ArrayList<NotiItem> list;
	
	public NotiAdapter(Context context, ArrayList<NotiItem> list) {
		super(context, 0, 0, list);
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}
	
	private class ViewHolder {
		TextView title;
		TextView message;
		TextView when;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.noti_line, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.when = (TextView) convertView.findViewById(R.id.when);
			convertView.setTag(holder);
		}
		NotiItem item = getItem(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.title.setText(item.getTitle());
		holder.message.setText(item.getMessage());
		holder.when.setText(item.getWhen());
		if (item.getChecked() != 0) {
			convertView.setBackgroundResource(R.drawable.menu_list_selected_selector);
		}
		else {
			convertView.setBackgroundResource(R.drawable.menu_list_selector);
		}
		return convertView;
	}
	
	public ArrayList<NotiItem> getList() {
		return list;
	}
	
	
}
