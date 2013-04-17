package org.sparcs.onestepandroid.article;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class ArticleListFragment extends ListFragment {
	private String boardname;
	private String type;
	private int articleID;
	private ArticleEndlessAdapter adapter;
	private Context context;

	public ArticleListFragment() {
		super();

	}

	public static ArticleListFragment newInstance(Context context, String boardname
			,String type, int articleID) {
		ArticleListFragment instance = new ArticleListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("boardname", boardname);
		bundle.putString("type", type);
		bundle.putInt("articleID", articleID);
		instance.setArguments(bundle);
		instance.initialize(context);

		return instance;
	}

	public static ArticleListFragment newInstance(Context context, String boardname, String type) {
		return newInstance(context, boardname, type, -1);
	}

	public static ArticleListFragment newInstance(Context context) {
		return newInstance(context, "", "");
	}
	public void initialize(Context context) {
		this.context = context;
		Bundle bundle = getArguments();
		boardname = bundle.getString("boardname");
		type = bundle.getString("type");
		articleID = bundle.getInt("articleID");
		if (boardname == null)
			boardname = "";
		if (type == null)
			type = "";
		if (articleID == 0)
			articleID = -1;
		Log.i("articleID", String.valueOf(articleID));
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
		ArticleListInfo info = (ArticleListInfo) l.getItemAtPosition(position);
		goToArticle(info.getId());
		return;
	}

	public void goToArticle(int articleID) {
		Log.i("article", "gotoarticle");
		Bundle args = new Bundle();
		args.putInt("articleid", articleID);
		args.putString("boardname", boardname);
		ArticleReadFragment fragment = new ArticleReadFragment();
		fragment.setArguments(args);
		FragmentManager manager = getFragmentManager();
		Log.i("","loop1");
		for (int i=0; i < manager.getBackStackEntryCount(); i++) {
			manager.popBackStack();
		}
		Log.i("","loop3");
		if (getActivity() != null && getActivity().findViewById(R.id.fragment_content) != null) {
			manager
			.beginTransaction()
			.add(R.id.fragment_content, fragment, "readarticle")
			.addToBackStack(null)
			.commit();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("articlefragment","onResume");
		if (adapter == null){
			ArrayList<ArticleListInfo> wrapperList = new ArrayList<ArticleListInfo>();
			adapter = new ArticleEndlessAdapter(getActivity(), wrapperList, boardname, type);
		}
		setListAdapter(adapter);
		if (articleID > 0) {
			Log.i("fragment,articleId", String.valueOf(articleID));
			Log.i("", "gotoarticle articleid");
			goToArticle(articleID);
			articleID = -1;
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("articlefragment", "onStop");
		articleID = -1;
		getArguments().putInt("articleID", -1);

	}
}
