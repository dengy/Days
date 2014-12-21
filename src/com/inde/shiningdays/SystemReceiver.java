package com.inde.shiningdays;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.inde.shiningdays.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SystemReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Intent i = new Intent();
		i.setClass(context, UpdateWidgetService.class);
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			context.startService(i);

			rebootAllReminds(context);
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			context.startService(i);
		}
		
		
	}

	private void rebootAllReminds(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constant.ALARM_DATA_FILE,
				Context.MODE_PRIVATE);
		Map<String,?> remindMap = prefs.getAll();
		for(String key : remindMap.keySet()) {
			String content = remindMap.get(key).toString();
			if(content != null) {
				String[] arr = content.split(Constant.SPLIT_SEMICOLON);
				if(arr.length == 4) {
					try {
						int id = Integer.parseInt(arr[0]);
						long triggerTime = Long.parseLong(arr[1]);
						String title = arr[2];
						String endDate = arr[3];

						PendingIntent sender = PendingIntent.getBroadcast(context,
								id, CountDownEdit.setIntentForAlarm(context, id, title, endDate),
								PendingIntent.FLAG_UPDATE_CURRENT);
						AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						//am.cancel(sender);

						//start a new alarm
						am.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}






		}

	}
}
