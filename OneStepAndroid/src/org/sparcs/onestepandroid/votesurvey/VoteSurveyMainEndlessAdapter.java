package org.sparcs.onestepandroid.votesurvey;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.endless.EndlessAdapter;

public class VoteSurveyMainEndlessAdapter extends EndlessAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<VoteSurveyMainItem> cachedList;
	private ArrayList<VoteSurveyMainItem> wrapperList;
	private int from = 1;
	private int count = 15;

	
	public VoteSurveyMainEndlessAdapter(final Context context, ArrayList<VoteSurveyMainItem> list) {
		super(new ArrayAdapter<VoteSurveyMainItem>(context, 0, list)
				{
					@SuppressLint("SimpleDataFormat")
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null)
						{
							convertView = LayoutInflater.from(context).inflate(R.layout.vote_survey_main_item, parent, false);
						}
						VoteSurveyMainItem item =  getItem(position);
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
				});
		this.context = context;
		this.cachedList = new ArrayList<VoteSurveyMainItem>();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.wrapperList = list;
		this.from = list.size() + 1;
	}
	
	@Override
	protected void appendCachedData() {
		wrapperList.addAll(cachedList);
		cachedList.clear();
	}
	
	@Override
	protected View getPendingView(ViewGroup parent) {
		View prog = inflater.inflate(R.layout.progress_bar, parent, false);
		return prog;
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		Log.i("", "caching");
		NetworkReturning returning = NetworkManager.INSTANCE.getVoteSurveyList();
		int status = returning.getStatus();

		if (status == 500) {
			VoteSurveyMainItem info = new VoteSurveyMainItem();
			info.setTitle("서버와의 연결이 끊겼습니다.");
			cachedList.add(info);
			return false;
		}
		else {
			if (status == 401) {
				SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
				returning = NetworkManager.INSTANCE.login(
						preference.getString("username", ""), 
						preference.getString("password", ""));
				returning = NetworkManager.INSTANCE.getVoteSurveyList();
				status = returning.getStatus();
			}

			if (status == 200) {
				XmlParser parser = new XmlParser();
				ArrayList<VoteSurveyMainItem> parsed = parser.parseVoteSurveyMainItem(returning.getResponse());
				cachedList.addAll(parsed);
				from += count;
				if (parsed.size() < count)
					return false;
				return true;
			}
			else if (status == 401){
				VoteSurveyMainItem info = new VoteSurveyMainItem();
				info.setTitle("로그인이 필요합니다.");
				cachedList.add(info);
				return false;
			}
			else {
				VoteSurveyMainItem info = new VoteSurveyMainItem();
				info.setTitle("서버와의 연결이 끊겼습니다.");
				cachedList.add(info);
				return false;
			}
		}
	}

}
