package org.sparcs.onestepandroid.article;

import java.lang.ref.WeakReference;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class ArticleWriteFragment extends Fragment {
	private View view;
	private static ProgressDialog progDig;
	private static class UploadContentHandler extends Handler
	{
		private final WeakReference<ArticleWriteFragment> me;
		
		public  UploadContentHandler(ArticleWriteFragment articleWriteFragment) {
			this.me= new WeakReference<ArticleWriteFragment>(articleWriteFragment);
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			FragmentActivity context = me.get().getActivity();
			switch(msg.arg1) {
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
				progDig.dismiss();
				break;
			case 1:
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
				progDig.dismiss();
				context.getSupportFragmentManager().beginTransaction().remove(me.get()).commit();
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.article_write, container, false);
		ImageView button = (ImageView) view.findViewById(R.id.article_write_icon_upload);
		if (button!=null){
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final UploadContentHandler handler = new UploadContentHandler(ArticleWriteFragment.this);
				EditText viewtitle = (EditText) view.findViewById(R.id.article_write_title);
				final String title = viewtitle.getText().toString();
				EditText viewcontent = (EditText) view.findViewById(R.id.article_write_contents);
				final String content = viewcontent.getText().toString();
				final Thread thread = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						NetworkReturning returning = NetworkManager.INSTANCE.writeArticle("Notice",title,content,-1,null);
						int status = returning.getStatus();
						int flag=0;
						if (!(status == 500)) // no internal error
						{
							if (status == 401) {
								SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
								NetworkManager.INSTANCE.login(
										preference.getString("username", ""), 
										preference.getString("password", ""));
								returning = NetworkManager.INSTANCE.writeArticle("Notice",title,content,-1,null);
								status = returning.getStatus();
							}

							if (status == 200) flag=1;
						}
						handler.sendEmptyMessage(flag);
					}
				});
				thread.start();
				progDig = new ProgressDialog(getActivity());
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
		
		return view;
	}
}
