package org.sparcs.onestepandroid.sitesuggestion;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;
import org.sparcs.onestepandroid.votesurvey.VoteSurveyMainListAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SiteSuggestionListFragment extends Fragment {
	private View view;
	private ListView listview;
	private ArrayList<SiteSuggestionInfo> data;
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LinearLayout loading = (LinearLayout) view.findViewById(R.id.site_suggestion_loading);
			loading.setVisibility(View.GONE);
			TextView header = (TextView) view.findViewById(R.id.site_suggestion_section_header);
			header.setVisibility(View.VISIBLE);
			listview.setAdapter(new SiteSuggestionListAdapter(getActivity(),data));
			listview.setVisibility(View.VISIBLE);
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.site_suggestion, null);
		listview = (ListView) view.findViewById(R.id.site_suggestion_list);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				TextView urlview = (TextView) arg1.findViewById(R.id.site_suggestion_item_url);
				String url = urlview.getText().toString();
				if (!url.startsWith("http://")) url = "http://" + url;
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			}
		});
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getSiteSuggestion();
				int status = returning.getStatus();
				data = new ArrayList<SiteSuggestionInfo>();
				
				if (status == 500) {
					SiteSuggestionInfo info = new SiteSuggestionInfo();
					info.setName("서버와의 연결이 끊겼습니다.");
					info.setUrl("");
					data.add(info);
				}
				else {
					if (status == 200) {
						XmlParser parser = new XmlParser();
						ArrayList<SiteSuggestionInfo> parsed = parser.parseSiteSuggestionInfo(returning.getResponse());
						data = parsed;
						
					}
					else {
						SiteSuggestionInfo info = new SiteSuggestionInfo();
						info.setName("서버와의 연결이 끊겼습니다.");
						info.setUrl("");
						data.add(info);
					}
				}
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		
		return view;
	}
}