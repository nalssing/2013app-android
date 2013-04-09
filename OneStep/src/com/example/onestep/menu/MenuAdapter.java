package com.example.onestep.menu;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onestep.MainActivity;
import com.example.onestep.R;
import com.example.onestep.article.ArticleReadFragment;
import com.example.onestep.calendar.CalendarFragment;
import com.example.onestep.home.HomeFragment;
import com.example.onestep.noticeBoard.NoticeTabMenuFragment;


public class MenuAdapter extends ArrayAdapter<MyMenu> implements OnItemClickListener{
	private Context context;
	private ArrayList<MyMenu> list;
	private LayoutInflater inflater;
	private String selected;

	public MenuAdapter(Context context, ArrayList<MyMenu> list, ListView listview) {
		super(context, 0, list);
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.selected = "home";
		listview.setOnItemClickListener(this);
	}
	static class ViewHolder {
		TextView title;
		ImageView icon;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MyMenu item = getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			switch(item.getType()) {
			case SECTION_HEADER:
				convertView = inflater.inflate(R.layout.menu_section_header, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.textView1);
				holder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
				break;
			case SINGLE_LINE:
				convertView = inflater.inflate(R.layout.menu_single_line, parent, false);
				
				holder.title = (TextView) convertView.findViewById(R.id.textView1);
				break;
			case EXPANDABLE_LINE:
				convertView = inflater.inflate(R.layout.menu_expandable_line, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.textView1);
				break;
			default:
				ListView listview = (ListView) inflater.inflate(R.layout.sub_list, parent, false);
				ArrayList<MyMenu> childs = item.getChilds();
				MenuAdapter adapter = new MenuAdapter(context, childs, listview);
				childs.get(childs.size() - 1).setListview(listview);
				listview.setAdapter(adapter);
				listview.getLayoutParams().height = 
						context.getResources().getDimensionPixelSize(R.dimen.menu_height);
				listview.setSelection(listview.getAdapter().getCount()-1);
						//* item.getChilds().size();
				convertView = listview;
				
			}
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch(item.getType()) {
		case SECTION_HEADER:
			holder.icon.setImageResource(item.getIcon());
		case SINGLE_LINE:
		case EXPANDABLE_LINE:
			holder.title.setText(item.getTitle());
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		switch(getItem(position).getType()){
		case SECTION_HEADER:
			return 0;
		case SINGLE_LINE:
			return 1;
		case EXPANDABLE_SET:
			return 2;
		case EXPANDABLE_LINE:
			return 3;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		MyMenu menu = getItem(position);
		if (menu.getType() == MyMenu.Type.EXPANDABLE_LINE) {
			ListView listview = menu.getListview();
			int menuHeight = context.getResources().getDimensionPixelSize(R.dimen.menu_height);
			if (menu.isExpanded()) {
				listview.getLayoutParams().height = menuHeight;
				menu.setTitle("더 보기");
				((TextView)view.findViewById(R.id.textView1)).setText("더 보기");
				listview.requestLayout();
				listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				menu.setExpanded(false);
				
			}
			else {
				listview.getLayoutParams().height = menuHeight * listview.getAdapter().getCount();
				menu.setTitle("접기");
				((TextView)view.findViewById(R.id.textView1)).setText("접기");
				listview.requestLayout();
				menu.setExpanded(true);
			}
		}
		else {
			String tag = menu.getTag();
			MainActivity mainActivity = ((MainActivity)context);
			FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
			if (tag.startsWith("notice") || !tag.equals(selected)) {
				ArticleReadFragment article = (ArticleReadFragment) manager.findFragmentByTag("readarticle");
				if ( article!=null)
				{
					manager.beginTransaction().remove(article).commit();
					manager.popBackStack();
				}


				if (tag.equals("home")){
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new HomeFragment())
					.commit();
					mainActivity.setTitle("홈");
					selected = tag;
				}
				else if (tag.startsWith("notice")){
					NoticeTabMenuFragment fragment;
					try {
						fragment= (NoticeTabMenuFragment) manager
								.findFragmentById(R.id.content_frame);
					}
					catch (Exception e){
						fragment = null;
					}
					if (selected.equals("notice") && fragment != null) {
						if (tag.equals("notice/student")) {
							fragment.goToTab(1, null);
						}
						else if (tag.equals("notice/usc")) {
							fragment.goToTab(2, null);
						}
						else {
							fragment.goToTab(3, tag.split("/")[1]);
						}
					}
					else {
						Bundle bundle;
						fragment = new NoticeTabMenuFragment();
						bundle = new Bundle();
						fragment.setArguments(bundle);
						if (tag.equals("notice/student")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 1);
							bundle.putString("boardname", null);
						}
						else if (tag.equals("notice/usc")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 2);
							bundle.putString("boardname", null);
						}
						else {
							bundle = fragment.getArguments();
							bundle.putInt("position", 3);
							bundle.putString("boardname", tag.split("/")[1]);
						}
						manager
						.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.commit();
					}
					mainActivity.setTitle("공지");
					selected = tag;
				}
				else if (tag.equals("schedule"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new CalendarFragment())
					.commit();
					mainActivity.setTitle("일정");
					selected=tag;
				}

			}

			mainActivity.getSlidingMenu().toggle();
		}
	}

}


