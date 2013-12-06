package org.sparcs.onestepandroid.login;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import org.sparcs.onestepandroid.MainActivity;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity {
	
	static ProgressDialog progDig;
	
	private static class LoginHandler extends Handler {
		private final WeakReference<LoginActivity> activity;
		private boolean isNotiOpened;
		public LoginHandler(LoginActivity activity, boolean isNotiOpened) {
			this.activity = new WeakReference<LoginActivity>(activity);
			this.isNotiOpened = isNotiOpened;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.arg1) {
			case 0:
				Builder dlg = new AlertDialog.Builder(activity.get())
				.setTitle("Login Failed")
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
				activity.get().findViewById(R.id.login_button).setClickable(true);
				break;
			case 1:
				dlg = new AlertDialog.Builder(activity.get())
				.setTitle("Login Failed")
				.setMessage("ID 또는 Password가 일치하지 않습니다.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.show();
				progDig.dismiss();
				activity.get().findViewById(R.id.login_button).setClickable(true);
				break;
			case 2:
				Intent intent = new Intent(activity.get(), MainActivity.class);
				intent.putExtra("fromNoti", isNotiOpened);
				activity.get().startActivity(intent);
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
		    File filename = new File(Environment.getExternalStorageDirectory()+"/onestep_log.txt"); 
		    filename.createNewFile(); 
		    String cmd = "logcat -d -f "+filename.getAbsolutePath() + " AndroidRuntime:W com.example.onestep:W *:S";
		    Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		NetworkManager.INSTANCE.registerSharedPreference(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.login_button);
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isNotiOpened;
				try {
					isNotiOpened = getIntent().getBooleanExtra("fromNoti", false);
				}
				catch (Exception e){
					isNotiOpened = false;
				}
				final LoginHandler loginHandler = new LoginHandler(LoginActivity.this, isNotiOpened);
				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						
						TextView idTv = (TextView)findViewById(R.id.input_id);
						TextView pwTv = (TextView)findViewById(R.id.input_password);
						
						NetworkReturning returning =
								NetworkManager.INSTANCE.login(idTv.getText().toString(), pwTv.getText().toString());
						Message msg = loginHandler.obtainMessage();
						int responseStatus = returning.getStatus();
						if (Thread.interrupted()) {
							return;
						}
						else if(responseStatus == 500) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								Log.e("login", e.toString());
							}
							returning =
									NetworkManager.INSTANCE.login(idTv.getText().toString(), pwTv.getText().toString());
							msg = loginHandler.obtainMessage();
							responseStatus = returning.getStatus();
						}
						
						if(responseStatus == 500) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								Log.e("login", e.toString());
							}
							returning =
									NetworkManager.INSTANCE.login(idTv.getText().toString(), pwTv.getText().toString());
							msg = loginHandler.obtainMessage();
							responseStatus = returning.getStatus();
						}
						
						if(responseStatus == 401) {
								msg.arg1 = 1;
						}
						else if(responseStatus == 200) {
							msg.arg1 = 2;
						}
						else  {
							msg.arg1 = 0;
						}
						loginHandler.sendMessage(msg);
						
					}
				});
				thread.start();
				progDig = new ProgressDialog(LoginActivity.this);
				progDig.setCancelable(true);
				progDig.setMessage("Trying to Login..");
				progDig.show();
				progDig.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						thread.interrupt();
					}
				});
			}
		});
		EditText editPassword = (EditText) findViewById(R.id.input_password);
		editPassword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if(arg1 == EditorInfo.IME_ACTION_DONE){
					ImageButton imageButton = (ImageButton)findViewById(R.id.login_button);
					imageButton.performClick();
				}
				return false;
			}

		});
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String username = preference.getString("username", null);
		String password = preference.getString("password", null);
		if (username!=null && password!=null) {
			((TextView)findViewById(R.id.input_id)).setText(username);
			((TextView)findViewById(R.id.input_password)).setText(password);
			imageButton.performClick();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progDig != null)
			progDig.dismiss();
	}

}
