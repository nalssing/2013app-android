package org.sparcs.onestepandroid.article;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.login.LoginActivity;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.commonsware.cwac.endless.EndlessAdapter;

public class ArticleEndlessAdapter extends EndlessAdapter{
	Context context;
	LayoutInflater inflater;
	ArrayList<ArticleListInfo> cachedList;
	ArrayList<ArticleListInfo> wrapperList;
	String boardName;
	int from = 1;
	int count = 15;
	static int MAX_LENGTH = 5;
	String type;
	static class ViewHolder {
		TextView title;
		TextView info;
	}
	public ArticleEndlessAdapter(final Context context, ArrayList<ArticleListInfo> list, String boardName, String type) {
		// TODO Auto-generated constructor stub
		super(new ArrayAdapter<ArticleListInfo>(context, 0, list)
				{
			@SuppressLint("SimpleDateFormat")
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				ViewHolder holder;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.article_secondary_line, parent, false);
					holder.title = ((TextView)convertView.findViewById(R.id.textView1));
					holder.info = ((TextView)convertView.findViewById(R.id.textView2));
					convertView.setTag(holder);
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
				ArticleListInfo item =  getItem(position);
				holder.title.setText(item.getTitle());
				if (item.getTime() != "")
					holder.info.setText(String.format("%s | %s | %s", item.getTime(), item.getWriter(), item.getHit()));
				else
					holder.info.setText("");
				return convertView;
			}
				});
		this.cachedList = new ArrayList<ArticleListInfo>();
		this.context = context;
		this.boardName = boardName;
		this.inflater = LayoutInflater.from(context);
		this.type = type;
		this.wrapperList = list;
		this.from = list.size() + 1;
	}


	@Override
	protected View getPendingView(ViewGroup parent) {
		View prog = inflater.inflate(R.layout.progress_bar, parent, false);
		return prog;
	}


	@Override
	protected boolean cacheInBackground() throws Exception {
		Log.i("", "caching");
		NetworkReturning returning = NetworkManager.INSTANCE.getArticleList(boardName, from, count, type);
		int status = returning.getStatus();

		if (status == 500) {
			ArticleListInfo info = new ArticleListInfo();
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
				returning = NetworkManager.INSTANCE.getArticleList(boardName, from, count, type);
				status = returning.getStatus();
			}

			if (status == 200) {
				XmlParser parser = new XmlParser();
				ArrayList<ArticleListInfo> parsed = parser.parseArticleListInfo(returning.getResponse());
				cachedList.addAll(parsed);
				from += count;
				if (parsed.size() < count)
					return false;
				return true;
			}
			else if (status == 401){
				ArticleListInfo info = new ArticleListInfo();
				info.setTitle("로그인이 필요합니다.");
				cachedList.add(info);
				return false;
			}
			else {
				ArticleListInfo info = new ArticleListInfo();
				info.setTitle("서버와의 연결이 끊겼습니다.");
				cachedList.add(info);
				return false;
			}
		}
	}

	@Override
	protected void appendCachedData() {
		wrapperList.addAll(cachedList);
		cachedList.clear();
	}}
