package android.first.countdown;


import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.first.countdown.util.Utils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class WidgetConfigure extends Activity{
	private ListView list;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	//public static final String PREFS_NAME
    //= "android.first.countdown.widgetData";
	public static final String PREF_PREFIX_KEY = "prefix_";
	public String isInitialized_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.countdownlist_widget);
		
        list = (ListView) findViewById(R.id.list);
        
        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(CountDown.CONTENT_URI);
        }
        
        //test begin
        
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);
        
        Bundle extras = intent.getExtras();
  		if (extras != null) {
  		    mAppWidgetId = extras.getInt(
  		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
  		            AppWidgetManager.INVALID_APPWIDGET_ID);
  		}
  		
  		// If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        
  		//test end
      		
        
        //show the opened state item
        Cursor cursor = getCursorByUri(CountDown.CONTENT_STATE_OPEN_URI);
        
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, R.layout.countdownlist_item_widget, cursor);
        list.setAdapter(adapter);    
        
        //给出空记录提示
        if(adapter != null && adapter.getCount() == 0) {
        	Toast.makeText(getApplicationContext(), R.string.noDataForWidget, Toast.LENGTH_SHORT).show();
        } 
        
        //item click
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long id) {
				Context context = WidgetConfigure.this;
				Cursor cursor = ((CustomCursorAdapter)adapter.getAdapter()).getCursor();
				//save data 
				saveDataForWidget(context, mAppWidgetId, cursor, Constant.RUNNING_STATE);
				
				//updateWidgetIdToDb
				updateWidgetIdToDb(cursor, mAppWidgetId);
				
				new CountDownAppWidgetProvider().updateAppWidget(context, appWidgetManager, mAppWidgetId);
				
				// Make sure we pass back the original appWidgetId
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				//isInitialized_ID 
				resultValue.putExtra(isInitialized_ID, true);
				setResult(RESULT_OK, resultValue);
				finish();
			}
        	
		});

	}
	
	@Override
    public void onResume() {
    	super.onResume();
    	//umeng sdk
    	MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
    	super.onPause();
    	//umeng sdk
    	MobclickAgent.onPause(this);
    }
	
	private void saveDataForWidget(Context context, int mAppWidgetId, Cursor cursor, String state) {
		if(cursor != null) {
			int _ID = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
			String title = cursor.getString(cursor.getColumnIndex(CountDown.TITLE));
			String endTime = cursor.getString(cursor.getColumnIndex(CountDown.END_TIME));
			String endDate = cursor.getString(cursor.getColumnIndex(CountDown.END_DATE));
			String priority = cursor.getString(cursor.getColumnIndex(CountDown.PRIORITY));
			
			saveToPreference(context, mAppWidgetId, _ID, title, endTime, endDate, priority);
		}
	}
	
	/**
	 * 保存的创建widget相关的数据到preference文件中
	 * @param context
	 * @param mAppWidgetId
	 * @param _ID
	 * @param title
	 * @param endTime
	 * @param endDate
	 * @param priority
	 */
	public static void saveToPreference(Context context, int mAppWidgetId, int _ID, String title, String endTime, String endDate, String priority) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(Constant.WIDGET_DATA_FILE, Context.MODE_PRIVATE).edit();
		//save appWidgetId
		prefs.putInt(Utils.appendAppWidgetId(CountDown._ID, mAppWidgetId), _ID);
		prefs.putString(Utils.appendAppWidgetId(CountDown.TITLE, mAppWidgetId), title);
		
		/**
		if(endTime != null && !"".equals(endTime)) {
			prefs.putString(Utils.appendAppWidgetId(CountDown.END_DATE, mAppWidgetId), endDate + " " + endTime);
		} else {
			prefs.putString(Utils.appendAppWidgetId(CountDown.END_DATE, mAppWidgetId), endDate);
		}**/
		
		prefs.putString(Utils.appendAppWidgetId(CountDown.END_DATE, mAppWidgetId), endDate);
		
		prefs.putString(Utils.appendAppWidgetId(Constant.TASK_STATE, mAppWidgetId), Constant.RUNNING_STATE);
		
		prefs.putString(Utils.appendAppWidgetId(CountDown.PRIORITY, mAppWidgetId), priority);
		
		prefs.commit();
	}
	/**
	 * 更新remind task的状态(running,deleted,finished)
	 * @param context
	 * @param mAppWidgetId
	 */
	public static void updateTaskStateInPreference(Context context, int mAppWidgetId, String taskState) {
		SharedPreferences.Editor prefs = context
    			.getSharedPreferences(Constant.WIDGET_DATA_FILE, Context.MODE_PRIVATE).edit();
		prefs.putString(Utils.appendAppWidgetId(Constant.TASK_STATE, mAppWidgetId), taskState);
    	prefs.commit();
	}
	
	/**
	 * 删除preference文件中保存的创建widget相关的数据
	 * @param context
	 * @param mAppWidgetId
	 */
	public static void deleteFromPreference(Context context, int mAppWidgetId) {
		SharedPreferences.Editor prefs = context
    			.getSharedPreferences(Constant.WIDGET_DATA_FILE, Context.MODE_PRIVATE).edit();
		prefs.remove(Utils.appendAppWidgetId(CountDown._ID, mAppWidgetId));
    	prefs.remove(Utils.appendAppWidgetId(CountDown.TITLE, mAppWidgetId));
    	prefs.remove(Utils.appendAppWidgetId(CountDown.END_DATE, mAppWidgetId));
    	prefs.remove(Utils.appendAppWidgetId(CountDown.PRIORITY, mAppWidgetId));
    	prefs.remove(Utils.appendAppWidgetId(Constant.TASK_STATE, mAppWidgetId));
    	prefs.commit();
	}
	
	private void updateWidgetIdToDb(Cursor cursor, int mAppWidgetId) {
		StringBuilder widgetIds = new StringBuilder(cursor.getString(
				cursor.getColumnIndex(CountDown.WIDGET_IDS)));
		
		int id = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
		//update widgetId to database
		if(id > 0) {
			Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
			ContentValues values = new ContentValues();
			if(widgetIds.length() > 0) {
				widgetIds.append(",").append(mAppWidgetId);
			} else {
				widgetIds.append(mAppWidgetId);
			}
			values.put(CountDown.WIDGET_IDS, widgetIds.toString());
			getContentResolver().update(uri, values, null, null);
		}
		
	}
	
