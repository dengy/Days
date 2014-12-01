package com.inde.shiningdays;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class CommonReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(Constant.SEND_NOTIFICATION.equals(action)) {
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				sendNotification(context, bundle);
			}
		}
	}

	private void sendNotification(Context context, Bundle bundle) {
		String endDate = bundle.getString(CountDown.END_DATE);
		int id = bundle.getInt(CountDown._ID);
		int leftDays = CountDownAppWidgetProvider.getDayDiff(endDate);
		StringBuilder notificationTitle = new StringBuilder();
		String title = bundle.getString(CountDown.TITLE);
		notificationTitle.append(context.getResources().getString(R.string.leftDayLabel)).append(":").
				append(title).
				append(context.getResources().getString(R.string.days_status_left)).
				append(leftDays).append(context.getResources().getString(R.string.days));
		String appInfo = context.getResources().getString(R.string.app_name);

		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent
				= PendingIntent.getActivity(context, 0, intent, 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).
				setSmallIcon(R.drawable.ic_launcher).
				setContentTitle(appInfo).
				setContentText(notificationTitle.toString()).
				setDefaults(Notification.DEFAULT_SOUND).
				setContentIntent(pendingIntent).
				setAutoCancel(true);

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, mBuilder.build());
	}
}
