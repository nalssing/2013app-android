package com.example.onestep.noti;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onestep.MainActivity;
import com.example.onestep.R;
import com.example.onestep.menu.MenuFragment;
import com.example.onestep.noticeBoard.NoticeTabMenuFragment;
import com.example.onestep.util.DBHelper;
import com.example.onestep.util.Values;

public class NotiFragment extends ListFragment{
	ArrayList<NotiItem> items = new ArrayList<NotiItem>();
	NotiAdapter adapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new NotiAdapter(this.getActivity(), items);
		setListAdapter(adapter);
		refresh();
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
				fragment = new NoticeTabMenuFragment();
				bundle = new Bundle();
				fragment.setArguments(bundle);
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
			((MainActivity)this.getActivity()).setTitle("°øÁö");
			
		}
		((MainActivity)this.getActivity()).getSlidingMenu().toggle();
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBHelper.INSTANCE.initialize(NotiFragment.this.getActivity());
				DBHelper.INSTANCE.check(item.getID());
			}
		}).start();
	}

	public void refresh() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBHelper.INSTANCE.initialize(NotiFragment.this.getActivity());
				adapter.getList().clear();
				adapter.getList().addAll(DBHelper.INSTANCE.getNotiItems());
				final int count = DBHelper.INSTANCE.unCheckedCount();
				try {
					NotiFragment.this.getView().post(new Runnable() {
						@Override
						public void run() {
							adapter.notifyDataSetChanged();
							((TextView)NotiFragment.this.getActivity().findViewById(R.id.noti_count))
							.setText(String.valueOf(count));
						}
					});
				}
				catch (Exception e){
					e.printStackTrace();
					//Log.w(Values.INSTANCE.tag, e.toString());
				}
			}
		}).start();
	}


}
