package org.sparcs.onestepandroid.setting;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SettingFragment extends Fragment {
	
	private Context mContext;
	private SetPushHandler pushhandler;
	private SetStatusHandler stathandler;
	private View view;
	private ArrayList<String> data;
	private boolean cancelsetting;
	
	public SettingFragment() {
		this.pushhandler = new SetPushHandler();
		this.stathandler = new SetStatusHandler();
		cancelsetting=false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mContext = getActivity();
		view = inflater.inflate(R.layout.setting_main, null);
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getPushStatus();
				int status = returning.getStatus();
				data = new ArrayList<String>();
				
				if (status == 200) {
					XmlParser parser = new XmlParser();
					data.addAll(parser.parsePushStatus(returning.getResponse()));
					stathandler.sendEmptyMessage(0);
				}
				else
					stathandler.sendEmptyMessage(1);
				
				
			}
		});
		thread.start();
			
		return view;
	}
	
	class SetStatusHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==1)
			{
				Builder dlg = new AlertDialog.Builder(mContext)
				.setTitle("Connection Failed")
				.setMessage("네트워크에 연결되어야 설정을 변경할 수 있습니다.")
				.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dlg.show();
				return;
			}
			Switch noti = (Switch) view.findViewById(R.id.setting_push_board);
			if (!data.contains("PORTAL"))
				noti.setChecked(true);

			noti.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (cancelsetting)
						return;
					final Switch s = (Switch)buttonView;
					final Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							NetworkReturning returning;
							Message msg = new Message();
							if (s.isChecked())
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(true, "PORTAL");
							}
							else
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(false, "PORTAL");	
							}
							
							msg.arg1 = returning.getStatus();
							msg.obj = view.findViewById(R.id.setting_push_board);
							pushhandler.sendMessage(msg);
						}
					});
					thread.start();
					s.setActivated(false);
				}
			});

			noti = (Switch) view.findViewById(R.id.setting_push_calender);
			if (!data.contains("CALENDAR"))
				noti.setChecked(true);
			
			noti.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (cancelsetting)
						return;
					final Switch s = (Switch)buttonView;
					final Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							NetworkReturning returning;
							Message msg = new Message();
							if (s.isChecked())
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(true, "CALENDAR");
							}
							else
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(false, "CALENDAR");	
							}
							msg.arg1 = returning.getStatus();
							msg.obj = view.findViewById(R.id.setting_push_calender);
							pushhandler.sendMessage(msg);
						}
					});
					thread.start();
					s.setActivated(false);
				}
			});
			
			noti = (Switch) view.findViewById(R.id.setting_push_vote_survey);
			if (!data.contains("SURVEY"))
				noti.setChecked(true);
			
			noti.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (cancelsetting)
						return;
					final Switch s = (Switch)buttonView;
					final Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							NetworkReturning returning;
							Message msg = new Message();
							if (s.isChecked())
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(true, "SURVEY");
							}
							else
							{
								returning = NetworkManager.INSTANCE.SetPushStatus(false, "SURVEY");	
							}
							
							msg.arg1 = returning.getStatus();
							msg.obj = view.findViewById(R.id.setting_push_vote_survey);
							pushhandler.sendMessage(msg);
						}
					});
					thread.start();
					s.setActivated(false);
				}
			});
		}
	}
	
	class SetPushHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			//progDig.dismiss();
			Log.i("setting",Integer.toString(msg.arg1));
			Switch noti = (Switch)msg.obj;
			
			if (msg.arg1!=200)
			{
				cancelsetting = true;
				noti.toggle();
				cancelsetting = false;
				
				Builder dlg = new AlertDialog.Builder(mContext)
				.setTitle("Connection Failed")
				.setMessage("서버와의 연결에 실패했습니다.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dlg.show();
			}
			
			noti.setActivated(true);
		}	
	}
}
