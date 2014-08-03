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
//				Log.i(TAG, "loop.......");
//				String key = it.next();
//				String value = all.get(key).toString();
//				resetAlarm(value , context);
			}
		}
		
		
	}
}
