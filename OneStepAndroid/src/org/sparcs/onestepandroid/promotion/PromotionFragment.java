package org.sparcs.onestepandroid.promotion;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.article.ArticleInfo;
import org.sparcs.onestepandroid.article.ArticleListInfo;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PromotionFragment extends Fragment {
	private View view;
	private LayoutInflater inflater;
	private LinearLayout content;
	private LinearLayout list;
	private ArrayList<PromotionInfo> data;
	private int off=0;
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			PromotionInfo item = data.get(off);
			Log.i("invalidate",Integer.toString(off));
			TextView subject = (TextView) view.findViewById(R.id.promotion_main_subject);
			subject.setText(item.getTitle());
			ProgressBar bar = (ProgressBar) view.findViewById(R.id.promotion_main_progressbar);
			bar.setProgress(item.getProgress());
			ImageView img = (ImageView) view.findViewById(R.id.promotion_main_image);
			img.setImageBitmap(item.getPicture());
			img.invalidate();
			TextView num = (TextView) view.findViewById(R.id.promotion_main_progress_description);
			num.setText(Integer.toString(item.getProgress())+"% done.");
			
			for (View v : item.getBody())
			{
				content.addView(v);
			}
			int len = data.size();
			
			for (int i=0;i<len;i++)
			{
				View child = inflater.inflate(R.layout.promotion_list_item, null);
				subject = (TextView) child.findViewById(R.id.promotion_list_item_subject);
				subject.setText(data.get(i).getTitle());
				img = (ImageView) view.findViewById(R.id.promotion_main_image);
				img.setImageBitmap(data.get(i).getPicture());
				child.setClickable(true);
				child.setTag(data.get(i));
				final int loc = i;
				child.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						/*PromotionInfo item = (PromotionInfo)v.getTag(); */
						
						off = loc;
						PromotionInfo item = data.get(off);
						TextView subject = (TextView) view.findViewById(R.id.promotion_main_subject);
						subject.setText(item.getTitle());
						ProgressBar bar = (ProgressBar) view.findViewById(R.id.promotion_main_progressbar);
						bar.setProgress(item.getProgress());
						ImageView img = (ImageView) view.findViewById(R.id.promotion_main_image);
						img.setImageBitmap(item.getPicture());
						TextView num = (TextView) view.findViewById(R.id.promotion_main_progress_description);
						num.setText(Integer.toString(item.getProgress())+"% done.");
						content.removeAllViews();
						for (View v1 : item.getBody())
						{
							content.addView(v1);
						}
						view.scrollTo(0, 0);
					}
				});
				
				list.addView(child);
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.promotion_list, null);
		content = (LinearLayout)view.findViewById(R.id.promotion_content);
		list = (LinearLayout)view.findViewById(R.id.promotion_list);
		this.inflater = inflater;
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				NetworkReturning returning = NetworkManager.INSTANCE.getPromotion();
				Context context = getActivity();
				int status = returning.getStatus();
				data = new ArrayList<PromotionInfo>();
				//Log.i("",returning.getResponse());
				
				if (status == 500) {
					PromotionInfo info = new PromotionInfo();
					info.setTitle("서버와의 연결이 끊겼습니다.");
					info.setPicture(BitmapFactory.decodeResource(getResources(), R.drawable.project_default_image));
					info.setBody(new ArrayList<View>());
					info.setProgress(0);
					data.add(info);
				}
				else {
					if (status == 200) {
						XmlParser parser = new XmlParser();
						for (ArticleListInfo promlist: parser.parseArticleListInfo(returning.getResponse()))
						{
							returning = NetworkManager.INSTANCE.getArticleInfo(promlist.getId(),null);
							if (returning.getStatus()==200)
							{
								ArrayList<ArticleInfo> parsed = parser.parseArticleInfo(returning.getResponse());
								PromotionInfo info = new PromotionInfo();
								info.setTitle(parsed.get(0).getTitle());
								info.setBody(new ArrayList<View>());
								Bitmap mainimg = null;
								Log.i("promotion",parsed.get(0).getTitle());
								LayoutParams textParams = new LayoutParams(
										LinearLayout.LayoutParams.MATCH_PARENT,
										LinearLayout.LayoutParams.MATCH_PARENT);
								textParams.setMargins(10, 10, 10, 10);
								
								for (int i=1;i<parsed.size();i++)
								{
									ArticleInfo item = parsed.get(i);
									if (item.getTitle().equals("content"))
									{
										TextView tv = new TextView(context);
										tv.setText(item.getContent());
										//tv.setLayoutParams(textParams);
										tv.setPadding(15, 15, 15, 15);
										info.getBody().add(tv);
									}
									else if (item.getTitle().equals("image"))
									{
										int bm = Integer.parseInt(item.getContent());
										if (mainimg==null)
										{
											mainimg = NetworkManager.INSTANCE.getImage(bm);
											Log.i("loading img",Boolean.toString(mainimg == null));
											if (mainimg==null)
												mainimg = BitmapFactory.decodeResource(getActivity().getResources(),  R.drawable.project_default_image);
											info.setPicture(mainimg);
										}
										else
										{
											Log.i("main img exists","");
											ImageView img = new ImageView(context);
											Bitmap bitmap = NetworkManager.INSTANCE.getImage(bm);
											img.setImageBitmap(bitmap);
											info.getBody().add(img);
										}
									}
									else if (item.getTitle().equals("progress"))
									{
										info.setProgress(Integer.parseInt(item.getContent()));
									}
								}
								data.add(info);
							}
						}
					}
					else 
					{
						PromotionInfo info = new PromotionInfo();
						info.setTitle("서버와의 연결이 끊겼습니다.");
						info.setPicture(BitmapFactory.decodeResource(getResources(), R.drawable.project_default_image));
						info.setBody(new ArrayList<View>());
						info.setProgress(0);
						data.add(info);
					}
				}
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
				
		return view;
	}

}