//	private HashMap<String,String> getDataForWidget() {
//		
//	}
	
	private Cursor getCursorByUri(Uri uri) {
		Cursor cursor = managedQuery(uri , new String[] {CountDown._ID, CountDown.TITLE, 
        	CountDown.END_DATE, CountDown.END_TIME, CountDown.REMIND_BELL, CountDown.STATE, CountDown.PRIORITY, CountDown.WIDGET_IDS}, null, null,
                CountDown.DEFAULT_SORT_ORDER); 
		return cursor;
	}
	
	class CustomCursorAdapter extends ResourceCursorAdapter {
		public CustomCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c);
		}
		
		@Override
		public View newView (Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			ViewHolder viewHolder = new ViewHolder();
//			viewHolder.priorityView = (TextView)view.findViewById(R.id.priority);
			viewHolder.titleView = (TextView)view.findViewById(R.id.title);
			viewHolder.remindBellImage = view.findViewById(R.id.remindBellImage);
//			viewHolder.finishSlipButton = (SlipButton)view.findViewById(R.id.finish);
			viewHolder.enddateView = (TextView)view.findViewById(R.id.enddate);
//			viewHolder.daysView = (TextView)view.findViewById(R.id.days);
//			viewHolder.hoursView = (TextView)view.findViewById(R.id.hours);
//			viewHolder.minutesView = (TextView)view.findViewById(R.id.minutes);
//			viewHolder.secondsView = (TextView)view.findViewById(R.id.seconds);
//			viewHolder.expireView = (TextView)view.findViewById(R.id.expire);
//			viewHolder.countdownLayout = view.findViewById(R.id.countdownLayout);
			view.setTag(viewHolder);
			
			return view;
		}
		
		class ViewHolder{
//			private TextView priorityView;
			private TextView titleView;
			private View remindBellImage;
//			private SlipButton finishSlipButton;
			private TextView enddateView;
//			private TextView daysView;
//			private TextView hoursView;
//			private TextView minutesView;
//			private TextView secondsView;
//			private TextView expireView;
//			private View countdownLayout;
			
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			ViewHolder viewHolder = (ViewHolder)view.getTag();
			
			if(c == null) {
				throw new NullPointerException("Cusor is null");
			} else {
				viewHolder.titleView.setText(c.getString(c.getColumnIndex(CountDown.TITLE)));
//			    viewHolder.enddateView.setText(c.getString(c.getColumnIndex(CountDown.END_DATE)));
				String remindBell = c.getString(c.getColumnIndex(CountDown.REMIND_BELL));
				
				//show remindBell image
//				if(remindBell == null || getResources().getString(R.string.mute).equals(remindBell)) {
//					viewHolder.remindBellImage.setVisibility(View.GONE);
//				} else {
//					viewHolder.remindBellImage.setVisibility(View.VISIBLE);
//				}
				
			    //show priority
//			    String priority = c.getString(c.getColumnIndex(CountDown.PRIORITY));
//			    if(CountDown.PRIORITY_H.equals(priority)) {
//			    	viewHolder.priorityView.setText(CountDown.PRIORITY_H.substring(0, 1));
//			    	viewHolder.priorityView.setBackgroundColor(getResources().getColor(R.color.H_color));
//			    } else if(CountDown.PRIORITY_M.equals(priority)) {
//			    	viewHolder.priorityView.setText(CountDown.PRIORITY_M.substring(0, 1));
//			    	viewHolder.priorityView.setBackgroundColor(getResources().getColor(R.color.M_color));
//			    } else if(CountDown.PRIORITY_L.equals(priority)) {
//			    	viewHolder.priorityView.setText(CountDown.PRIORITY_L.substring(0, 1));
//			    	viewHolder.priorityView.setBackgroundColor(getResources().getColor(R.color.L_color));
//			    }
			    
			    //show countdown time
			    String endDate = c.getString(c.getColumnIndex(CountDown.END_DATE));
			    String endTime = c.getString(c.getColumnIndex(CountDown.END_TIME));
			    viewHolder.enddateView.setText(endDate + " " + endTime);
			}
			
		}
				
	}
}
