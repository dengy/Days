package android.first.countdown;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.first.countdown.util.Utils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class CountDownAppWidgetProvider extends AppWidgetProvider {
	public static final String TAG = "CountDownAppWidgetProvider";

	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int count = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i<count; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.d(TAG, "widgetId=" + appWidgetId);
            if(!validateWidget(appWidgetId, appWidgetManager)) {
				continue;
			}
            updateAppWidget(context, appWidgetManager, appWidgetId);
                        
        }
        
        
    }
    
    @Override 
    public void onReceive(Context context, Intent intent) {
    	super.onReceive(context, intent); 
        Log.i(TAG, "onReceive");
        
        if(Constant.UPDATE_WIDGET.equals(intent.getAction())){ 
        	Log.i("OnlyWidget", "UPDATE_WIDGET");
        	Bundle extras = intent.getExtras();
        	int[] appWidgetIds = null;;
        	if (extras != null) {
        		appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_ID);
        	} 
        	
        	if(appWidgetIds == null ) {
        		appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CountDownAppWidgetProvider.class));
        	}
        	
        	this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        	
        } else if(Constant.DELETE_WIDGET.equals(intent.getAction())) {
        	Bundle extras = intent.getExtras();
        	if (extras != null) {
        		String strAppWidgetId = extras.getString(AppWidgetManager.EXTRA_APPWIDGET_ID);
        		
        		if(strAppWidgetId != null && !"".equals(strAppWidgetId)) {
        			int mAppWidgetId = Integer.parseInt(strAppWidgetId);
        			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            		//if widget exist
        			if(!validateWidget(mAppWidgetId, appWidgetManager)) {
        				return;
        			}
                	
            		if(mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            			//update remind task state
            			WidgetConfigure.updateTaskStateInPreference(context, mAppWidgetId, Constant.DELETED_STATE);
            			//nofify widget to update
            			this.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                    	
            		}
        		}
            }
        } 
        /**else if(Constant.FINISH_WIDGET.equals(intent.getAction())) {
        	Log.i(TAG, Constant.FINISH_WIDGET);
        	Bundle extras = intent.getExtras();
        	if (extras != null) {
        		String strAppWidgetId = extras.getString(AppWidgetManager.EXTRA_APPWIDGET_ID);
        		if(strAppWidgetId != null && !"".equals(strAppWidgetId)) {
        			int mAppWidgetId = Integer.parseInt(strAppWidgetId);
            		
                	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                	//if widget exist
                	if(!validateWidget(mAppWidgetId, appWidgetManager)) {
        				return;
        			}
                	
            		if(mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            			//update remind task state
            			WidgetConfigure.updateRemindStateInPreference(context, mAppWidgetId, Constant.FINISHED_STATE);
            			//nofify widget to update
            			this.updateAppWidget(context, appWidgetManager, mAppWidgetId);                    	
            		}
        		}
        		
            }
        }**/
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        int count = appWidgetIds.length;
        for (int i=0; i<count; i++) {
        	int appWidgetId = appWidgetIds[i];
        	deleteData(context, appWidgetId);
        }
    }
    
    /**
     * delete the data in the prefs file
     * @param context
     * @param mAppWidgetId
     */
    public void deleteData(Context context ,int mAppWidgetId) {
    	//delete countdownTimder object in the map
    	//cancelUpdateWidgetAlarmer(context, mAppWidgetId);
    	
    	//delete the data in SharedPreferences
    	WidgetConfigure.deleteFromPreference(context, mAppWidgetId);
    }
    
    /**
     * update the deleted state wiget view
     * @param appWidgetManager
     * @param context
     * @param mAppWidgetId
     */
    public static void deleteAppWidget(AppWidgetManager appWidgetManager, Context context, int mAppWidgetId) {
    	//updated widget
    	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.invalid_widget);
    	views.setTextViewText(R.id.invalid_widget, context.getResources().getString(R.string.invalid_widget));
    	appWidgetManager.updateAppWidget(mAppWidgetId, views);
    	
    	//cancel alarmManager
    	//cancelUpdateWidgetAlarmer(context, mAppWidgetId);
    }
    
    /**public static void finishAppWidget(AppWidgetManager appWidgetManager, Context context, int mAppWidgetId, 
    		String title, String endDate) {
    	//RemoteViews views = getRemoteViews(priority, context, _ID);
    	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reminder_widget_finish);;    	
    	
    	views.setTextViewText(R.id.widget_title, title);
    	views.setTextViewText(R.id.widget_end_date, endDate);
    	
    	views.setTextViewText(R.id.expire, Constant.FINISHED);
        views.setViewVisibility(R.id.expire, View.VISIBLE);
        views.setViewVisibility(R.id.countdownLayout, View.GONE);
    	appWidgetManager.updateAppWidget(mAppWidgetId, views);
    	
    	//cancel alarmManager
    	cancelUpdateWidgetAlarmer(context, mAppWidgetId);
    }**/
    
    /**
     * refresh the widget view
     * @param context
     * @param views
     * @param endDate
     * @param type
     * @param mAppWidgetId
     * @param appWidgetManager
     */
    public static void refreshAppWidget(Context context, RemoteViews views, String endDate, String type, int mAppWidgetId, 
    		AppWidgetManager appWidgetManager) {
    	int days = getDayDiff(endDate);
    	if(days < 0) {
    		days *= -1;
    		views.setViewVisibility(R.id.itemLeftDayLabel, View.GONE);
    		views.setTextViewText(R.id.itemLeftDayStatus, context.getResources().getString(R.string.days_status_passed));
    	} else {
    		views.setViewVisibility(R.id.itemLeftDayLabel, View.VISIBLE);
    		views.setTextViewText(R.id.itemLeftDayStatus, context.getResources().getString(R.string.days_status_left));
    	}
    	views.setTextViewText(R.id.widget_type, type);
    	views.setTextViewText(R.id.widget_end_date,  endDate);
        views.setTextViewText(R.id.days, days + "");
        
//        views.setTextViewText(R.id.hours, hours + "");
//        views.setTextViewText(R.id.minutes, minutes + "");
//        views.setTextViewText(R.id.seconds, seconds + "");

//        views.setViewVisibility(R.id.expire, View.GONE);
        //views.setViewVisibility(R.id.countdownLayout, View.VISIBLE);
    	
    	// Tell the widget manager
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
    	
    }
    
    /**
     * update app widget
     * @param context
     * @param appWidgetManager
     * @param mAppWidgetId
     */
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int mAppWidgetId) {
    	//load data from shared preferences
    	SharedPreferences prefs = context.getSharedPreferences(Constant.WIDGET_DATA_FILE, Context.MODE_PRIVATE);
    	int _ID = prefs.getInt(Utils.appendAppWidgetId(CountDown._ID, mAppWidgetId), -1);
    	String title = prefs.getString(Utils.appendAppWidgetId(CountDown.TITLE, mAppWidgetId), null);
    	if(title != null && !"".equals(title)) {
    		String priority = prefs.getString(Utils.appendAppWidgetId(CountDown.PRIORITY, mAppWidgetId), "");
        	String endDate = prefs.getString(Utils.appendAppWidgetId(CountDown.END_DATE, mAppWidgetId), "");
        	String taskState = prefs.getString(Utils.appendAppWidgetId(Constant.TASK_STATE, mAppWidgetId), "");
        	
        	if(Constant.DELETED_STATE.equals(taskState)) {
        		deleteAppWidget(appWidgetManager, context, mAppWidgetId);
        	} else {
        		//set alarmManager to control service to update widget
            	Intent intent  = getIntentForAlarm(context, mAppWidgetId,
                		_ID, title, priority, endDate, taskState);
                context.startService(intent); 
                
        		
        		/**
                RemoteViews views = CountDownAppWidgetProvider.getRemoteViews(priority, context, _ID);
                
              //仅当widget所对应的任务属于Running状态的时候，可以编辑
        		if(Constant.RUNNING_STATE.equals(taskState)) {
        			//add click event
        			Uri uri = ContentUris.withAppendedId(CountDown.CONTENT_URI, _ID);
        			Intent updateIntent = new Intent(Intent.ACTION_EDIT, uri);
        	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, updateIntent, 0);
        	        views.setOnClickPendingIntent(R.id.reminderWidget, pendingIntent);
        		}		
            	
            	views.setTextViewText(R.id.widget_title, title);
            	//views.setTextViewText(R.id.widget_end_date, endDate);
            	
            	//update widget
            	CountDownAppWidgetProvider.refreshAppWidget(context, views, endDate, priority, mAppWidgetId, appWidgetManager);
            	**/
        	}
    	}    	    

    }
    
    /**
     * get the intent which transfered to the service
     * @param context
     * @param mAppWidgetId
     * @param _ID
     * @param title
     * @param priority
     * @param endDate
     * @param remind_state
     * @return
     */
    public static Intent getIntentForAlarm(Context context , int mAppWidgetId, int _ID, String title, String priority, 
    		String endDate, String remind_state) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		
		//transfer data to receiver
		Bundle extras = new Bundle();
		extras.putInt(CountDown._ID, _ID);
		extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		extras.putString(CountDown.TITLE, title);
		extras.putString(CountDown.PRIORITY, priority);
		extras.putString(CountDown.END_DATE, endDate);
		extras.putString(Constant.TASK_STATE, remind_state);
		
		intent.putExtras(extras);
		
		return intent;
	} 
    
    /**
     * validate widget exist
     * @param appWidgetId
     * @param appWidgetManager
     * @return
     */
    private boolean validateWidget(int appWidgetId, AppWidgetManager appWidgetManager) {
    	AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(appWidgetId);
    	if(info == null) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    /**
     * get the remoteViews by type
     * @param priority
     * @param context
     * @param _ID
     * @return
     */
    public static RemoteViews getRemoteViews(String priority, Context context, int _ID) {
    	RemoteViews views = null;
    	int viewId;
    	
//    	viewId = R.layout.reminder_widget_life;
//		views = new RemoteViews(context.getPackageName(), viewId);
		Resources resources = context.getResources();
		if(resources.getString(R.string.type_life).equals(priority)) {
			viewId = R.layout.reminder_widget_life;
			views = new RemoteViews(context.getPackageName(), viewId);
	    } else if(resources.getString(R.string.type_study).equals(priority)) {
	    	viewId = R.layout.reminder_widget_study;
	    	views = new RemoteViews(context.getPackageName(), viewId);
	    } else if(resources.getString(R.string.type_work).equals(priority)) {
	    	viewId = R.layout.reminder_widget_work;
	    	views = new RemoteViews(context.getPackageName(), viewId);
	    } else if(resources.getString(R.string.type_memorial_day).equals(priority)) {
	    	viewId = R.layout.reminder_widget_memorialday;
	    	views = new RemoteViews(context.getPackageName(), viewId);
	    } else {
	    	viewId = R.layout.reminder_widget_custom;
	    	views = new RemoteViews(context.getPackageName(), viewId);
	    }

		return views;
    }
    
    /**
     * get the left days
     * @param endDate
     * @return
     */
    public static int getDayDiff(String endDate) {
		String[] date = endDate.split("-");
		if(date.length == 3) {
    		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    		Date currentDate = new Date(System.currentTimeMillis());
    		String currentDateStr = sf.format(currentDate);
			try {
				long difference = 0l;
        		difference = sf.parse(endDate).getTime() - sf.parse(currentDateStr).getTime();
				int days = (int) ((difference / 1000) / 86400);
				return days;
			} catch (ParseException e) {
				e.printStackTrace();
				return Constant.ERROR_DAYS;
			}
    	}
		return Constant.ERROR_DAYS;
	}
    
    
    /**private  static void cancelUpdateWidgetAlarmer(Context context, int mAppWidgetId) {    	
    	Intent intent = new Intent(context, UpdateWidgetService.class);
		PendingIntent operation = PendingIntent.getService(context,
				mAppWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//cancel alarm
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(operation);
      }**/
        
}