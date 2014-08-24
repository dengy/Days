package android.first.countdown;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.first.countdown.util.SharedPrefsUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ItemListFragment extends Fragment{
	private static final String TAG = "ItemList";
	private ListView list;
	private View rootView;
	
	//Activity act = getActivity();
    private Activity act;
	
	public ItemListFragment() {
        // Empty constructor required for fragment subclasses
    }
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        rootView = inflater.inflate(R.layout.countdownlist, container, false);
        Bundle bundle = this.getArguments();
		String mType = bundle.getString(CountDown.PRIORITY);
        initViews(rootView, mType);
        act = getActivity();
        
     // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = act.getIntent();
        if (intent.getData() == null) {
            intent.setData(CountDown.CONTENT_URI);
        }
        
        //show the list by type
        Cursor cursor = getCursorByUri(CountDown.CONTENT_TYPE_URI, mType);
        
        CustomCursorAdapter itemAdapter = new CustomCursorAdapter(act, R.layout.countdownlist_item, cursor);
        list.setAdapter(itemAdapter);
        list.setEmptyView(rootView.findViewById(android.R.id.empty));
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Uri uri = ContentUris.withAppendedId(act.getIntent().getData(), id);
				Intent intent = new Intent(Intent.ACTION_EDIT, uri);
				startActivity(intent);
			}
        	
		});
        
        return rootView;
    }
	
	@Override
    public void onResume() {
    	super.onResume();
    	//show the top countdown data
        showTheTopCountDown(rootView);
    }
	
	private void initViews(View rootView, final String mType) {
		list = (ListView)rootView.findViewById(R.id.list);
		rootView.findViewById(android.R.id.empty).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_INSERT, act.getIntent().getData());
				intent.putExtra(CountDown.PRIORITY, mType);
				startActivity(intent);
			}
			
		});
