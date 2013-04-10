package com.example.onestep.article;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.onestep.R;

public class ArticleReadAdapter extends ArrayAdapter<ArticleInfo> {
	private LayoutInflater inflater;
	public ArticleReadAdapter(Context context,  ArrayList<ArticleInfo> objects) {
		super(context, 0, 0, objects);
		inflater = LayoutInflater.from(context);
	}

	class ViewHolder {
		TextView header;
		TextView title;
		TextView author;
		TextView content;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		ArticleInfo info = getItem(position);
		if (convertView == null) {
			if (type == 0) {
				ViewHolder holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.article_read_article, parent, false);
				holder.header = (TextView)convertView.findViewById(R.id.header);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.author = (TextView)convertView.findViewById(R.id.author);
				holder.content = (TextView)convertView.findViewById(R.id.content);
				convertView.setTag(holder);
				
			}
			else {
				convertView = inflater.inflate(R.layout.article_read_reply, parent, false);
				//TODO 제대로 만들기
			}
		}
		ViewHolder holder = (ViewHolder)convertView.getTag();
		if (type == 0) {
			holder.header.setText(info.getBoard());
			holder.title.setText(info.getTitle());
			holder.author.setText("|".concat(info.getWriter()).concat("|"));
			holder.content.setText(Html.fromHtml(info.getContent()));
		}
		else {
			
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getItemViewType(int position) {
		switch (getItem(position).getType()) {
		case ARTICLE:
			return 0;
		default:
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	

}
