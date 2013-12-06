package org.sparcs.onestepandroid.policysuggestion;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.article.ArticleListInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class PolicySuggestionListFragment extends ListFragment {
	private View header;
	private String tab;
	private String board;
	private int articleID;
	private PolicySuggestionEndlessAdapter adapter;
	private Context context;
	
//	Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            TextView text = (TextView) view.findViewById(R.id.article_write_title);
//            text.setText(data);
//        }
//	};
	
	public PolicySuggestionListFragment() {
		super();

	}

	public static PolicySuggestionListFragment newInstance(Context context, int articleID) {
		PolicySuggestionListFragment instance = new PolicySuggestionListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("articleID", articleID);
		instance.setArguments(bundle);
		instance.initialize(context);

		return instance;
	}

	public static PolicySuggestionListFragment newInstance(Context context) {
		return newInstance(context, -1);
	}
	public void initialize(Context context) {
		this.context = context;
		Bundle bundle = getArguments();
		articleID = bundle.getInt("articleID");
		tab = bundle.getString("tab");
		board = bundle.getString("board");
		if (articleID == 0)
			articleID = -1;
		Log.i("articleID", String.valueOf(articleID));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.article_list, null);
		header = inflater.inflate(R.layout.policy_suggestion_list_header, null);
		header.setClickable(false);
		ImageView write = (ImageView) header.findViewById(R.id.policy_suggestion_write_button);
		
		write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("PolicySuggestion","Write button click");
				PolicyWriteFragment fragment = new PolicyWriteFragment();
				FragmentManager manager = getFragmentManager();
				Log.i("","loop1");
				for (int i=0; i < manager.getBackStackEntryCount(); i++) {
					manager.popBackStack();
				}
				Log.i("","loop3");
				if (getActivity() != null && getActivity().findViewById(R.id.policy_fragment_content) != null) {
					manager
					.beginTransaction()
					.add(R.id.policy_fragment_content, fragment, "writepolicy")
					.addToBackStack(null)
					.commit();
				}
			}
		});
		return view;
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (position>0)
		{
			ArticleListInfo info = (ArticleListInfo) l.getItemAtPosition(position);
			Log.i("policy menu click", info.getTitle());
			goToArticle(info.getId());
		}
		return;
	}

	public void goToArticle(int articleID) {
		Log.i("article", "gotoarticle");
		Bundle args = new Bundle();
		args.putInt("articleid", articleID);
		args.putString("boardname",tab);
		PolicyReadFragment fragment = new PolicyReadFragment();
		fragment.setArguments(args);
		FragmentManager manager = getFragmentManager();
		Log.i("","loop1");
		for (int i=0; i < manager.getBackStackEntryCount(); i++) {
			manager.popBackStack();
		}
		Log.i("","loop3");
		if (getActivity() != null && getActivity().findViewById(R.id.policy_fragment_content) != null) {
			manager
			.beginTransaction()
			.add(R.id.policy_fragment_content, fragment, "readpolicy")
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
			adapter = new PolicySuggestionEndlessAdapter(getActivity(), wrapperList,board);
		}
		if ( getListView().getHeaderViewsCount()==0 )
		{
			setListAdapter(null);
			getListView().addHeaderView(header);
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
