package org.sparcs.onestepandroid.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventListAdapter extends ArrayAdapter<EventInfo> {

	Context context;
	//private ArrayList<EventInfo> data;
	private LayoutInflater inflater;
	
	public EventListAdapter(Context context, ArrayList<EventInfo> list) {
		super(context, 0, list);
		this.context = context;
		//this.data = list;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if ( convertView == null )
		{
			convertView = inflater.inflate(R.layout.site_suggetsion_item, null);
		}
		EventInfo info = this.getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.site_suggestion_item_name);
		TextView url = (TextView) convertView.findViewById(R.id.site_suggestion_item_url);
		Calendar cal = Calendar.getInstance();
		cal.setTime(info.getDate());
		Log.i("cal show",Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		String text = String.format(Locale.KOREA,"%s (%02d:%02d)", info.getTitle(),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
		name.setText(text);
		url.setText(info.getContent());
		
		return convertView;
	}

}
