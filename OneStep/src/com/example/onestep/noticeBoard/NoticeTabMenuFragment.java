package com.example.onestep.noticeBoard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onestep.R;
import com.example.onestep.article.ArticleListFragment;
import com.example.onestep.article.ArticleReadFragment;

public class NoticeTabMenuFragment extends Fragment {
	private int selectedPosition;
	private Bundle bundle;
	private ArticleListFragment tab1;
	private ArticleListFragment tab2;
	private BoardListFragment tab3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.bundle = getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("","recreate view of noticetab");
		View v = inflater.inflate(R.layout.tab_fragment, null);

		Button view = (Button) v.findViewById(R.id.top_bar_tab1);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleReadFragment article = (ArticleReadFragment) getFragmentManager().findFragmentByTag("readarticle");
				if ( article!=null)
				{
					getFragmentManager().beginTransaction().remove(article).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(1, null, 0);
			}
		});

		view = (Button) v.findViewById(R.id.top_bar_tab2);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleReadFragment article = (ArticleReadFragment) getFragmentManager().findFragmentByTag("readarticle");
				if ( article!=null)
				{
					getFragmentManager().beginTransaction().remove(article).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(2, null, 0);
			}
		});

		view = (Button) v.findViewById(R.id.top_bar_tab3);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleReadFragment article = (ArticleReadFragment) getFragmentManager().findFragmentByTag("readarticle");
				if ( article!=null)
				{
					getFragmentManager().beginTransaction().remove(article).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(3, null, 0);
			}
		});
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		goToTab(bundle.getInt("position"), bundle.getString("boardname"), bundle.getInt("articleID"));
	}

	public void goToTab(int position, String boardname, int articleID) {
		View selectedTab;
		View view;
		switch(selectedPosition) {
		case 1:
			selectedTab = getView().findViewById(R.id.top_bar_tab1);
			break;
		case 2:
			selectedTab = getView().findViewById(R.id.top_bar_tab2);
			break;
		default:
			selectedTab = getView().findViewById(R.id.top_bar_tab3);
		}
		if (position == 1) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			if (tab1 == null)
				tab1 = new ArticleListFragment();
			bundle = new Bundle();
			bundle.putString("boardname", "student-notice");
			bundle.putString("type", "portal");
			bundle.putInt("articleID", articleID);
			tab1.setArguments(bundle);
			tab1.initialize();
			view = getView().findViewById(R.id.top_bar_tab1);
			view.setSelected(true);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_content, tab1);
			ft.commit();
		}
		else if (position == 2) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			view = getView().findViewById(R.id.top_bar_tab2);
			view.setSelected(true);
			if (tab2 == null)
				tab2 = new ArticleListFragment();
			bundle = new Bundle();
			bundle.putString("boardname", "gsc-usc-notice");
			bundle.putString("type", "portal");
			bundle.putInt("articleID", articleID);
			tab2.setArguments(bundle);
			tab2.initialize();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_content, tab2);
			ft.commit();
		}
		else {
			selectedPosition = position;
			selectedTab.setSelected(false);
			view = getView().findViewById(R.id.top_bar_tab3);
			view.setSelected(true);
			if (tab3 == null)
				tab3 = new BoardListFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_content, tab3);
			ft.commit();
			if (boardname != null) {
				tab3.goToBoard(boardname, articleID);
				view.setSelected(false);
			}
		}


	}
}
