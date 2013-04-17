package org.sparcs.onestepandroid;

import org.sparcs.onestepandroid.home.HomeFragment;
import org.sparcs.onestepandroid.menu.MenuFragment;
import org.sparcs.onestepandroid.noti.NotiFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;

public class MainActivity extends FragmentActivity
{
	private SlidingMenu sm;

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
		Log.i("activity", "check4");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		findViewById(R.id.content_frame).setVisibility(View.INVISIBLE);
		setTitle("Ȩ");
		HomeFragment homeFragment = HomeFragment.newInstance(this);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, homeFragment)
		.commit();
		
		Log.i("activity", "check3");
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
		
		Log.i("activity", "check2");
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
		MenuFragment menuFragment = MenuFragment.newInstance(this);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame,menuFragment)
		.commit();

		NotiFragment notiFragment = NotiFragment.newInstance(this);
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
		Log.i("activity", "chck1");
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
		Log.i("activity", "endOncreate");
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
		//finish();
	}


}
