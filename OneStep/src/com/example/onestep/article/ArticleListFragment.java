package com.example.onestep.article;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.onestep.R;

public class ArticleListFragment extends ListFragment {
	private String boardname;
	private String type;
	private ArticleEndlessAdapter adapter;

	onArticleListItemSelectedListener mCallback;
	public interface onArticleListItemSelectedListener 	{
		public void onArticleListItemSelected(int articleid);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            mCallback = (onArticleListItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
	}
		
	
	public ArticleListFragment() {
		super();
		this.boardname = "";
		this.type = "";
	}

	public void initialize() {
		Bundle bundle = getArguments();
		boardname = bundle.getString("boardname");
		type = bundle.getString("type");
		if (boardname == null)
			boardname = "";
		if (type == null)
			type = "";
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("", "redraw article list");
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.article_list, null);
		if (adapter == null){
			ArrayList<ArticleListInfo> wrapperList = null;
				wrapperList = getActivity().getIntent().getParcelableArrayListExtra(boardname + "wrapperList");
			if (wrapperList == null)
				wrapperList = new ArrayList<ArticleListInfo>();
			adapter = new ArticleEndlessAdapter(getActivity(), wrapperList, boardname, type);
		}
		setListAdapter(adapter);
		return view;
	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		getActivity().getIntent().putExtra(boardname + "wrapperList", adapter.wrapperList);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i("save", "save");
		outState.putParcelableArrayList(boardname + "wrapperList", adapter.wrapperList);
		outState.putInt(boardname + "size", adapter.wrapperList.size());

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ArticleListInfo info = (ArticleListInfo) l.getItemAtPosition(position);
		
		//mCallback.onArticleListItemSelected(info.getId());
		
		Bundle args = new Bundle();
		args.putInt("articleid", info.getId());
		args.putString("boardname", boardname);
		ArticleReadFragment fragment = new ArticleReadFragment();
		fragment.setArguments(args);
		FragmentManager manager = getFragmentManager();
		while (manager.getBackStackEntryCount() != 0) {
			manager.popBackStack();
		}
		manager
		.beginTransaction()
		.add(R.id.fragment_content,fragment, "readarticle")
		.addToBackStack(null)
		.commit();
		return;
	}
}
