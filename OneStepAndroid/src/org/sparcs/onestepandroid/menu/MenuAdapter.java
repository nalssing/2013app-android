package org.sparcs.onestepandroid.menu;

import java.util.ArrayList;

import org.sparcs.onestepandroid.MainActivity;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.about.AboutFragment;
import org.sparcs.onestepandroid.article.ArticleReadFragment;
import org.sparcs.onestepandroid.calendar.CalendarFragment;
import org.sparcs.onestepandroid.home.HomeFragment;
import org.sparcs.onestepandroid.noticeBoard.NoticeTabMenuFragment;
import org.sparcs.onestepandroid.noticeBoard.PolicyTabMenuFragment;
import org.sparcs.onestepandroid.policysuggestion.PolicyReadFragment;
import org.sparcs.onestepandroid.policysuggestion.PolicyWriteFragment;
import org.sparcs.onestepandroid.promotion.PromotionFragment;
import org.sparcs.onestepandroid.setting.SettingFragment;
import org.sparcs.onestepandroid.sitesuggestion.SiteSuggestionListFragment;
import org.sparcs.onestepandroid.votesurvey.VoteSurveyFormFragment;
import org.sparcs.onestepandroid.votesurvey.VoteSurveyMainFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
				ExpandAnimation ea = new ExpandAnimation(context, listview, 500, menuHeight);
				listview.startAnimation(ea);
				menu.setTitle("더 보기");
				((TextView)view.findViewById(R.id.textView1)).setText("더 보기");
				listview.requestLayout();
				listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				menu.setExpanded(false);
				
				
			}
			else {
				menu.setTitle("접기");
				((TextView)view.findViewById(R.id.textView1)).setText("접기");
				listview.requestLayout();
				menu.setExpanded(true);
				ExpandAnimation ea = new ExpandAnimation(context, listview, 500, menuHeight * listview.getAdapter().getCount());
				listview.startAnimation(ea);
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
				PolicyReadFragment rp = (PolicyReadFragment) manager.findFragmentByTag("readpolicy");
				if ( rp!=null)
				{
					manager.beginTransaction().remove(rp).commit();
					manager.popBackStack();
				}
				PolicyWriteFragment wp = (PolicyWriteFragment) manager.findFragmentByTag("writepolicy");
				if ( wp !=null)
				{
					manager.beginTransaction().remove(wp).commit();
					manager.popBackStack();
				}
				VoteSurveyFormFragment vp = (VoteSurveyFormFragment) manager.findFragmentByTag("survey");
				if ( vp !=null)
				{
					manager.beginTransaction().remove(vp).commit();
					manager.popBackStack();
				}


				if (tag.equals("home")){
					HomeFragment fragment = HomeFragment.newInstance(context);
					manager
					.beginTransaction()
					.replace(R.id.content_frame, fragment)
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
							fragment.goToTab(1, null, 0);
						}
						else if (tag.equals("notice/usc")) {
							fragment.goToTab(2, null, 0);
						}
						else {
							fragment.goToTab(3, tag.split("/")[1], 0);
						}
					}
					else {
						Bundle bundle;
						fragment = NoticeTabMenuFragment.newInstance(context);
						bundle = new Bundle();
						fragment.setArguments(bundle);
						fragment.initialize(context);
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
				else if (tag.equals("link"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new SiteSuggestionListFragment())
					.commit();
					mainActivity.setTitle("주요사이트 링크");
					selected=tag;
				}
				else if (tag.equals("vote"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new VoteSurveyMainFragment())
					.commit();
					mainActivity.setTitle("Vote/Survey");
					selected=tag;
				}
				else if (tag.equals("promotion"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new PromotionFragment())
					.commit();
					mainActivity.setTitle("사업 홍보");
					selected=tag;
				}
				else if (tag.startsWith("suggestion"))
				{
					Log.i("제안정책 메뉴",tag.substring(11));
					PolicyTabMenuFragment fragment;
					try {
						fragment= (PolicyTabMenuFragment) manager.findFragmentById(R.id.content_frame);
					}
					catch (Exception e){
						fragment = null;
					}
					if (selected.equals("suggestion") && fragment != null) {
						if (tag.equals("suggestion/main")) {
							fragment.goToTab(1, null, 0);
						}
						else if (tag.equals("suggestion/best")) {
							fragment.goToTab(1, null, 0);
						}
						else if (tag.equals("suggestion/exam")) {
							fragment.goToTab(2, null, 0);
						}
						else if (tag.equals("suggestion/proc")) {
							fragment.goToTab(3, null, 0);
						}
						else if (tag.equals("suggestion/all")) {
							fragment.goToTab(4, null, 0);
						}
					}
					else {
						Bundle bundle;
						fragment = PolicyTabMenuFragment.newInstance(context);
						bundle = new Bundle();
						fragment.setArguments(bundle);
						fragment.initialize(context);
						if (tag.equals("suggestion/main")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 1);
//							bundle.putString("boardname", "best");
						}
						else if (tag.equals("suggestion/best")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 1);
//							bundle.putString("boardname", "best");
						}
						else if (tag.equals("suggestion/exam")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 2);
//							bundle.putString("boardname", "exam");
						}
						else if (tag.equals("suggestion/proc")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 3);
//							bundle.putString("boardname", "proc");
						}
						else if (tag.equals("suggestion/all")) {
							bundle = fragment.getArguments();
							bundle.putInt("position", 4);
//							bundle.putString("boardname", "all");
						}
						manager
						.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.commit();
					}
					mainActivity.setTitle("학우제안정책");
					selected = tag;
				}
				else if (tag.equals("setting"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new SettingFragment())
					.commit();
					mainActivity.setTitle("설정");
					selected=tag;
				}
				else if(tag.equals("about"))
				{
					manager
					.beginTransaction()
					.replace(R.id.content_frame, new AboutFragment())
					.commit();
					mainActivity.setTitle("총학생회 소개");
					selected=tag;
				}
				else if(tag.equals("q&a"))
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					    dialog.dismiss();     //닫기
					    }
					});
					alert.setTitle("아직 준비중입니다.");
					alert.setMessage("");
					alert.show();
				}

			}

			mainActivity.getSlidingMenu().toggle();
		}
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

}


