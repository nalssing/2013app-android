package org.sparcs.onestepandroid.votesurvey;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VoteSurveyMainListAdapter extends ArrayAdapter<VoteSurveyMainItem> {
	private LayoutInflater inflater;
	private ArrayList<VoteSurveyMainItem> list;
	
	public VoteSurveyMainListAdapter(Context context, ArrayList<VoteSurveyMainItem> list) {
		super(context, 0, list);
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public VoteSurveyMainItem getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		if ( convertView == null )
		{
			convertView = inflater.inflate(R.layout.vote_survey_main_item, null);
		}
		VoteSurveyMainItem item = getItem(position);
		TextView title = (TextView) convertView.findViewById(R.id.vote_survey_main_item_title);
		TextView type  = (TextView)	convertView.findViewById(R.id.vote_survey_main_item_type);
		title.setText(item.getTitle());
		type.setText(item.getType());
		if (item.getIs_done())
		{
			ImageView check = (ImageView) convertView.findViewById(R.id.vote_survey_main_item_check);
			check.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	

}
