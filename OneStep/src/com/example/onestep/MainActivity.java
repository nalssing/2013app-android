package com.example.onestep;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.onestep.article.ArticleListFragment;
import com.example.onestep.article.ArticleReadFragment;
import com.example.onestep.home.HomeFragment;
import com.example.onestep.menu.MenuFragment;
import com.example.onestep.util.MyCache;
import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity
implements ArticleListFragment.onArticleListItemSelectedListener
{
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
		HomeFragment fragment = new HomeFragment();
		MyCache.INSTANCE.getCache().put("home", fragment);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame,fragment)
		.commit();
		
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment())
		.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		View menuCallButton = findViewById(R.id.menu_button);
		menuCallButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	
	@Override
	public void onArticleListItemSelected(int articleid) {
		// TODO Auto-generated method stub
		ArticleReadFragment articleview = (ArticleReadFragment) getSupportFragmentManager().findFragmentByTag("readarticle");
		if (articleview != null) {
			articleview.updateArticleView(articleid);
		} else {
			ArticleReadFragment newFragment = new ArticleReadFragment();
			Bundle args = new Bundle();
			args.putInt("articleid", articleid);
			newFragment.setArguments(args);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_content, newFragment,"readarticle");
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
