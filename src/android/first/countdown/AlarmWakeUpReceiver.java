package android.first.countdown;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmWakeUpReceiver extends BroadcastReceiver {
	public static final String TAG = "android.first.countdown.AlarmWakeUpReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceive");
		Log.i(TAG, intent.getAction());
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			SharedPreferences prefs = context.getSharedPreferences(Constant.ALARM_DATA_FILE, Context.MODE_PRIVATE);
			Map<String,?> all = prefs.getAll();
			Set<String> set = all.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()) {
				Log.i(TAG, "loop.......");
				String key = it.next();
				String value = all.get(key).toString();
				resetAlarm(value , context);
			}
		}
		
		
	}
	
	private void resetAlarm(String value, Context context) {
		Log.i(TAG, "resetAlarm");
		if(value != null && !"".equals(value)) {
			String[] strs = value.split(Constant.SPLIT_SEMICOLON);
			if(strs.length != 6)
				return;
			
			Boolean remindDate = Boolean.parseBoolean(strs[1]);
			Long firstTime = Long.parseLong(strs[2]);
			Integer _ID = Integer.parseInt(strs[0]);
			String title = strs[3];
			String reminder = strs[4];
			String endDate = strs[5];
			
			PendingIntent operation = PendingIntent.getBroadcast(context,
					_ID, CountDownEdit.setIntentForAlarm(context, _ID, title, reminder, endDate),
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			//reset alarm
			AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
			if(remindDate) {
				am.setRepeating(AlarmManager.RTC_WAKEUP,
	            		firstTime, AlarmManager.INTERVAL_DAY, operation);
				Log.i(TAG, "startAlarm1");
			} else {
				am.set(AlarmManager.RTC_WAKEUP, firstTime, operation);
				Log.i(TAG, "startAlarm2");
			}
			
		}
		
		
	}
}
