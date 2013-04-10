package com.example.onestep;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.onestep.login.LoginActivity;
import com.example.onestep.util.NetworkManager;
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
	private static void generateNotification(Context context, Intent intent) {
		String type;
		try {
			type = URLDecoder.decode(intent.getStringExtra("type"), "UTF-8");
			String title = URLDecoder.decode(intent.getStringExtra("title"), "UTF-8");
			NotificationManager notificationManager = (NotificationManager)
					context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			Intent notiIntent  = new Intent(context, LoginActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notiIntent, 0);
			builder
			.setSmallIcon(R.drawable.ic_launcher)
			.setTicker("새 알림이 있습니다.")
			.setWhen(System.currentTimeMillis())
			.setContentText(title)
			.setContentInfo("뭔가 정보")
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			.setVibrate(new long[]{100, 50, 150, 50, 200});

			if (type.equals("portal")) {
				builder.setContentTitle("새로운 공지글이 등록되었습니다.");
			}
			else if (type.equals("article")){
				builder.setContentTitle("아티클임");
			}
			else if (type.equals("reply")) {
				builder.setContentTitle("댓글임");
			}
			else if (type.equals("calendar")) {
				builder.setContentTitle("달력임");
			}
			else {
				builder.setContentTitle("서버가 미쳐 날뛰고 있습니다.");
			}
			notificationManager.notify(0, builder.build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
