package org.sparcs.onestepandroid;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.sparcs.onestepandroid.login.LoginActivity;
import org.sparcs.onestepandroid.util.DBHelper;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.ThreadManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {
	static int num = 0;
	public GCMIntentService() {
		super("556333818024");
	}
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i("","error");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i("", "received");
		generateNotification(context, intent);

	}

	@Override
	protected void onRegistered(Context arg0, final String arg1) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				NetworkManager.INSTANCE.registerPush(arg1);
			}
		}).start();

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}
	private static void generateNotification(final Context context, final Intent intent) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				String temp = null;
				String type = null;
				String message = null;
				int id = -1;
				String board = null;
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeZone(TimeZone.getDefault());
				calendar.setTimeInMillis(System.currentTimeMillis());
				SimpleDateFormat formatter = new SimpleDateFormat("a hh:mm");
				String when = formatter.format(calendar.getTime());
				try {
					if ((temp = intent.getStringExtra("type")) != null)
						type = URLDecoder.decode(temp, "UTF-8");
					if ((temp = intent.getStringExtra("message")) != null)
						message = URLDecoder.decode(temp, "UTF-8");
					if ((temp = intent.getStringExtra("id")) != null)
						id = Integer.parseInt(temp);
					if ((temp = intent.getStringExtra("board")) != null)
						board = URLDecoder.decode(temp, "UTF-8");
				}
				catch(Exception e) {

				}

				NotificationManager notificationManager = (NotificationManager)
						context.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
				Intent notiIntent  = new Intent(context, LoginActivity.class);
				notiIntent.putExtra("fromNoti", true);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 6, notiIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
				
				builder
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("새 알림이 있습니다.")
				.setWhen(System.currentTimeMillis())
				.setContentText(message)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setVibrate(new long[]{100, 50, 150, 50, 200});
				
				DBHelper.INSTANCE.initialize(context);
				if (type.equals("portal")) {
					String title = context.getResources().getString(R.string.noti_portal);
					DBHelper.INSTANCE.insertColumn(title, message, type, board, id, when);
					builder.setContentTitle(title);
				}
				else if (type.equals("article")){
					String title = context.getResources().getString(R.string.noti_article);
					DBHelper.INSTANCE.insertColumn(title, message, type, board, id, when);
					builder.setContentTitle(title);
				}
				else if (type.equals("reply")) {
					String title = context.getResources().getString(R.string.noti_reply);
					DBHelper.INSTANCE.insertColumn(title, message, type, board, id, when);
					builder.setContentTitle(title);
				}
				else if (type.equals("calendar")) {
					String title = context.getResources().getString(R.string.noti_calendar);
					DBHelper.INSTANCE.insertColumn(title, "아직달력지원 ㄴㄴ해", type, null, -1, when);
					builder.setContentTitle(title);
				}
				else {
					builder.setContentTitle("서버가 미쳐 날뛰고 있습니다.");
				}
				notificationManager.notify(0, builder.build());
			}
		};
		ThreadManager.INSTANCE.getPoolExcecutor().execute(runnable);

	}
	
}