/*        settings = (View)rootView.findViewById(R.id.settings);
        
        //add click listener
        rootView.findViewById(R.id.about).setOnClickListener(this);
        rootView.findViewById(R.id.functions).setOnClickListener(this);
        rootView.findViewById(R.id.rate).setOnClickListener(this);
        rootView.findViewById(R.id.feedback).setOnClickListener(this);
        runningButton = (Button)rootView.findViewById(R.id.running);
        runningButton.setOnClickListener(this);
        completeButton = (Button)rootView.findViewById(R.id.complete);
        completeButton.setOnClickListener(this);
        settingButton = (Button)rootView.findViewById(R.id.setting);
        settingButton.setOnClickListener(this);
*/	}
	
	/**
	 * show the top task
	 * @param rootView
	 */
	private void showTheTopCountDown(View rootView) {
		boolean isFirstOpenApp = isFirstOpenApp();
		String endDate = null;
		String title = null;
		int topLeftDays = 0;
		if(isFirstOpenApp) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String str = sdf.format(new Date());
			endDate = (Integer.parseInt(str.split("-")[0]) + 1 )+ "-01-01";
			if(initTheFirstTask(endDate)) {
				//init the first data: new year
				title = getResources().getString(R.string.init_data_title);
			}
		} else {
			Cursor cursor = act.managedQuery(CountDown.CONTENT_URI, new String[] {CountDown._ID, CountDown.TITLE, 
		        	CountDown.END_DATE, CountDown.PRIORITY, CountDown.WIDGET_IDS}, null, null, CountDown.TOP_SORT_ORDER);
			boolean hasData = cursor.moveToFirst();
			if(hasData) {
				endDate = cursor.getString(cursor.getColumnIndex(CountDown.END_DATE));
				title = cursor.getString(cursor.getColumnIndex(CountDown.TITLE));
			}
		}
		 
		if(endDate != null && title != null) {
			topLeftDays = CountDownAppWidgetProvider.getDayDiff(endDate);
			TextView dayStatus = (TextView)rootView.findViewById(R.id.dayStatus);
			if(topLeftDays < 0) {
				dayStatus.setText(getResources().getString(R.string.days_status_passed));
				topLeftDays*=-1;
			} else {
				dayStatus.setText(getResources().getString(R.string.days_status_left));
			}
			((TextView)rootView.findViewById(R.id.topTitle)).setText(title);
			((TextView)rootView.findViewById(R.id.topDate)).setText(endDate);
			((TextView)rootView.findViewById(R.id.topLeftDays)).setText(topLeftDays + "");
			
			rootView.findViewById(R.id.topLayout).setVisibility(View.VISIBLE);
		} else {
			rootView.findViewById(R.id.topLayout).setVisibility(View.INVISIBLE);
		}
		
		
	}
	
	/**
	 * is first open app?
	 * @return
	 */
	private boolean isFirstOpenApp() {
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(act, Constant.COUNT_DOWN_SETTING_PREF);
		return prefs.getBoolean(Constant.IS_FIRST_OPEN_APP, true);
	}
	
	/**
	 * init the app with the first task
	 * @param endDate
	 * @return
	 */
	private boolean initTheFirstTask(String endDate) {
		ContentValues values = new ContentValues();
		values.put(CountDown.TITLE, getResources().getString(R.string.init_data_title));
		values.put(CountDown.PRIORITY, this.getResources().getString(R.string.type_life));
		values.put(CountDown.END_DATE, endDate);
		values.put(CountDown.END_TIME, "");
		values.put(CountDown.REMIND_DATE, "");
		values.put(CountDown.REMIND_BELL, "");
		values.put(CountDown.TOP_INDEX, Constant.TOP_SWITCH_ON);
		act.getContentResolver().insert(act.getIntent().getData(), values);
		
		saveIsOpenAppFlag();
		
		return true;
	}
	
	/**
	 * save the first open app state
	 */
	private void saveIsOpenAppFlag() {
		SharedPreferences.Editor editor = SharedPrefsUtil.getSharedPrefs(act, Constant.COUNT_DOWN_SETTING_PREF).edit();
		editor.putBoolean(Constant.IS_FIRST_OPEN_APP, false);
		editor.commit();
	}
	
	/**
	 * get the cursor for the list
	 * @param uri
	 * @return
	 */
	private Cursor getCursorByUri(Uri uri, String mType) {
		//uri = Uri.withAppendedPath(uri, mType);
		Cursor cursor = null;
		if(getResources().getString(R.string.type_all).equals(mType)) {
			cursor = act.managedQuery(uri, new String[] {CountDown._ID, CountDown.TITLE, CountDown.END_DATE, CountDown.PRIORITY, CountDown.WIDGET_IDS}, 
					null, null,CountDown.DEFAULT_SORT_ORDER); 
		} else {
			cursor = act.managedQuery(uri, new String[] {CountDown._ID, CountDown.TITLE, CountDown.END_DATE, CountDown.PRIORITY, CountDown.WIDGET_IDS}, 
					CountDown.PRIORITY + "=?", new String[]{mType},
		                CountDown.DEFAULT_SORT_ORDER); 
		}

		return cursor;
	}
	
	class CustomCursorAdapter extends ResourceCursorAdapter {
//		private HashMap<Integer, MyCountDownTimer> timerHashMap 
//		= new HashMap<Integer, MyCountDownTimer>();
		
		public CustomCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c);
		}
		
		@Override
		public View newView (Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.titleView = (TextView)view.findViewById(R.id.title);
//			viewHolder.priorityView = (TextView)view.findViewById(R.id.priority);
//			viewHolder.remindBellImage = view.findViewById(R.id.remindBellImage);
//			viewHolder.finishSlipButton = (SlipButton)view.findViewById(R.id.finish);
//			viewHolder.finishCheckBox = (CheckBox)view.findViewById(R.id.finish);
			viewHolder.enddateView = (TextView)view.findViewById(R.id.enddate);
			viewHolder.daysView = (TextView)view.findViewById(R.id.days);
//			viewHolder.hoursView = (TextView)view.findViewById(R.id.hours);
//			viewHolder.minutesView = (TextView)view.findViewById(R.id.minutes);
//			viewHolder.secondsView = (TextView)view.findViewById(R.id.seconds);
//			viewHolder.expireView = (TextView)view.findViewById(R.id.expire);
			viewHolder.countdownLayout = view.findViewById(R.id.countdownLayout);
			viewHolder.itemLeftDayLabelView = (TextView)view.findViewById(R.id.itemLeftDayLabel);
			viewHolder.itemLeftDayStatusView = (TextView)view.findViewById(R.id.itemLeftDayStatus);
			view.setTag(viewHolder);
			
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			ViewHolder viewHolder = (ViewHolder)view.getTag();
			
			if(c == null) {
				Log.e(TAG, "cursor is null!");
				throw new NullPointerException("Cusor is null");
			} else {
				final String title = c.getString(c.getColumnIndex(CountDown.TITLE));
				viewHolder.titleView.setText(title);
//				String remindBell = c.getString(c.getColumnIndex(CountDown.REMIND_BELL));
				//show remindBell image
				/**if(remindBell == null || getResources().getString(R.string.mute).equals(remindBell)) {
					viewHolder.remindBellImage.setVisibility(View.GONE);
				} else {
					viewHolder.remindBellImage.setVisibility(View.VISIBLE);
				}**/
				
//			    String state = c.getString(c.getColumnIndex(CountDown.STATE));
			    final int id = c.getInt(c.getColumnIndex(CountDown._ID));//primary key
			    //for delete alarm data after countdown finish
			    viewHolder._ID = id;
			    //viewHolder.prefs = context.getSharedPreferences(Constant.ALARM_DATA_FILE, Context.MODE_PRIVATE).edit();
			    //final String widgetIds = c.getString(c.getColumnIndex(CountDown.WIDGET_IDS));

			    //show checkbox
//			    if(CountDown.STATE_OPENED.equals(state)) {
//			    	viewHolder.finishCheckBox.setChecked(false);
//			    } else if(CountDown.STATE_CLOSED.equals(state)){
//			    	viewHolder.finishCheckBox.setChecked(true);
//			    }
			    			    
			    //show priority
			    //String priority = c.getString(c.getColumnIndex(CountDown.PRIORITY));
//			    setPriority(priority, viewHolder);
			    
			    //show countdown time
			    String endDate = c.getString(c.getColumnIndex(CountDown.END_DATE));
//			    String endTime = StringUtil.underLineFilter(c.getString(c.getColumnIndex(CountDown.END_TIME)));
//			    viewHolder.enddateView.setText(endDate + " " + endTime);
			    if(endDate != null && !"".equals(endDate)) {
			    	//createCountDownTimer(endDate, endTime, viewHolder, id);
			    	showCountDown(endDate, viewHolder);
			    }
			    
			}
			
		}
		
		/**
		 * show the left days of the list item
		 * @param endDate
		 * @param viewHolder
		 */
		private void showCountDown(String endDate, ViewHolder viewHolder) {
			int daysDiff = CountDownAppWidgetProvider.getDayDiff(endDate);
			if(Constant.ERROR_DAYS != daysDiff) {
				if(daysDiff < 0) {
					daysDiff *= -1;
					viewHolder.itemLeftDayLabelView.setVisibility(View.GONE);
					viewHolder.itemLeftDayStatusView.setText(getResources().getString(R.string.days_status_passed));
				} else {
					viewHolder.itemLeftDayLabelView.setVisibility(View.VISIBLE);
					viewHolder.itemLeftDayStatusView.setText(getResources().getString(R.string.days_status_left));
				}
		    	viewHolder.daysView.setText(daysDiff + "");
			} else {				
		    	viewHolder.daysView.setText("³ö´íÁË");
			}
		}
		
		/**
		 * create countDownTimer
		 * @param endDate
		 * @param endTime
		 * @param viewHolder
		 * @param id
		 */
		/**private void createCountDownTimer(String endDate, String endTime, ViewHolder viewHolder, int id) {
			Calendar calendar = Calendar.getInstance();
			String[] date = endDate.split("-");
	    	if(date.length == 3) {
	    		long difference = 0;
	    		if(endTime != null && !"".equals(endTime)) {
			    	String[] time = endTime.split(":");
			    	if(time.length == 2) {
		    			calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
		        				Integer.parseInt(time[0]), Integer.parseInt(time[1]));
		    			difference = calendar.getTimeInMillis() - System.currentTimeMillis();
		    			
			    	} 
			  } else {
				    calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
	        				0, 0);
				    
				    difference = calendar.getTimeInMillis() - System.currentTimeMillis();
			  }
	    		
	    	  //create countdown timer
	    	MyCountDownTimer timer = timerHashMap.get(id);
	    	if(timer != null) {
	    		timer.cancel();
	    	}
	    	
	    	timer = new MyCountDownTimer(difference, Constant.INTERVAL_MIN, viewHolder);
	    	timer.start();
	    	timerHashMap.put(id, timer);
	    	
	    }			  
	}**/
		
		/**
		 * cancel all old countDownTimers
		 * @param timerHashMap
		 */
//		private void cancelAllCountDownTimers(HashMap<Integer, MyCountDownTimer> timerHashMap) {
//			Set<Integer> keySet = timerHashMap.keySet();
//			Iterator<Integer> it = keySet.iterator();
//			while(it.hasNext()) {
//				Integer key = it.next();
//				MyCountDownTimer timer = timerHashMap.get(key);
//				if(timer != null) {
//					timer.cancel();
//				}
//			}
//			
//			//clean all data
//			timerHashMap.clear();
//		}
		
		/**
		 * update state
		 */
//		private void updateState(Uri uri, boolean checkState) {
//			ContentValues values = new ContentValues();
//			if(checkState) {
//				values.put(CountDown.STATE, CountDown.STATE_CLOSED);
//				getContentResolver().update(uri, values, null, null);
//				
//			} else {
//				values.put(CountDown.STATE, CountDown.STATE_OPENED);
//				getContentResolver().update(uri, values, null, null);
//				
//			}
//		}
	}
}
