package org.sparcs.onestepandroid.policysuggestion;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.article.ArticleInfo;
import org.sparcs.onestepandroid.article.ArticleReadAdapter;
import org.sparcs.onestepandroid.login.LoginActivity;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.ThreadManager;
import org.sparcs.onestepandroid.util.XmlParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PolicyReadFragment extends ListFragment {
	private View view;
	private static ArrayList<ArticleInfo> articlelist;
	private static ArticleReadAdapter adapter;
	private static class SetContentHandler extends Handler
	{
		private static String s;
		private int pid;
		private View view;
		private final WeakReference<PolicyReadFragment> me;

		public SetContentHandler(PolicyReadFragment me, int pid, View view) {
			this.me= new WeakReference<PolicyReadFragment>(me);
			this.pid = pid;
			this.view = view;
		}

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.arg1==1)
			{
				Builder dlg = new AlertDialog.Builder(me.get().getActivity())
				.setTitle("Connection Failed")
				.setMessage("서버와의 연결에 실패했습니다.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.show();
				me.get().getActivity().getSupportFragmentManager().beginTransaction().remove(me.get()).commit();
			}
			else
			{
				me.get().view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
				me.get().view.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
				adapter = new ArticleReadAdapter(me.get().getActivity(), articlelist, pid, view, (Fragment)me.get(),false );
				me.get().setListAdapter(adapter);
				
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.article_read, container, false);
		//getListView().setItemsCanFocus(true);
		return view;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}

	public void updateArticleView(int articleid) {
		view = getView();
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final int articleid = this.getArguments().getInt("articleid");
		final SetContentHandler handler = new SetContentHandler(PolicyReadFragment.this,articleid,view);
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				Log.i("", "caching");
				int flag=0;
				NetworkReturning returning = NetworkManager.INSTANCE.getArticleInfo(articleid, null);
				int status = returning.getStatus();

				if (!(status == 500)) // no internal error
				{
					if (status == 401) {
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
						returning = NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						if (returning.getStatus() != 200) {
							Intent intent = new Intent(PolicyReadFragment.this.getActivity(), LoginActivity.class);
							PolicyReadFragment.this.getActivity().startActivity(intent);
							return;
						}
						returning = NetworkManager.INSTANCE.getArticleInfo(articleid, null);
						status = returning.getStatus();
					}

					if (status == 200) {
						XmlParser parser = new XmlParser();
						articlelist = parser.parseArticleInfo(returning.getResponse());
						flag=1;
					}
				}
				handler.sendEmptyMessage(flag);

			}
		};
		ThreadManager.INSTANCE.getPoolExcecutor().execute(runnable);
	}
	
}
