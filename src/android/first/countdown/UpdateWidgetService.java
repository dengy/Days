package android.first.countdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	public static final String TAG = "UpdateWidgetService";
    int mStartMode = Service.START_STICKY;;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
    	setWidgetUpdateAlarmManager();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
//    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	 
        //if (pm.isScreenOn()) {
    	Bundle bundle = intent.getExtras();
    	//update Widget View
    	if(bundle != null) {
    		int _ID = bundle.getInt(CountDown._ID, -99);
        	String title = bundle.getString(CountDown.TITLE);
        	if(title != null && !"".equals(title) && _ID != -99) {
            	int mAppWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        		String priority = bundle.getString(CountDown.PRIORITY);
            	String endDate = bundle.getString(CountDown.END_DATE);
            	String task_state = bundle.getString(Constant.TASK_STATE);
            	
            	AppWidgetManager am = AppWidgetManager.getInstance(this); 
                RemoteViews views = CountDownAppWidgetProvider.getRemoteViews(priority, this, _ID);
                
              //仅当widget所对应的任务属于Running状态的时候，可以编辑
        		if(Constant.RUNNING_STATE.equals(task_state)) {
        			//add click event
        			Uri uri = ContentUris.withAppendedId(CountDown.CONTENT_URI, _ID);
        			Intent updateIntent = new Intent(Intent.ACTION_EDIT, uri);
        	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        	        views.setOnClickPendingIntent(R.id.reminderWidget, pendingIntent);
        		}		
            	
            	views.setTextViewText(R.id.widget_title, title);
            	//views.setTextViewText(R.id.widget_end_date, endDate);
            	
            	//update widget
            	CountDownAppWidgetProvider.refreshAppWidget(this, views, endDate, priority, mAppWidgetId, am);
        	}
        	
            //}
        	
    	}
    	
        return mStartMode;
    }
    
    /**
    protected void tick(RemoteViews views, long millisUntilFinished, int mAppWidgetId, AppWidgetManager appWidgetManager) {
    	int days = (int) ((millisUntilFinished / 1000) / 86400);

		int hours = (int) (((millisUntilFinished / 1000) - (days
                * 86400)) / 3600);
        int minutes = (int) (((millisUntilFinished / 1000) - ((days
                * 86400) + (hours * 3600))) / 60);
//        int seconds = (int) ((millisUntilFinished / 1000) % 60);
        views.setTextViewText(R.id.days, days + "");
        views.setTextViewText(R.id.hours, hours + "");
        views.setTextViewText(R.id.minutes, minutes + "");
//        views.setTextViewText(R.id.seconds, seconds + "");

        views.setViewVisibility(R.id.expire, View.GONE);
        views.setViewVisibility(R.id.countdownLayout, View.VISIBLE);
        
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
    }**/
    
    
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }
    
    private void setWidgetUpdateAlarmManager() {
    	cancelAlarmRepeat();
    	
		Intent intent = new Intent(Constant.UPDATE_WIDGET);
		intent.setClass(this, CountDownAppWidgetProvider.class);
		//transfer data to receiver
		Bundle extras = new Bundle();
		intent.putExtras(extras);
		
		PendingIntent sender = PendingIntent.getBroadcast(this,
        		0, intent, 0);
        // Schedule the alarm!
		long l = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
        		l, AlarmManager.INTERVAL_DAY, sender);
	}
    
    private void cancelAlarmRepeat() {
		Intent intent = new Intent(Constant.UPDATE_WIDGET);
		intent.setClass(this, CountDownAppWidgetProvider.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
}
