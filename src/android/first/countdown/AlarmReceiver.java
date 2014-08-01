package android.first.countdown;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String TAG = "android.first.countdown.AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
			System.out.println("hahahhahah");
		}
		
		intent.setClass(context, AlarmExecutor.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		//context.startActivity(intent);
		
	}
}
