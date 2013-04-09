package com.example.onestep.noticeBoard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.onestep.R;
import com.example.onestep.util.NetworkManager;
import com.example.onestep.util.NetworkReturning;
import com.example.onestep.util.XmlParser;

public class TempTestAdapter extends ArrayAdapter<ArticleListInfo>{
	Context context;
	LayoutInflater inflater;
	ArrayList<ArticleListInfo> cachedList;
	ArrayList<ArticleListInfo> wrapperList;
	String boardName;
	int from = 1;
	int count = 15;
	static int MAX_LENGTH = 5;
	String type;

	public TempTestAdapter(final Context context, final ArrayList<ArticleListInfo> list, final String boardName, final String type) {
		// TODO Auto-generated constructor stub
		super(context, 0, list);
		this.cachedList = new ArrayList<ArticleListInfo>();
		this.context = context;
		this.boardName = boardName;
		this.inflater = LayoutInflater.from(context);
		this.type = type;
		this.wrapperList = list;
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getArticleList(boardName, from, count, type);
				int status = returning.getStatus();

				if (status == 500) {
					ArticleListInfo info = new ArticleListInfo();
					info.setTitle("서버와의 연결이 끊겼습니다.");
					wrapperList.add(info);
				}
				else {
					if (status == 401) {
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
						NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						returning = NetworkManager.INSTANCE.getArticleList(boardName, from, count, type);
						status = returning.getStatus();
					}

					if (status == 200) {
						XmlParser parser = new XmlParser();
						ArrayList<ArticleListInfo> parsed = parser.parseArticleListInfo(returning.getResponse());
						wrapperList.addAll(parsed);
					}
					else {
						ArticleListInfo info = new ArticleListInfo();
						info.setTitle("서버와의 연결이 끊겼습니다.");
						wrapperList.add(info);
					}
				}
				View a = new View(context);
				a.post(
						new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								notifyDataSetChanged();
							}
						});
			}
		}).start();
		
		
	}

	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {
		View v = convertView;
		if (v == null)
			v = LayoutInflater.from(context).inflate(R.layout.article_secondary_line, parent, false);
		String text = getItem(position).getTitle();
		((TextView)v.findViewById(R.id.textView1)).setText(text);
		if (getItem(position).getTime() != "") {
			long timestamp = Long.parseLong(getItem(position).getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getDefault());
			calendar.setTimeInMillis(timestamp);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
			((TextView)v.findViewById(R.id.textView2)).setText(
					new StringBuilder()
					.append(formatter.format(calendar.getTime()))
					.append(" | ")
					.append(getItem(position).getWriter())
					.append(" | ")
					.append(getItem(position).getHit())
					.toString());
		}
		else
			((TextView)v.findViewById(R.id.textView2)).setText("");
		return v;
	}
}
