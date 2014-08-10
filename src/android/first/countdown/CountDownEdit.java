package android.first.countdown;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.first.countdown.util.SharedPrefsUtil;
import android.first.countdown.util.StringUtil;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CountDownEdit extends Activity implements OnClickListener {
	
	/**
     * Standard projection for the interesting columns of a normal note.
     */
    private static final String[] PROJECTION = new String[] {
            CountDown._ID, // 0
            CountDown.TITLE, // 1
            CountDown.STARRED,
            CountDown.PRIORITY,
            CountDown.END_DATE,
            CountDown.END_TIME,
            CountDown.REMIND_DATE,
            CountDown.REMIND_BELL,
            CountDown.REMARK,
            CountDown.WIDGET_IDS,
            CountDown.TOP_INDEX
    };
	
	// The different distinct states the activity can be run in.
	private static final String TAG = "CountDownEdit";
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    private int mState;
    private Uri mUri;
    private Cursor mCursor;
    private TextView titleTextView;
    private TextView priorityTextView;
    private TextView endDateTextView;
    private TextView endTimeTextView;
    private TextView remindDateTextView;
    private TextView reminderTextView;
    private TextView remarkEditText;
    private Switch topSwitch;
    private int topSwitchText;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.countdown_edit);
		initViews();
		
		
		//select data from database
		Intent intent = getIntent();
		
		String action = intent.getAction();
		if(Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
			mUri = intent.getData();
		} else if(Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);
			
			// If we were unable to create a new countdown, then just finish
            // this activity.  A RESULT_CANCELED will be sent back to the
            // original activity if they requested a result.
            if (mUri == null) {
                Log.e(TAG, "Failed to insert new countdown into " + intent.getData());
                finish();
                return;
            }
		} else {
            // Whoops, unknown action!  Bail.
            Log.e(TAG, "Unknown action, exiting");
            finish();
            return;
        }
		
		// Get the countdown!
        mCursor = managedQuery(mUri, PROJECTION, null, null, null);
		
		//alert title dialog
		if(Intent.ACTION_INSERT.equals(action)) {
			titleDialog();
		}
		
		
	}
	
	private void initViews() {
		//add clickListener on element
		titleTextView = (TextView)findViewById(R.id.title);
		titleTextView.setOnClickListener(this);
		priorityTextView = (TextView)findViewById(R.id.priority);
		priorityTextView.setOnClickListener(this);
		endDateTextView = (TextView)findViewById(R.id.enddate);
		endDateTextView.setOnClickListener(this);
		endTimeTextView = (TextView)findViewById(R.id.endtime);
		endTimeTextView.setOnClickListener(this);
		remindDateTextView = (TextView)findViewById(R.id.remindFromDate);
		remindDateTextView.setOnClickListener(this);
		reminderTextView = (TextView)findViewById(R.id.reminder);
		reminderTextView.setOnClickListener(this);
		remarkEditText = (EditText)findViewById(R.id.remark);
		topSwitch = (Switch)findViewById(R.id.topSwitch);
		
		View addOkButton = findViewById(R.id.add_ok);
		addOkButton.setOnClickListener(this);
		View addCancelButton = findViewById(R.id.add_cancel);
		addCancelButton.setOnClickListener(this);
		View deleteButton = findViewById(R.id.delete_action);
		deleteButton.setOnClickListener(this);
		topSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked) {
					topSwitchText = Constant.TOP_SWITCH_ON;
				} else {
					topSwitchText = Constant.TOP_SWITCH_OFF;
				}
				
			}
			
		});
		findViewById(R.id.title_row).setOnClickListener(this);
		findViewById(R.id.type_row).setOnClickListener(this);
		findViewById(R.id.enddate_row).setOnClickListener(this);
				
	}
	
	@Override
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
	}
	 
	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart");
		super.onRestart();
	}
	
	@Override
    protected void onResume() {
		Log.i(TAG, "onResume");
        super.onResume();
        // The activity has become visible (it is now "resumed").
        if(mCursor != null) {
            if(mState == STATE_EDIT) {
            	// Make sure we are at the one and only row in the cursor.
                mCursor.moveToFirst();
                
                titleTextView.setText(mCursor.getString(mCursor.getColumnIndex(CountDown.TITLE)));
                priorityTextView.setText(mCursor.getString(mCursor.getColumnIndex(CountDown.PRIORITY)));
                String endDate = mCursor.getString(mCursor.getColumnIndex(CountDown.END_DATE));
                if(endDate != null && !"".equals(endDate)) {
                	endDateTextView.setText(endDate);
                }
                
                String endTime = mCursor.getString(mCursor.getColumnIndex(CountDown.END_TIME));
                if(endTime != null && !"".equals(endTime)) {
                	endTimeTextView.setText(endTime);
                }
                String remindDate = mCursor.getString(mCursor.getColumnIndex(CountDown.REMIND_DATE));
                if(remindDate != null && !"".equals(remindDate)) {
                	remindDateTextView.setText(remindDate);
                }
                
                String reminder = mCursor.getString(mCursor.getColumnIndex(CountDown.REMIND_BELL));
                if(reminder != null && !"".equals(reminder)) {
                	reminderTextView.setText(reminder);
                }
//                if(CountDown.DEFAUL_BELL.equals(mCursor.getString(mCursor.getColumnIndex(CountDown.REMIND_BELL)))) {
//                	remindBellSlipButton.setCheck(true);
//                } else {
//                	remindBellSlipButton.setCheck(false);
//                }
                remarkEditText.setText(mCursor.getString(mCursor.getColumnIndex(CountDown.REMARK)));
                
                int topIndex = mCursor.getInt(mCursor.getColumnIndex(CountDown.TOP_INDEX));
                if(Constant.TOP_SWITCH_ON == topIndex) {
                	topSwitch.setChecked(true);
                } else {
                	topSwitch.setChecked(false);
                }
            }
        } else {
        	Toast.makeText(getApplicationContext(), R.string.loadDataError, Toast.LENGTH_SHORT).show();
        }
        
    }
	
	@Override
    protected void onPause() {
		Log.i(TAG, "onPause");
        super.onPause();
        cancel();
	}
	
	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
        super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
        super.onDestroy();
	}
	
	/**
	 * show a dialog to input title,click Ok to save and click Cancel to quit
	 */
	private void titleDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    final View layout = inflater.inflate(R.layout.countdown_add_title, null);
	    final EditText inputTitleEditText = (EditText)layout.findViewById(R.id.input_title);
	    String currentTitle = titleTextView.getText().toString();
		if(currentTitle != null && !"".equals(currentTitle)) {
			inputTitleEditText.setText(currentTitle);
		}
	    
	    builder.setView(layout)
	    .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String title = inputTitleEditText.getText().toString();
				if(!"".equals(title) && title != null) {
					titleTextView.setText(title);
				} else {
					titleTextView.setText(Constant.UN_NAMED_TITLE);
				}
			}
			
	    }).setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				//go back ItemList
				cancel();
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
	
	private void showDeleteConfirmDialog(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View layout = inflater.inflate(R.layout.confirm_dialog, null);
	    TextView confirmTitle = (TextView)layout.findViewById(R.id.confirmTitle);
	    confirmTitle.setText(title); 
	    
	    builder.setView(layout)
	    .setPositiveButton("É¾³ý", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteAction();
			}
			
	    }).setNegativeButton("·ÅÆú", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
	
	private void priorityDialog() {
		//when edit,show the current priority
		int checkedIndex = 0;
	    String[] priority = SharedPrefsUtil.getAllTypes(this);
	    final String[] usedPriority = new String[priority.length -1];

		String currentPriority = priorityTextView.getText().toString();
		if(currentPriority != null && !"".equals(currentPriority)) {
			for(int i = 1; i < priority.length; i++) {
				int currentIndex = i - 1;
				usedPriority[currentIndex] = priority[i];//remove the first item--all
				if(currentPriority.equals(priority[i])) {
					checkedIndex = currentIndex;
				}
			}
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_type_dialog).
		setSingleChoiceItems(usedPriority, checkedIndex, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				priorityTextView.setText(usedPriority[which]);
				dialog.dismiss();
			}
		});
		
		Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}
	
