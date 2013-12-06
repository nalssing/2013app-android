package org.sparcs.onestepandroid.votesurvey;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.util.Log;
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
		Holder holder;
		
		if ( convertView == null )
		{
			convertView = inflater.inflate(R.layout.vote_survey_main_item, null);
			holder = new Holder();
			holder.Title = (TextView) convertView.findViewById(R.id.vote_survey_main_item_title);
			holder.Type = (TextView) convertView.findViewById(R.id.vote_survey_main_item_type);
			holder.Check = (ImageView) convertView.findViewById(R.id.vote_survey_main_item_check);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		VoteSurveyMainItem item = getItem(position);
		holder.Title.setText(item.getTitle());
		holder.Type.setText(item.getType());
		if (item.getIs_done())
			holder.Check.setVisibility(View.VISIBLE);
		else
			holder.Check.setVisibility(View.INVISIBLE);

		return convertView;
	}

	class Holder
	{
		TextView Title;
		TextView Type;
		ImageView Check;
	}
	
}
