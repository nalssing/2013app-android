package com.example.onestep.login;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.onestep.MainActivity;
import com.example.onestep.R;
import com.example.onestep.util.NetworkManager;
import com.example.onestep.util.NetworkReturning;

public class LoginActivity extends Activity {
	
	static ProgressDialog progDig;
	
	private static class LoginHandler extends Handler {
		private final WeakReference<LoginActivity> activity;
		
		public LoginHandler(LoginActivity activity) {
			this.activity = new WeakReference<LoginActivity>(activity);
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
				activity.get().startActivity(intent);
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NetworkManager.INSTANCE.registerSharedPreference(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.login_button);
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final LoginHandler loginHandler = new LoginHandler(LoginActivity.this);
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
						else if(responseStatus == 401) {
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
