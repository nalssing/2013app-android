package org.sparcs.onestepandroid.article;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.Values;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleReadAdapter extends ArrayAdapter<ArticleInfo> {
	private LayoutInflater inflater;
	private Context mContext;
	final private UploadContentHandler handler;
	final int pid;
	final private View view;
	private boolean isArticle;
	
	public ArticleReadAdapter(Context context,  ArrayList<ArticleInfo> objects,
			int pid, View view, Fragment fr, boolean isNotice) {
		super(context, 0, 0, objects);
		mContext = context;
		inflater = LayoutInflater.from(context);
		handler = new UploadContentHandler(fr);
		this.view = view;
		this.pid = pid;
		isArticle = isNotice; 
	}

	class ViewHolder {
		TextView header;
		TextView title;
		TextView author;
		TextView content;
	}
	
	private static ProgressDialog progDig;
	private static class UploadContentHandler extends Handler
	{
		private final WeakReference<Fragment> me;
		
		public  UploadContentHandler(Fragment fr) {
			this.me= new WeakReference<Fragment>(fr);
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			FragmentActivity context = me.get().getActivity();
			switch(msg.what) {
			case 0:
				Builder dlg = new AlertDialog.Builder(context)
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
				if (progDig!=null)
					progDig.dismiss();
				break;
			case 1:
				Log.i("write reply","success");
				dlg = new AlertDialog.Builder(context)
				.setTitle("Upload Complete")
				.setMessage("성공적으로 업데이트되었습니다.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.show();
				if (progDig!=null)
					progDig.dismiss();
				//context.getFragmentManager().beginTransaction().remove(me.get()).
				context.getSupportFragmentManager().beginTransaction().remove(me.get()).commit();
				break;
				
			case 2: // vote success
				Log.i("vote","success");
				ImageView button = (ImageView)msg.obj;
				button.setImageResource(R.drawable.already_like);
				button.setClickable(false);
				break;
			case 3: // vote fails
				Log.i("vote","fail");
				ImageView btn = (ImageView)msg.obj;
				btn.setImageResource(R.drawable.already_like);
				btn.setClickable(false);
				dlg = new AlertDialog.Builder(context)
				.setTitle("Already Voted")
				.setMessage("이미 추천하셨거나 추천하실 수 없는 글입니다.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.show();
				break;
			}
		}
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		ArticleInfo info = getItem(position);
		if (convertView == null) {
			if (type == 0) {
				ViewHolder holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.article_read_article, parent, false);
				holder.header = (TextView)convertView.findViewById(R.id.header);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.author = (TextView)convertView.findViewById(R.id.author);
				holder.content = (TextView)convertView.findViewById(R.id.content);
				convertView.setTag(holder);
				
				if (!this.isArticle)
				{
					ImageView like = (ImageView)convertView.findViewById(R.id.article_read_like_button);
					
					like.setOnClickListener( new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							final View view = v;
							Thread thread = new Thread (new Runnable() {
								
								@Override
								public void run() {
									NetworkReturning returning = NetworkManager.INSTANCE.SetVote(pid, "up");
									int status = returning.getStatus();
									int flag=0;
									if (!(status == 500)) // no internal error
									{
										if (status == 401) {
											SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mContext);
											NetworkManager.INSTANCE.login(
													preference.getString("username", ""), 
													preference.getString("password", ""));
											returning = NetworkManager.INSTANCE.SetVote(pid, "up");
											status = returning.getStatus();
										}
	
										if (status == 200) flag=2;
										if (status == 409) flag=3;
									}
									Log.i("policy vote",Integer.toString(status));
									Message msg = new Message();
									msg.what=flag;
									msg.obj=view;
									handler.sendMessage(msg);
								}
							});
							thread.start();
						}
					});
	
					final EditText reply = (EditText) convertView.findViewById(R.id.article_write_reply);
					final TextView button = (TextView) convertView.findViewById(R.id.article_send_reply_button);
	
					ImageView ReplyButton = (ImageView) convertView.findViewById(R.id.policy_suggestion_reply_button);
					ReplyButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							reply.setVisibility(View.VISIBLE);
							button.setVisibility(View.VISIBLE);
						}
					});
					
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							
							final String content = reply.getText().toString();
							Log.i("reply write:policy", content);
							final Thread thread = new Thread(new Runnable()
							{
								@Override
								public void run()
								{
									NetworkReturning returning = NetworkManager.INSTANCE.writeArticle("Suggestion","",content,pid,"Proc");
									//NetworkReturning returning = NetworkManager.INSTANCE.writeArticle("Promotion",title,content,115,"Promotion");
									int status = returning.getStatus();
									int flag=0;
									if (!(status == 500)) // no internal error
									{
										if (status == 401) {
											SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mContext);
											NetworkManager.INSTANCE.login(
													preference.getString("username", ""), 
													preference.getString("password", ""));
											returning = NetworkManager.INSTANCE.writeArticle("Suggestion","title",content,pid,null);
											status = returning.getStatus();
										}
	
										if (status == 200) flag=1;
									}
									Log.i("policy write",Integer.toString(status));
									handler.sendEmptyMessage(flag);
								}
							});
							thread.start();
							progDig = new ProgressDialog(mContext);
							progDig.setCancelable(true);
							progDig.setMessage("Uploading..");
							progDig.show();
							progDig.setOnCancelListener(new OnCancelListener() {
								
								@Override
								public void onCancel(DialogInterface dialog) {
									thread.interrupt();
								}
							});
						}		
					});
				}
				
			}
			else {
				convertView = inflater.inflate(R.layout.article_read_reply, parent, false);
				ViewHolder holder = new ViewHolder();
				holder.header = null;
				holder.title = null;
				holder.author = (TextView)convertView.findViewById(R.id.article_reply_author);
				holder.content = (TextView)convertView.findViewById(R.id.article_reply_content);
				convertView.setTag(holder);
				//TODO 제대로 만들기
			}
		}
		ViewHolder holder = (ViewHolder)convertView.getTag();
		if (type == 0) {
			holder.header.setText(Values.INSTANCE.portalBoardsMap.get(info.getBoard()));
			holder.title.setText(info.getTitle());
			if (isArticle)
				holder.author.setText(info.getWriter().concat(" | ").concat(info.getTime()));
			else
				holder.author.setText(info.getWriter().concat(" | ").concat(info.getTime())
										.concat(" | ").concat(Integer.toString(info.getVoteUp())));
			holder.content.setText(Html.fromHtml(info.getContent()));
		}
		else {
			holder.author.setText(info.getWriter().concat(" | ").concat(info.getTime()));
			holder.content.setText(Html.fromHtml(info.getContent()));
		}
		
		
		
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getItemViewType(int position) {
		switch (getItem(position).getType()) {
		case ARTICLE:
			return 0;
		default:
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	

}
