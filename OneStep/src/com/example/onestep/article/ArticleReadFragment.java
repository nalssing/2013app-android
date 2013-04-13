package com.example.onestep.article;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.onestep.R;
import com.example.onestep.login.LoginActivity;
import com.example.onestep.util.NetworkManager;
import com.example.onestep.util.NetworkReturning;
import com.example.onestep.util.XmlParser;

public class ArticleReadFragment extends ListFragment {
	private View view;
	private static ArrayList<ArticleInfo> articlelist;
	private static ArticleReadAdapter adapter;
	private static class SetContentHandler extends Handler
	{
		private static String s;
		private final WeakReference<ArticleReadFragment> me;

		public SetContentHandler(ArticleReadFragment me) {
			this.me= new WeakReference<ArticleReadFragment>(me);
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
				adapter = new ArticleReadAdapter(me.get().getActivity(), articlelist);
				me.get().setListAdapter(adapter);
				
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.article_read, container, false);
		final int articleid = this.getArguments().getInt("articleid");
		final String boardname = this.getArguments().getString("boardname");
		final SetContentHandler handler = new SetContentHandler(ArticleReadFragment.this);
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i("", "caching");
				int flag=0;
				NetworkReturning returning = NetworkManager.INSTANCE.getArticleInfo(articleid, boardname);
				int status = returning.getStatus();

				if (!(status == 500)) // no internal error
				{
					if (status == 401) {
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
						returning = NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						if (returning.getStatus() != 200) {
							Intent intent = new Intent(ArticleReadFragment.this.getActivity(), LoginActivity.class);
							ArticleReadFragment.this.getActivity().startActivity(intent);
							return;
						}
						returning = NetworkManager.INSTANCE.getArticleInfo(articleid, "student-notice");
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
		});
		thread.start();

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

}
