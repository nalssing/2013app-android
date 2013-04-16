package com.example.onestep;

import android.R.menu;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.onestep.home.HomeFragment;
import com.example.onestep.menu.MenuFragment;
import com.example.onestep.noti.NotiFragment;
import com.example.onestep.util.MyCache;
import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;

public class MainActivity extends FragmentActivity
{
	private SlidingMenu sm;
	private HomeFragment homeFragment;
	private NotiFragment notiFragment;
	private MenuFragment menuFragment;
	
	public static class MainHandler extends Handler {
		private FragmentActivity context;
		public MainHandler(FragmentActivity fragmentActivity) {
			this.context = fragmentActivity;
		}
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0) {
				((MainActivity)context).screenOff();
			}
			else {
				((MainActivity)context).screenOn();
			}
		}

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("activity", "onCreate");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, "556333818024");
		}
		else {
			Log.i("", "aleady registered");
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		findViewById(R.id.content_frame).setVisibility(View.INVISIBLE);
		setTitle("Ȩ");
		if (homeFragment == null)
			homeFragment = new HomeFragment();
		MyCache.INSTANCE.getCache().put("home", homeFragment);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, homeFragment)
		.commit();

		//setBehindContentView(R.layout.menu_frame);


		sm = new SlidingMenu(this);
		sm.setMenu(R.layout.menu_frame);
		sm.setSecondaryMenu(R.layout.noti_frame);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setSecondaryShadowDrawable(R.drawable.shadow_secondary);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		if (getIntent().getBooleanExtra("fromNoti", false))
			sm.showSecondaryMenu();
		sm.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				if (true) {
					((NotiFragment)getSupportFragmentManager().findFragmentById(R.id.noti_frame)).refresh();
				}
			}
		});
		if (menuFragment == null) {
			menuFragment = new MenuFragment();
		}
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame,menuFragment)
		.commit();
		
		if (notiFragment == null) {
			notiFragment = new NotiFragment();
		}
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.noti_frame, notiFragment)
		.commit();
		View menuCallButton = findViewById(R.id.menu_button);
		menuCallButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sm.toggle();
			}
		});
		View notiCallButton = findViewById(R.id.noti_count);
		notiCallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sm.isSecondaryMenuShowing()) {
					sm.toggle();
				}
				else {
					sm.showSecondaryMenu();
				}
			}
		});

	}

	public void setTitle(String title) {
		((TextView)findViewById(R.id.main_header)).setText(title);
	}
	public void screenOn() {
		findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
		findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
	}
	public void screenOff() {
		findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		findViewById(R.id.content_frame).setVisibility(View.INVISIBLE);
	}

	public SlidingMenu getSlidingMenu() {
		return sm;
	}

	@Override
	protected void onStop() {
		Log.i("activity", "onStop");
		super.onStop();
		finish();
	}

	


}
