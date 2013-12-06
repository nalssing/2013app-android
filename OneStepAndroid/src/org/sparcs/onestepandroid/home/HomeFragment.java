package org.sparcs.onestepandroid.home;

import java.util.ArrayList;

import org.sparcs.onestepandroid.MainActivity.MainHandler;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.article.ArticleInfo;
import org.sparcs.onestepandroid.article.ArticleListInfo;
import org.sparcs.onestepandroid.article.ArticleReadFragment;
import org.sparcs.onestepandroid.policysuggestion.PolicyReadFragment;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.ThreadManager;
import org.sparcs.onestepandroid.util.XmlParser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends ListFragment {
	
	private Context context;
	private ArrayList<HomeArticle> list;
	public HomeFragment() {
		super();

	}
	public static HomeFragment newInstance(Context context) {
		HomeFragment instance = new HomeFragment();
		instance.setRetainInstance(true);
		instance.initialize(context);
		return instance;
	}

	public void initialize(Context context) {
		this.context = context;
		list = new ArrayList<HomeArticle>();
		// 임시로 리스트 작성
		HomeArticle article;
		article = new HomeArticle(HomeArticle.Type.SECTION_HEADER, "이벤트", null, null, 0,0);
		list.add(article);
		//article = new HomeArticle(HomeArticle.Type.SINGLE_LINE, "진행중인 이벤트가 없습니다.", null, null, 0,0);
		//list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECTION_HEADER, "학우제안정책", null, null, 0,0);
//		list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECONDARY_LINE, "여학우는 반드시 전산학을 전공하도록 하자", "1362034832000", "정창제", 2147483647);
//		list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECONDARY_LINE, "여학우는 필요없다.", "1362034832000", "이근홍", 0);
//		list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECONDARY_LINE, "여자를 만나고싶다.", "1362034832000", "김정민", 1818);
//		list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECONDARY_LINE, "여자로 시작하는 말은 여우 여시 여행", "1362034832000", "채종욱", 231);
//		list.add(article);
//		article = new HomeArticle(HomeArticle.Type.SECONDARY_LINE, "적을 말 없다.", "1362034832000", "최낙현", 1);
//		list.add(article);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("fragment", "onCreateView");
		return inflater.inflate(R.layout.article_list, null);
	}
	@Override
	public void onResume() {
		super.onResume();
		list = new ArrayList<HomeArticle>();
		// 임시로 리스트 작성
		HomeArticle article;
		article = new HomeArticle(HomeArticle.Type.SECTION_HEADER, "이벤트", null, null, 0,0);
		list.add(article);
		final MainHandler handler = new MainHandler(getActivity());
		final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.arg1 = 0;
				handler.sendMessage(msg);
				ArrayList<ArticleListInfo> eventparsed = new ArrayList<ArticleListInfo>();
				
				NetworkReturning returning = NetworkManager.INSTANCE.getArticleList("Event", 1, 1, "Event");
				int status = returning.getStatus();
				if (status == 500) {
					//pass
				}
				else {
					if (status == 401) {
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
						returning = NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						returning = NetworkManager.INSTANCE.getArticleList("Event", 1, 1, "Event");
						status = returning.getStatus();
					}

					if (status == 200) {
						XmlParser parser = new XmlParser();
						eventparsed = parser.parseArticleListInfo(returning.getResponse());
					}
				}
				////////////////////////
				
				
				ArrayList<ArticleListInfo> policyparsed = new ArrayList<ArticleListInfo>();
				returning = NetworkManager.INSTANCE.getListPolicies(1, 5,"Normal");
				status = returning.getStatus();
				if (status == 500) {
					//pass
				}
				else {
					if (status == 401) {
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
						returning = NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						returning = NetworkManager.INSTANCE.getListPolicies(1, 5,"all");
						status = returning.getStatus();
					}

					if (status == 200) {
						XmlParser parser = new XmlParser();
						policyparsed = parser.parseArticleListInfo(returning.getResponse());
					}
				}
				Log.i("home:loading policy",Integer.toString(policyparsed.size()));
				////////////////////////////////////
				
				ArrayList<ArticleListInfo> noticeparsed = new ArrayList<ArticleListInfo>();
				returning = NetworkManager.INSTANCE.getArticleList("student-notice", 1, 5, "portal");
				status = returning.getStatus();
				if (status == 500) {
					//pass;
				}
				else {
					if (status == 401) {
						NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						returning = NetworkManager.INSTANCE.getArticleList("student-notice", 1, 5, "portal");
						status = returning.getStatus();
					}
					if (status == 200) {
						XmlParser parser = new XmlParser();
						try {
							noticeparsed.addAll(parser.parseArticleListInfo(returning.getResponse()));
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				}
				Log.i("home:loading notice",Integer.toString(policyparsed.size()));

				final ArrayList<ArticleListInfo> finalevent = eventparsed;
				final ArrayList<ArticleListInfo> finalpolicy = policyparsed;
				final ArrayList<ArticleListInfo> finalnotice = noticeparsed;
				Handler uiHandler = new Handler(Looper.getMainLooper()) {
					@Override
					public void handleMessage(Message msg) {
						if ( finalevent.size()==0)
						{
							list.add(new HomeArticle(HomeArticle.Type.SINGLE_LINE, "진행중인 이벤트가 없습니다.", null, null, 0,0));
						}
						else
						{
							list.add(new HomeArticle(HomeArticle.Type.EVENT_LINE, finalevent.get(0).getTitle(), null, null, 0,finalevent.get(0).getId()));
						}
						list.add(new HomeArticle(HomeArticle.Type.SECTION_HEADER, "학우제안정책", null, null, 0,0));
						
						for (ArticleListInfo item : finalpolicy) {
							HomeArticle article = new HomeArticle(
									HomeArticle.Type.POLICY_LINE,
									item.getTitle(), 
									item.getTime(), 
									item.getWriter(), 
									item.getHit(),
									item.getId());
							list.add(article);
						}
						list.add(new HomeArticle(HomeArticle.Type.SECTION_HEADER, "공지사항", null, null, 0,0));
						for (ArticleListInfo item : finalnotice) {
							HomeArticle article = new HomeArticle(
									HomeArticle.Type.NOTICE_LINE,
									item.getTitle(), 
									item.getTime(), 
									item.getWriter(), 
									item.getHit(),
									item.getId());
							list.add(article);
						}
						HomeAdapter adapter = new HomeAdapter(context, list);
						setListAdapter(adapter);
					}

				};
				uiHandler.sendEmptyMessage(0);
				msg = handler.obtainMessage();
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
		};
		ThreadManager.INSTANCE.getPoolExcecutor().execute(runnable);

		Log.i("fragment", "onCreatViewEnd");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//super.onListItemClick(l, v, position, id);
		HomeArticle article = (HomeArticle) l.getItemAtPosition(position);
		if (article.getType()==HomeArticle.Type.POLICY_LINE)
		{
			goToPolicy(article.getArticleid());
		}
		else if (article.getType()==HomeArticle.Type.NOTICE_LINE)
		{
			goToNotice(article.getArticleid());
		}
		else if (article.getType()==HomeArticle.Type.EVENT_LINE)
		{
			goToEvent(article.getArticleid());
		}
	}
	
	public void goToNotice(int articleID) {
		Log.i("home", "gotoarticle");
		Bundle args = new Bundle();
		args.putInt("articleid", articleID);
		args.putString("boardname","student-notice");
		ArticleReadFragment fragment = new ArticleReadFragment();
		fragment.setArguments(args);
		FragmentManager manager = getFragmentManager();
		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		if (getActivity() != null && getActivity().findViewById(R.id.content_frame) != null) {
			manager
			.beginTransaction()
			//.add(R.id.content_frame, fragment, "readhome")
			.replace(R.id.content_frame, fragment, "readarticle")
			.addToBackStack(null)
			.commit();
		}
	}
	public void goToPolicy(int articleID) {
		Log.i("home", "gotoarticle");
		Bundle args = new Bundle();
		args.putInt("articleid", articleID);
		args.putString("boardname","all");
		PolicyReadFragment fragment = new PolicyReadFragment();
		fragment.setArguments(args);
		FragmentManager manager = getFragmentManager();
		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		if (getActivity() != null && getActivity().findViewById(R.id.content_frame) != null) {
			manager
			.beginTransaction()
			//.add(R.id.content_frame, fragment, "readhome")
			.replace(R.id.content_frame, fragment, "readpolicy")
			.addToBackStack(null)
			.commit();
		}
	}
	public void goToEvent(int articleID)
	{
		final int ID = articleID;
		final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				NetworkReturning returning = NetworkManager.INSTANCE.getArticleInfo(ID,null);
				int status = returning.getStatus();
				if (status == 500) {
					//pass;
				}
				else {
					if (status == 401) {
						NetworkManager.INSTANCE.login(
								preference.getString("username", ""), 
								preference.getString("password", ""));
						returning = NetworkManager.INSTANCE.getArticleInfo(ID,null);
						status = returning.getStatus();
					}
					if (status == 200) {
						XmlParser parser = new XmlParser();
						ArticleInfo info = parser.parseArticleInfo(returning.getResponse()).get(0);
						Message msg = new Message();
						msg.what=0;
						msg.obj=info.getContent();
						eventhandler.sendMessage(msg);
					}
				}
			}
		});
		thread.start();
	}
	
	final Handler eventhandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what==0)
			{
				String url = (String)msg.obj;
				if (!url.startsWith("http://")) url = "http://" + url;
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			}
		}
	};
}
