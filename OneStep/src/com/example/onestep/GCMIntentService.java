package com.example.onestep;

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

	public GCMIntentService() {
		super("556333818024");
	}
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i("","error");
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		Log.i("", "received");
		generateNotification(arg0, arg1.getStringExtra("message"));

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
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent notiIntent  = new Intent(context, LoginActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notiIntent, 0);
        Notification notification = 
        		builder
        		.setSmallIcon(R.drawable.ic_launcher)
        		.setTicker("알림!")
        		.setWhen(System.currentTimeMillis())
        		.setContentTitle("이것은 총학어플 알림")
        		.setContentText(message)
        		.setContentInfo("정보")
        		.setContentIntent(intent)
        		.setAutoCancel(true)
        		.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        		.setVibrate(new long[]{100, 50, 150, 50, 200})
        		.build();
        notificationManager.notify(0, notification);
    }

}