//	private void bellDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		LayoutInflater inflater = this.getLayoutInflater();
//		View layout = inflater.inflate(R.layout.bell_dialog, null);
//		final CheckBox vibrator = (CheckBox)layout.findViewById(R.id.vibrator);
//		final RadioButton mute = (RadioButton)layout.findViewById(R.id.mute);
//		final RadioButton bell1 = (RadioButton)layout.findViewById(R.id.bell1);
//		final RadioButton bell2 = (RadioButton)layout.findViewById(R.id.bell2);
//		final RadioButton bell3 = (RadioButton)layout.findViewById(R.id.bell3);
//		
//		String reminder = reminderTextView.getText().toString();
//		if(reminder != null && !"".equals(reminder)) {
//			String[] arr = reminder.split("\\+");
//			if(arr.length == 1) {
//				if(getResources().getString(R.string.mute).equals(arr[0])) {
//					mute.setChecked(true);
//				} else if(getResources().getString(R.string.bell1).equals(arr[0])) {
//					bell1.setChecked(true);
//				} else if(getResources().getString(R.string.bell2).equals(arr[0])) {
//					bell2.setChecked(true);
//				} else if(getResources().getString(R.string.bell3).equals(arr[0])) {
//					bell3.setChecked(true);
//				} 
//				
//				if(getResources().getString(R.string.vibrator).equals(arr[0])) {
//					vibrator.setChecked(true);
//				} else {
//					vibrator.setChecked(false);
//				}
//			} else if(arr.length == 2) {
//				vibrator.setChecked(true);
//				
//				if(getResources().getString(R.string.mute).equals(arr[1])) {
//					mute.setChecked(true);
//				} else if(getResources().getString(R.string.bell1).equals(arr[1])) {
//					bell1.setChecked(true);
//				} else if(getResources().getString(R.string.bell2).equals(arr[1])) {
//					bell2.setChecked(true);
//				} else if(getResources().getString(R.string.bell3).equals(arr[1])) {
//					bell3.setChecked(true);
//				}
//				
//			}
//		}
//		
//		builder.setTitle(R.string.select_reminder);
//		builder.setView(layout);
//		builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int arg1) {
//				StringBuilder sb = new StringBuilder();
//				if(vibrator.isChecked()) {
//					sb.append(vibrator.getText()).append("+");
//				}
//				if(mute.isChecked()) {
//				    sb.append(mute.getText());
//				} else if(bell1.isChecked()) {
//					sb.append(bell1.getText());
//				} else if(bell2.isChecked()) {
//					sb.append(bell2.getText());
//				} else if(bell3.isChecked()) {
//					sb.append(bell3.getText());
//				}
//				
//				reminderTextView.setText(sb.toString());
//			}
//			
//		}).setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				
//			}
//			
//		});
//		
//		Dialog d = builder.create();
//		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
//		d.show();
//	}
	
	/**
	 * show datepicker dialog according to id of the element
	 * @param id
	 */
	private void datePicker(int id) {
		// Use the current date as the default date in the picker
		int year = 0;
        int month = 0;
        int day = 0;
        
        final TextView dateTextView = (TextView)findViewById(id);
		String  date = dateTextView.getText().toString();
		
		boolean flag = false;
		if(date != null && !"".equals(date)) {
			String[] str = date.split("-");
			if(str.length != 3) {
				flag = true;
			} else {
				year = Integer.parseInt(str[0]);
				month = Integer.parseInt(str[1]) - 1;
				day = Integer.parseInt(str[2]);
			}
		} else {
			flag = true;
		}
		
		if(flag) {
			Calendar c = Calendar.getInstance();
	        year = c.get(Calendar.YEAR);
	        month = c.get(Calendar.MONTH);
	        day = c.get(Calendar.DAY_OF_MONTH);
		}
        

        // Create a new instance of DatePickerDialog and return it
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar cal = Calendar.getInstance();
				cal .set(Calendar. YEAR , year);
				cal .set(Calendar. MONTH , monthOfYear);
				cal .set(Calendar. DAY_OF_MONTH , dayOfMonth);
				
/*				if(cal.getTimeInMillis() < System.currentTimeMillis() ) {
					Toast.makeText(getApplicationContext(), R.string.endLessThanCurrent, Toast.LENGTH_SHORT).show();
					return;
				} else {
					//update enddate textview
					SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
					dateTextView.setText(df.format(cal.getTime()));
				}
*/				
				//update enddate textview
				SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
				dateTextView.setText(df.format(cal.getTime()));
			}
        	
        }, year, month, day).show();
	}
	
	/**
	 * show timepicker dialog
	 */
	private void timePicker(int id) {
		String endDate = endDateTextView.getText().toString();
		if(endDate == null || "".equals(endDate)) {
			Toast.makeText(getApplicationContext(), R.string.endTimeWithoutEndDate, Toast.LENGTH_SHORT).show();
			return;
		}
		// Use the current time as the default values for the picker
		int hour = 0;
		int minute = 0;
		
		final TextView timeTextView = (TextView)findViewById(id);
		String time = timeTextView.getText().toString();
		
		boolean flag = false;//time has some wrong,then choose current time
		if(time != null && !"".equals(time)) {
			/*String[] str1 = time.split(" ");
			if(str1.length != 2) {
				flag = true;
			} else {
				String[] str2 = str1[0].split(":");
				if(str2.length != 2) {
					flag = true;
				} else {
					hour = Integer.parseInt(str2[0]);
					minute = Integer.parseInt(str2[1]);
				}
			}*/
			String[] str2 = time.split(":");
			if(str2.length != 2) {
				flag = true;
			} else {
				hour = Integer.parseInt(str2[0]);
				minute = Integer.parseInt(str2[1]);
			}
		} else {
			flag = true;
		}
		
		if(flag) {
			Calendar c = Calendar.getInstance();
	        hour = c.get(Calendar.HOUR_OF_DAY);
	        minute = c.get(Calendar.MINUTE);
		}

//		final java.text.DateFormat df = DateFormat.getTimeFormat(this);
//		final DateFormat df1 = DateFormat.getTimeInstance(DateFormat.SHORT);
//		final DateFormat df2 = DateFormat.getTimeInstance(DateFormat.MEDIUM);
//		final DateFormat df3 = DateFormat.getTimeInstance(DateFormat.LONG);
        // Create a new instance of TimePickerDialog and return it
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//				Calendar cal = Calendar.getInstance();
//				cal .set(Calendar.HOUR_OF_DAY , hourOfDay);
//				cal .set(Calendar.MINUTE , minute);
				//update enddate textview
				
//				String d1 = df1.format(cal.getTime());
//				String
				StringBuilder sb = new StringBuilder();
				sb.append(hourOfDay).append(":");
				if(minute < 10) {
					sb.append("0").append(minute);
				} else {
					sb.append(minute);
				}
				
				timeTextView.setText(sb.toString());
			}
        	
        }, hour, minute,true).show();
	}
	
	/**
	 * select bell to remind when today is reminder date
	 */
