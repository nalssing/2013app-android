package com.example.onestep.noticeBoard;

import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onestep.R;
import com.example.onestep.article.ArticleListFragment;
import com.example.onestep.util.Values;

public class BoardListFragment extends ListFragment {
	private ArrayAdapter<NameValuePair> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("", "make view");
		View view = inflater.inflate(R.layout.article_list, null);
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
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String boardname = ((NameValuePair)l.getItemAtPosition(position)).getValue();
		getActivity().findViewById(R.id.top_bar_tab3).setSelected(false);
		goToBoard(boardname, 0);
	}

	public void goToBoard(String boardname, int articleID) {
		ArticleListFragment fragment = new ArticleListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("boardname", boardname);
		bundle.putString("type", "portal");
		bundle.putInt("articleID", articleID);
		fragment.setArguments(bundle);
		fragment.initialize();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction
		.replace(R.id.fragment_content, fragment)
		.commit();
	}
	
	


}
