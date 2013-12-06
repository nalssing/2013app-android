package org.sparcs.onestepandroid.sitesuggestion;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SiteSuggestionListAdapter extends ArrayAdapter<SiteSuggestionInfo> {
	private Context context;
	private ArrayList<SiteSuggestionInfo> list;
	private LayoutInflater inflater;
	
	public SiteSuggestionListAdapter(Context context, ArrayList<SiteSuggestionInfo> list) {
		super(context,0,list);
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}


		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if ( convertView == null )
		{
			convertView = inflater.inflate(R.layout.site_suggetsion_item, null);
		}
		SiteSuggestionInfo info = this.getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.site_suggestion_item_name);
		TextView url = (TextView) convertView.findViewById(R.id.site_suggestion_item_url);
		name.setText(info.getName());
		url.setText(info.getUrl());
		
		return convertView;
	}
	
}