/*	private void selectRemindBell() {
		Intent intent = new Intent(this, SdCardFileList.class);
		startActivity(intent);
	}
*/	
	/**
	 * add or modify a countdown record
	 */
	private void save() {
		if(mCursor != null) {
			//title must not be null or empty
			String title = titleTextView.getText().toString();
			if(title == null || "".equals(title)) {
				Toast.makeText(getApplicationContext(), R.string.titleMust, Toast.LENGTH_SHORT).show();
				return;
			}
			
			//enddate must not be null or empty
			String endDate = endDateTextView.getText().toString();
			if(endDate == null || "".equals(endDate) || getResources().getString(R.string.default_underline).equals(endDate)) {
				Toast.makeText(getApplicationContext(), R.string.endDateMust, Toast.LENGTH_SHORT).show();
				return;
			}
			// Get out updates into the provider.
			ContentValues values = new ContentValues();
			
			values.put(CountDown.TITLE, title);
			values.put(CountDown.PRIORITY, priorityTextView.getText().toString());
			values.put(CountDown.END_DATE, endDate);
//			values.put(CountDown.END_TIME, StringUtil.underLineFilter(endTimeTextView.getText().toString()));
//			values.put(CountDown.REMIND_DATE, StringUtil.underLineFilter(remindDateTextView.getText().toString()));
			values.put(CountDown.REMIND_BELL, reminderTextView.getText().toString());
			values.put(CountDown.TOP_INDEX, topSwitchText);
			if(Constant.TOP_SWITCH_ON.equals(topSwitchText)) {
				//update all the data, set the top_switch=0
				ContentValues contentValues2 = new ContentValues();
				contentValues2.put(CountDown.TOP_INDEX, Constant.TOP_SWITCH_OFF);
				updateAllData(contentValues2);
			}
//			if(remindBellSlipButton.isChecked()) {
//				values.put(CountDown.REMIND_BELL, CountDown.DEFAUL_BELL);
//			} else {
//				values.put(CountDown.REMIND_BELL, "");
//			}
			values.put(CountDown.REMARK, remarkEditText.getText().toString());
//			values.put(CountDown.CREATED_DATE, System.currentTimeMillis());//current time
			
			int count = getContentResolver().update(mUri, values, null, null);
			
			if(count == 1) {
	            //set countdown reminder
//	            setCountDownReminder();
	            
	            if(mState == STATE_EDIT) {
	            	//update widget 
		            updateWidget();
	            }
	            
				Toast.makeText(getApplicationContext(), R.string.save_successfully, Toast.LENGTH_SHORT).show();
				
				mCursor.close();
				mCursor = null;
				
				//return to main page
				finish();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
			
		}
		
	}
	
	private void updateAllData(ContentValues contentValues) {
		Cursor cursor = managedQuery(CountDown.CONTENT_URI, new String[] {CountDown._ID}, null, null, null); 
		while(cursor.moveToNext()) {
			Integer id = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
			Uri uri = ContentUris.withAppendedId(CountDown.CONTENT_URI, id);
			getContentResolver().update(uri, contentValues, null, null);
		}
	}
	
	/**
	 * cancel modify
	 */
	private void cancel() {
		if(mCursor != null) {
			if (mState == STATE_EDIT) {
				//TODO do nothing
			} else if(mState == STATE_INSERT) {
				deleteRecord();
			}
			
			//shutdown this activity
			finish();
			//to main page
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * delete countdown record
	 */
	private int deleteRecordAndWidgets(Cursor cursor) {
		int result = getContentResolver().delete(mUri, null, null);
		String widgetIds = cursor.getString(cursor.getColumnIndex(CountDown.WIDGET_IDS));
		if(result == 1) {
			//notice widget to delete itself
			if(widgetIds != null && !"".equals(widgetIds)) {
				String[] appWidgetIds = widgetIds.split(",");
				for(String mAppWidgetId : appWidgetIds) {
					Intent intent = new Intent(Constant.DELETE_WIDGET);
					Bundle extras = new Bundle();
					extras.putString(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
					intent.putExtras(extras);
					this.sendBroadcast(intent);
				}
			}
						
		} else {
			Toast.makeText(getApplicationContext(), R.string.deleteError, Toast.LENGTH_SHORT).show();
		}
		
		return result;
	}
	
	/**
	 * delete action
	 */
	private void deleteAction() {
		if(deleteRecordAndWidgets(mCursor) == 1) {
			finish();
			Toast.makeText(this, R.string.operationSuccess, Toast.LENGTH_SHORT).show();
			//to main page
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, R.string.deleteError, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * delete countdown record
	 */
	private int deleteRecord() {
		if (mCursor != null) {
            return getContentResolver().delete(mUri, null, null);
        }
		return -1;
	}
	
	/**
	 * set countdown reminder
	 */
//	private void setCountDownReminder() {
//		if(mCursor != null && mCursor.moveToFirst()) {
//			Integer _ID = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(CountDown._ID)));
////			Integer _ID = mCursor.getInt(mCursor.getColumnIndex(CountDown._ID));
//			String title = titleTextView.getText().toString();
//			String endDate = endDateTextView.getText().toString();
//			String endTime = StringUtil.underLineFilter(endTimeTextView.getText().toString());
//			String remindDate = StringUtil.underLineFilter(remindDateTextView.getText().toString());
//			String reminder = reminderTextView.getText().toString();
//			
//		    Calendar calendar = Calendar.getInstance();
//		    if(endDate != null && !"".equals(endDate)) {
//		    	
//		    	String[] date = endDate.split("-");
//		    	if(date.length == 3) {
//		    		if(endTime != null && !"".equals(endTime)) {
//				    	String[] time = endTime.split(":");
//				    	if(time.length == 2) {
//			    			calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
//			        				Integer.parseInt(time[0]), Integer.parseInt(time[1]));
//			    		}
//
//				  } else {
////					    calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
////		        				0, 0);
//					    calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
//				  }
//		    	}			    	
//		    	 
////		    	//set countdown reminder
////
////				Intent intent = new Intent(this, AlarmReceiver.class);
////				
////				//transfer data to receiver
////				Bundle extras = new Bundle();
////				extras.putInt(CountDown._ID, _ID);
////				extras.putString(CountDown.TITLE, title);
////				extras.putString(CountDown.REMIND_BELL, reminder);
////				extras.putString(CountDown.END_DATE, endDate);
////				
////				intent.putExtras(extras);
//				
//		        PendingIntent sender = PendingIntent.getBroadcast(this,
//		        		_ID, setIntentForAlarm(this, _ID, title, reminder ,endDate), 
//		        		PendingIntent.FLAG_UPDATE_CURRENT);
//		        // Schedule the alarm!
//		        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//		        long firstTime = 0;//remind time
//		        if(remindDate == null || "".equals(remindDate)) {
//		        	 firstTime = calendar.getTimeInMillis();
//		        	 am.set(AlarmManager.RTC_WAKEUP, firstTime, sender);
//		        } else {
//		        	String[] str = remindDate.split("-");
//		        	if(str.length == 3) {
//		        		Calendar remind = Calendar.getInstance();
//		        		remind.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) -1, Integer.parseInt(str[2]),
//		        				11, 50, 0);
//		        		
//		        		System.out.println(remind.getTime().toLocaleString());
//		        		System.out.println(calendar.getTime().toLocaleString());
//		        		
//		        		long currentTime = System.currentTimeMillis();
//		        		long remindTime = remind.getTimeInMillis();
//		        		long endDateTime = calendar.getTimeInMillis();
//		        		
//		        		if(endDateTime < remindTime) {
////		        			firstTime = endDateTime - currentTime ;
//		        			firstTime = endDateTime;
//		        		}else {
////		        			firstTime = remindTime - currentTime ;
//		        			firstTime = remindTime;
//		        		}
//		        		
//		        		//remind every day from remindDate to endDate
//			            am.setRepeating(AlarmManager.RTC_WAKEUP,
//			            		firstTime, AlarmManager.INTERVAL_DAY, sender);
//			            
//		        	}
//		        }
//		        
//		        //save alarm data for boot_up
//		        saveAlarmData(_ID, true, firstTime, title, reminder, endDate);
//
//		    }
//		}
//		
//	}
	
//	public static Intent setIntentForAlarm(Context context , int _ID, String title, String reminder, String endDate) {
//		Intent intent = new Intent(context, AlarmReceiver.class);
//		
//		//transfer data to receiver
//		Bundle extras = new Bundle();
//		extras.putInt(CountDown._ID, _ID);
//		extras.putString(CountDown.TITLE, title);
//		extras.putString(CountDown.REMIND_BELL, reminder);
//		extras.putString(CountDown.END_DATE, endDate);
//		
//		intent.putExtras(extras);
//		
//		return intent;
//	} 
	
	/**
	 * save alarm data for system boot-up
	 * @param _ID
	 * @param remindDate
	 * @param firstTime
	 */
	/**private void saveAlarmData(int _ID, boolean remindDate, long firstTime,
			String title, String reminder, String endDate) {
		SharedPreferences.Editor prefs = this.getSharedPreferences(Constant.ALARM_DATA_FILE, Context.MODE_PRIVATE).edit();

		StringBuilder sb = new StringBuilder();
		sb.append(_ID).append(Constant.SPLIT_SEMICOLON).append(remindDate).
		append(Constant.SPLIT_SEMICOLON).append(firstTime).append(Constant.SPLIT_SEMICOLON);
		sb.append(title).append(Constant.SPLIT_SEMICOLON).append(reminder).
		append(Constant.SPLIT_SEMICOLON).append(endDate);
		
		prefs.putString(StringUtil.appendAppWidgetId(CountDown._ID, _ID), sb.toString());
		
		prefs.commit();
	}**/
	
	private void updateWidget() {
		if(mCursor != null) {
			//save update data
			String widgetIds = mCursor.getString(mCursor.getColumnIndex(CountDown.WIDGET_IDS));
			if(widgetIds == null || "".equals(widgetIds)) {
				return;
			} 
			
			String[] appWidgetIds = widgetIds.split(",");
			
			int _ID = mCursor.getInt(mCursor.getColumnIndex(CountDown._ID));
			for(String mAppWidgetId : appWidgetIds) {
				String title = titleTextView.getText().toString();
				String priority = priorityTextView.getText().toString();
				String endDate = endDateTextView.getText().toString();
				//String endTime = StringUtil.underLineFilter(endTimeTextView.getText().toString());
				//save data for widget update
				WidgetConfigure.saveToPreference(this.getApplicationContext(), Integer.parseInt(mAppWidgetId), _ID, title, null, endDate, priority);
				
				//create broadcast to update widget
				Intent intent = new Intent(Constant.UPDATE_WIDGET);
				Bundle extras = new Bundle();
				extras.putString(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				intent.putExtras(extras);
				
				this.sendBroadcast(intent);
			}
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_row:
			titleDialog();
			break;
		case R.id.type_row:
			priorityDialog();
			break;
		case R.id.enddate_row:
			datePicker(R.id.enddate);
			break;
		case R.id.endtime:
			timePicker(R.id.endtime);
			break;
		case R.id.remindFromDate:
			datePicker(R.id.remindFromDate);
			break;
//		case R.id.reminder:
//			bellDialog();
//			break;
		case R.id.add_cancel:
			cancel();
			break;
		case R.id.add_ok:
			save();
			break;
		case R.id.delete_action:
			showDeleteConfirmDialog(Constant.DELETE_TASK_CONFIRM);
			break;
		default:
			break;
		}
	}
}
