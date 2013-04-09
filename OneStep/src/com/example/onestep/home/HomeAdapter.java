package com.example.onestep.home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.onestep.R;

public class HomeAdapter extends ArrayAdapter<HomeArticle> {
	private LayoutInflater inflater;
	public HomeAdapter(Context context, ArrayList<HomeArticle> list) {
		super(context, 0, list);
		this.inflater = LayoutInflater.from(context);
	}
	static class ViewHolder {
		TextView title;
		TextView info;
	}
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			switch(getItemViewType(position)) {
			case 0:
				v = inflater.inflate(R.layout.article_section_header, parent, false);
				
				break;
			case 1:
				v = inflater.inflate(R.layout.article_single_line, parent, false);
				break;
			case 2:
				v = inflater.inflate(R.layout.article_secondary_line, parent, false);
				holder = new ViewHolder();
				holder.title = ((TextView)v.findViewById(R.id.textView1));
				holder.info = ((TextView)v.findViewById(R.id.textView2));
				v.setTag(holder);
				break;
			default:
				v = inflater.inflate(R.layout.article_section_header, parent, false);
			}
		}
		if (getItemViewType(position) == 2) {
			if (holder == null) {
				holder = (ViewHolder) v.getTag();
			}
			HomeArticle item =  getItem(position);
			holder.title.setText(item.getTitle());
			if (item.getTime() != "")
				holder.info.setText(String.format("%s | %s | %s", item.getTime(), item.getWriter(), item.getHit()));
			else
				holder.info.setText("");
		}
		
		((TextView)v.findViewById(R.id.textView1)).setText(getItem(position).getTitle());
		
		return v;
	}
	
	@Override
	public boolean isEnabled(int position) {
		if(getItemViewType(position) == 0) {
			return false;
		}
		return true;
	}
	@Override
	public int getItemViewType(int position) {
		switch (getItem(position).getType()) {
		case SECTION_HEADER:
			return 0;
		case SINGLE_LINE:
			return 1;
		case SECONDARY_LINE:
			return 2;
		default:
			return 1;
		}
	}
	@Override
	public int getViewTypeCount() {
		return 3;
	}

}
