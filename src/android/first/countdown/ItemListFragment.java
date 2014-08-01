package android.first.countdown;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.first.countdown.util.StringUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ItemListFragment extends Fragment implements OnClickListener{
	private static final String TAG = "ItemList";
	private ListView list;
	private View settings;
	private Button runningButton;
	private Button completeButton;
	private Button settingButton;
	
	//Activity act = getActivity();
    private Activity act;
	
	public ItemListFragment() {
        // Empty constructor required for fragment subclasses
    }
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countdownlist, container, false);
        initViews(rootView);
        act = getActivity();
        
     // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = act.getIntent();
        if (intent.getData() == null) {
            intent.setData(CountDown.CONTENT_URI);
        }
        
        
        //show the top countdown data
        showTheTopCountDown(rootView);
        
        
        //show the list by type
        Cursor cursor = getCursorByUri(CountDown.CONTENT_TYPE_URI);
        
        CustomCursorAdapter itemAdapter = new CustomCursorAdapter(act, R.layout.countdownlist_item, cursor);
        list.setAdapter(itemAdapter);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Uri uri = ContentUris.withAppendedId(act.getIntent().getData(), id);
				startActivity(new Intent(Intent.ACTION_EDIT, uri));
			}
        	
		});
        
        //item long click
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View v,
					int position, long id) {
				TextView titleTextView = (TextView)v.findViewById(R.id.title);
				Cursor cursor = ((CustomCursorAdapter)adapter.getAdapter()).getCursor();
				//showActionDialog(titleTextView.getText().toString(), ((Long)id).intValue(), cursor);
				return false;
			}
        	
        });
        
        //setWidgetUpdateAlarmManager();
        
        return rootView;
    }
	
	private void initViews(View rootView) {
		list = (ListView)rootView.findViewById(R.id.list);
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
	
	private void showTheTopCountDown(View rootView) {
		Cursor cursor = act.managedQuery(CountDown.CONTENT_URI, new String[] {CountDown._ID, CountDown.TITLE, CountDown.STARRED, 
	        	CountDown.END_DATE, CountDown.END_TIME, CountDown.REMIND_BELL, CountDown.STATE, CountDown.PRIORITY, 
	        	CountDown.WIDGET_IDS}, CountDown.TOP_INDEX + "=?", new String[]{Constant.TOP_SWITCH_ON+""},null); 
		boolean hasTop = cursor.moveToFirst();
		String endDate = "";
		String title = "";
		int topLeftDays = 0;
		if(hasTop) {
			endDate = cursor.getString(cursor.getColumnIndex(CountDown.END_DATE));
			title = cursor.getString(cursor.getColumnIndex(CountDown.TITLE));
		} else {
			title = "倒数日发布";
			endDate = "2014-07-26";
			ContentValues values = new ContentValues();
			values.put(CountDown.TITLE, title);
			values.put(CountDown.PRIORITY, Constant.DEFAULT_PRIORITY);
			values.put(CountDown.END_DATE, endDate);
			values.put(CountDown.END_TIME, "");
			values.put(CountDown.REMIND_DATE, "");
			values.put(CountDown.REMIND_BELL, "");
			values.put(CountDown.TOP_INDEX, Constant.TOP_SWITCH_ON);
			act.getContentResolver().insert(act.getIntent().getData(), values);
		}
		
		topLeftDays = CountDownAppWidgetProvider.getDayDiff(endDate);
		((TextView)rootView.findViewById(R.id.topTitle)).setText(title);
		((TextView)rootView.findViewById(R.id.topDate)).setText(endDate);
		((TextView)rootView.findViewById(R.id.topLeftDays)).setText(topLeftDays + "");
	}
	
	private Cursor getCursorByUri(Uri uri) {
		String mType = null;
		Bundle bundle = this.getArguments();
		if(bundle != null) {
			mType = bundle.getString(CountDown.PRIORITY);
		}
		
		//uri = Uri.withAppendedPath(uri, mType);
		Cursor cursor = null;
		if(Constant.ALL_PRIORITY.equals(mType)) {
			cursor = act.managedQuery(uri, new String[] {CountDown._ID, CountDown.TITLE, CountDown.STARRED, 
		        	CountDown.END_DATE, CountDown.END_TIME, CountDown.REMIND_BELL, CountDown.STATE, CountDown.PRIORITY, 
		        	CountDown.WIDGET_IDS}, null, null,
		                CountDown.DEFAULT_SORT_ORDER); 
		} else {
			cursor = act.managedQuery(uri, new String[] {CountDown._ID, CountDown.TITLE, CountDown.STARRED, 
		        	CountDown.END_DATE, CountDown.END_TIME, CountDown.REMIND_BELL, CountDown.STATE, CountDown.PRIORITY, 
		        	CountDown.WIDGET_IDS}, CountDown.PRIORITY + "=?", new String[]{mType},
		                CountDown.DEFAULT_SORT_ORDER); 
		}
		

		return cursor;
	}
	
	class CustomCursorAdapter extends ResourceCursorAdapter {
		private HashMap<Integer, MyCountDownTimer> timerHashMap 
		= new HashMap<Integer, MyCountDownTimer>();
		
		public CustomCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c);
		}
		
		@Override
		public View newView (Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.titleView = (TextView)view.findViewById(R.id.title);
//			viewHolder.priorityView = (TextView)view.findViewById(R.id.priority);
			viewHolder.remindBellImage = view.findViewById(R.id.remindBellImage);
//			viewHolder.finishSlipButton = (SlipButton)view.findViewById(R.id.finish);
			viewHolder.finishCheckBox = (CheckBox)view.findViewById(R.id.finish);
			viewHolder.enddateView = (TextView)view.findViewById(R.id.enddate);
			viewHolder.daysView = (TextView)view.findViewById(R.id.days);
			viewHolder.hoursView = (TextView)view.findViewById(R.id.hours);
			viewHolder.minutesView = (TextView)view.findViewById(R.id.minutes);
//			viewHolder.secondsView = (TextView)view.findViewById(R.id.seconds);
			viewHolder.expireView = (TextView)view.findViewById(R.id.expire);
			viewHolder.countdownLayout = view.findViewById(R.id.countdownLayout);
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
				String remindBell = c.getString(c.getColumnIndex(CountDown.REMIND_BELL));
				
				//show remindBell image
				if(remindBell == null || getResources().getString(R.string.mute).equals(remindBell)) {
					viewHolder.remindBellImage.setVisibility(View.GONE);
				} else {
					viewHolder.remindBellImage.setVisibility(View.VISIBLE);
				}
				
			    String state = c.getString(c.getColumnIndex(CountDown.STATE));
			    final int id = c.getInt(c.getColumnIndex(CountDown._ID));//primary key
			    //for delete alarm data after countdown finish
			    viewHolder._ID = id;
			    viewHolder.prefs = context.getSharedPreferences(Constant.ALARM_DATA_FILE, Context.MODE_PRIVATE).edit();
			    final String widgetIds = c.getString(c.getColumnIndex(CountDown.WIDGET_IDS));
/*			    viewHolder.finishSlipButton.SetOnChangedListener(new SlipButton.OnChangedListener() {
					
					@Override
					public void OnChanged(boolean checkState) {
						Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
						updateState(uri, checkState);
					}
				});
			    
			    if(CountDown.STATE_OPENED.equals(state)) {
			    	viewHolder.finishSlipButton.setCheck(true);//slipbutton check
			    } else {
			    	viewHolder.finishSlipButton.setCheck(false);//slipbutton uncheck
			    }
*/			    
			    //show checkbox
			    if(CountDown.STATE_OPENED.equals(state)) {
			    	viewHolder.finishCheckBox.setChecked(false);
			    } else if(CountDown.STATE_CLOSED.equals(state)){
			    	viewHolder.finishCheckBox.setChecked(true);
			    }
			    
			    boolean checked = viewHolder.finishCheckBox.isChecked();

			    if(checked) {
			    	//set clickable true
//			    	view.setClickable(true);
			    	view.setLongClickable(true);
			    	
			    	//if task finished,then disable checkbox
			    	viewHolder.finishCheckBox.setEnabled(false);
			    	viewHolder.countdownLayout.setVisibility(View.GONE);
			    	viewHolder.expireView.setVisibility(View.VISIBLE);
			    	viewHolder.expireView.setText(R.string.finished);
			    	
			    	//cancel all countdownTimers
			    	cancelAllCountDownTimers(timerHashMap);
			    } else {
			    	//set clickable false
//			    	view.setClickable(false);
			    	view.setLongClickable(false);
			    	
			    	//enable checkbox
			    	viewHolder.finishCheckBox.setEnabled(true);
			    	
			    	viewHolder.finishCheckBox.setOnClickListener(new OnClickListener() {
					
			    		@Override
						public void onClick(View v) {
							boolean isChecked = ((CheckBox) v).isChecked();
							 if(isChecked) {
								 //update state
//								 Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
//								 updateState(uri, isChecked);
//								 
//								//cancel alarm & notice widget if exists
//							     deleteOrFinish(id, widgetIds, Constant.FINISH_WIDGET);
//								
//							     Toast.makeText(getApplicationContext(), getResources().getString(R.string.finishTask) + title, Toast.LENGTH_SHORT).show();
							 }
						}
			    	});
			    }
			    
			    
			    //show priority
			    String priority = c.getString(c.getColumnIndex(CountDown.PRIORITY));
//			    setPriority(priority, viewHolder);
			    
			    //show countdown time
			    String endDate = c.getString(c.getColumnIndex(CountDown.END_DATE));
			    String endTime = StringUtil.underLineFilter(c.getString(c.getColumnIndex(CountDown.END_TIME)));
			    viewHolder.enddateView.setText(endDate + " " + endTime);
			    if(endDate != null && !"".equals(endDate) && !checked) {
			    	//createCountDownTimer(endDate, endTime, viewHolder, id);
			    	showCountDown(endDate, viewHolder);
			    }
			    
			}
			
		}
		
		private void showCountDown(String endDate, ViewHolder viewHolder) {
			int daysDiff = CountDownAppWidgetProvider.getDayDiff(endDate);
			if(Constant.ERROR_DAYS != daysDiff) {
				viewHolder.countdownLayout.setVisibility(View.VISIBLE);
				//hidden expire textview
				viewHolder.expireView.setVisibility(View.GONE);
				
		    	viewHolder.daysView.setText(daysDiff + "");
			} else {
				viewHolder.countdownLayout.setVisibility(View.VISIBLE);
				//hidden expire textview
				viewHolder.expireView.setVisibility(View.GONE);
				
		    	viewHolder.daysView.setText("出错了");
			}
		}
		
		/**
		 * create countDownTimer
		 * @param endDate
		 * @param endTime
		 * @param viewHolder
		 * @param id
		 */
		private void createCountDownTimer(String endDate, String endTime, ViewHolder viewHolder, int id) {
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
	}
		
		/**
		 * cancel all old countDownTimers
		 * @param timerHashMap
		 */
		private void cancelAllCountDownTimers(HashMap<Integer, MyCountDownTimer> timerHashMap) {
			Set<Integer> keySet = timerHashMap.keySet();
			Iterator<Integer> it = keySet.iterator();
			while(it.hasNext()) {
				Integer key = it.next();
				MyCountDownTimer timer = timerHashMap.get(key);
				if(timer != null) {
					timer.cancel();
				}
			}
			
			//clean all data
			timerHashMap.clear();
		}
		
		/**
		 * update state
		 */
		private void updateState(Uri uri, boolean checkState) {
			ContentValues values = new ContentValues();
//			if(checkState) {
//				values.put(CountDown.STATE, CountDown.STATE_CLOSED);
//				getContentResolver().update(uri, values, null, null);
//				
//			} else {
//				values.put(CountDown.STATE, CountDown.STATE_OPENED);
//				getContentResolver().update(uri, values, null, null);
//				
//			}
			
		}
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		}
		
	}

}
