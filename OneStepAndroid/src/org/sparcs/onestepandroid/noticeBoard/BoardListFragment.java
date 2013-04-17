package org.sparcs.onestepandroid.noticeBoard;

import org.apache.http.NameValuePair;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.article.ArticleListFragment;
import org.sparcs.onestepandroid.util.Values;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BoardListFragment extends ListFragment {
	private ArticleListFragment articleFragment;
	private ArrayAdapter<NameValuePair> adapter;
	private Context context;
	public void initialize(Context context) {
		this.context = context;
	}
	public static BoardListFragment newInstance(Context context) {
		BoardListFragment instance = new BoardListFragment();
		instance.initialize(context);
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.article_list, null);
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String boardname = ((NameValuePair)l.getItemAtPosition(position)).getValue();
		getActivity().findViewById(R.id.top_bar_tab3).setSelected(false);
		goToBoard(boardname, 0);
	}

	public void goToBoard(String boardname, int articleID) {
		if (articleFragment == null)
			articleFragment = new ArticleListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("boardname", boardname);
		bundle.putString("type", "portal");
		bundle.putInt("articleID", articleID);
		articleFragment.setArguments(bundle);
		articleFragment.initialize(context);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction
		.replace(R.id.fragment_content, articleFragment)
		.commit();
	}
	@Override
	public void onResume() {
		super.onResume();
		if (adapter == null) {
			adapter = new ArrayAdapter<NameValuePair>(getActivity(), R.layout.article_single_line, Values.INSTANCE.portalBoards){

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View v = LayoutInflater.from(getContext()).inflate(R.layout.article_single_line, parent, false);
					((TextView)v.findViewById(R.id.textView1)).setText(getItem(position).getName());
					return v;
				}
			};
		}
		setListAdapter(adapter);
	}
	
	


}
