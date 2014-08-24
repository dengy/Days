package android.first.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Intent i = new Intent();
		i.setClass(context, UpdateWidgetService.class);
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			context.startService(i);
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			context.startService(i);
		}
		
		
	}
}
