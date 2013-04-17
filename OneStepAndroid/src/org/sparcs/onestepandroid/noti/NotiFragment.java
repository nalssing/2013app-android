package org.sparcs.onestepandroid.noti;

import java.util.ArrayList;

import org.sparcs.onestepandroid.MainActivity;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.menu.MenuFragment;
import org.sparcs.onestepandroid.noticeBoard.NoticeTabMenuFragment;
import org.sparcs.onestepandroid.util.DBHelper;
import org.sparcs.onestepandroid.util.ThreadManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class NotiFragment extends ListFragment{
	private NotiAdapter adapter;
	private Context context;
	public static NotiFragment newInstance(Context context) {
		NotiFragment instance = new NotiFragment();
		instance.initialize(context);
		return instance;
	}

	public void initialize(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.noti_list, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final NotiItem item = adapter.getItem(position);
		FragmentManager manager = getFragmentManager();
		String selected = ((MenuFragment)manager.findFragmentById(R.id.menu_frame)).getSelected();
		String boardname = item.getBoardname();
		int articleID = item.getArticleID();
		if (item.getType().equals("portal")) {
			NoticeTabMenuFragment fragment;
			try {
				fragment= (NoticeTabMenuFragment) manager
						.findFragmentById(R.id.content_frame);
			}
			catch (Exception e){
				fragment = null;
			}
			if (selected.equals("notice") && fragment != null) {
				if (boardname.equals("student-notice")) {
					fragment.goToTab(1, boardname, articleID);
				}
				else if (boardname.equals("gsc-usc-notice")) {
					fragment.goToTab(2, boardname, articleID);
				}
				else {
					fragment.goToTab(3, boardname, articleID);
				}
			}
			else {
				Bundle bundle;
				fragment = NoticeTabMenuFragment.newInstance(context);
				bundle = new Bundle();
				fragment.setArguments(bundle);
				fragment.initialize(context);
				if (boardname.equals("student-notice")) {
					bundle = fragment.getArguments();
					bundle.putInt("position", 1);
					bundle.putString("boardname", boardname);
					bundle.putInt("articleID", articleID);
				}
				else if (boardname.equals("gsc-usc-notice")){
					bundle = fragment.getArguments();
					bundle.putString("boardname", boardname);
					bundle.putInt("articleID", articleID);
				}
				else {
					bundle = fragment.getArguments();
					bundle.putInt("position", 3);
					bundle.putString("boardname", boardname);
					bundle.putInt("articleID", articleID);
				}
				manager
				.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.commit();
			}
			((MenuFragment)manager.findFragmentById(R.id.menu_frame)).setSelected("notice");
			((MainActivity)context).setTitle("°øÁö");

		}
		((MainActivity)context).getSlidingMenu().toggle();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				DBHelper.INSTANCE.initialize(context);
				DBHelper.INSTANCE.check(item.getID());
			}
		};
		ThreadManager.INSTANCE.getPoolExcecutor().execute(runnable);
	}

	public void refresh() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				DBHelper.INSTANCE.initialize(context);
				adapter.getList().clear();
				adapter.getList().addAll(DBHelper.INSTANCE.getNotiItems());
				final int count = DBHelper.INSTANCE.unCheckedCount();
				try {
					NotiFragment.this.getView().post(new Runnable() {
						@Override
						public void run() {
							adapter.notifyDataSetChanged();
							((TextView)((Activity)context).findViewById(R.id.noti_count))
							.setText(String.valueOf(count));
						}
					});
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		};
		ThreadManager.INSTANCE.getPoolExcecutor().execute(runnable);
	}

	@Override
	public void onResume() {
		super.onResume();
		ArrayList<NotiItem> items = new ArrayList<NotiItem>();
		adapter = new NotiAdapter(context, items);
		setListAdapter(adapter);
		refresh();
	}


}
