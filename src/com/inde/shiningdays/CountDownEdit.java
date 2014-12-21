package com.inde.shiningdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.*;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inde.shiningdays.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import org.w3c.dom.Text;

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
//    private TextView endTimeTextView;
//    private TextView remindDateTextView;
	private TextView remindSettingTextView;
	private TextView remindSettingShowTextView;
	private View remindSettingDialog;
    private TextView remarkEditText;
    private Switch topSwitch;
    private int topSwitchText;
    private View shareLayout;
    private View deleteButton;
    
    private UMSocialService mController;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.countdown_edit);
		
		initViews();
		
		//start update widget service
        startUpdateWidgetService();
		
		//select data from database
		Intent intent = getIntent();
		
		String action = intent.getAction();
		if(Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
			mUri = intent.getData();
			//show share button
			shareLayout.setVisibility(View.VISIBLE);
			//show delete button
			deleteButton.setVisibility(View.VISIBLE);
		} else if(Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);
			//hide share button
			shareLayout.setVisibility(View.GONE);
			//hide delete button
			deleteButton.setVisibility(View.INVISIBLE);
			
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
	
	private void startUpdateWidgetService() {
    	Intent i = new Intent();
		i.setClass(this, UpdateWidgetService.class);
		startService(i);
    }
	
	/**
	 * init views when open the activity
	 */
	private void initViews() {
		//add clickListener on element
		titleTextView = (TextView)findViewById(R.id.title);
		titleTextView.setOnClickListener(this);
		priorityTextView = (TextView)findViewById(R.id.priority);
		//priorityTextView.setOnClickListener(this);
		endDateTextView = (TextView)findViewById(R.id.enddate);
		//endDateTextView.setOnClickListener(this);
		//endTimeTextView = (TextView)findViewById(R.id.endtime);
		//endTimeTextView.setOnClickListener(this);
		//remindDateTextView = (TextView)findViewById(R.id.remindFromDate);
		//remindDateTextView.setOnClickListener(this);
		remindSettingTextView = (TextView)findViewById(R.id.remind_setting);
		remindSettingShowTextView = (TextView)findViewById(R.id.remind_setting_show);
		//remindTextView.setOnClickListener(this);
		remarkEditText = (EditText)findViewById(R.id.remark);
		topSwitch = (Switch)findViewById(R.id.topSwitch);
		
		View addOkButton = findViewById(R.id.add_ok);
		addOkButton.setOnClickListener(this);
		View addCancelButton = findViewById(R.id.add_cancel);
		addCancelButton.setOnClickListener(this);
		View shareButton = findViewById(R.id.share_action);
		shareButton.setOnClickListener(this);
		shareLayout = findViewById(R.id.share_layout);
		deleteButton = findViewById(R.id.delete_action);
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
		findViewById(R.id.remind_setting_row).setOnClickListener(this);
		
     //????1????Activity?? ????2?????????QQ?????????APP ID??????3?????????QQ?????????APP kEY.
//       UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
//                       "c7394704798a158208a74ab60104f0ba");
//       qqSsoHandler.addToSocialSDK();  
				
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        //umeng sdk
        MobclickAgent.onResume(this);
        
        // The activity has become visible (it is now "resumed").
        if(mCursor != null) {
        	if(mState == STATE_INSERT) {
        		Intent intent = this.getIntent();
        		Bundle bundle = intent.getExtras();
        		if(bundle != null) {
        			String mType = bundle.getString(CountDown.PRIORITY);
        			if(mType != null && !"".equals(mType) && !getResources().getString(R.string.type_all).equals(mType)) {
        				priorityTextView.setText(mType);
        			}
        		}
        	} else if(mState == STATE_EDIT) {
            	// Make sure we are at the one and only row in the cursor.
                mCursor.moveToFirst();
                
                titleTextView.setText(mCursor.getString(mCursor.getColumnIndex(CountDown.TITLE)));
                priorityTextView.setText(mCursor.getString(mCursor.getColumnIndex(CountDown.PRIORITY)));
                String endDate = mCursor.getString(mCursor.getColumnIndex(CountDown.END_DATE));
                if(endDate != null && !"".equals(endDate)) {
                	endDateTextView.setText(endDate);
                }
                
                /**
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
                }**/
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

				String remindSetting = mCursor.getString(mCursor.getColumnIndex(CountDown.REMIND_DATE));
				if(remindSetting != null) {
					String[] arr = remindSetting.split(";");
					if(arr.length == 2) {
						remindSettingTextView.setText(remindSetting);
						remindSettingShowTextView.setText(getRemindSettingShowText(arr[0], arr[1]));
					}

				}
            }
        } else {
        	Toast.makeText(getApplicationContext(), R.string.loadDataError, Toast.LENGTH_SHORT).show();
        }
        
    }
	
	@Override
    protected void onPause() {
        super.onPause();
        //umeng sdk
        MobclickAgent.onPause(this);
        cancel();
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
	    
	    builder.setView(layout).setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String title = inputTitleEditText.getText().toString();
				if(!"".equals(title) && title != null) {
					titleTextView.setText(title.trim());
				} else {
					titleTextView.setText(getResources().getString(R.string.unamed_title));
				}
			}
	    }).setNegativeButton(R.string.go_back_label, new DialogInterface.OnClickListener(){

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
	
	/**
	 * show the delete cofirm dialog when click delete button
	 * @param title
	 */
	private void showDeleteConfirmDialog(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View layout = inflater.inflate(R.layout.confirm_dialog, null);
	    TextView confirmTitle = (TextView)layout.findViewById(R.id.confirmTitle);
	    confirmTitle.setText(title); 
	    
	    builder.setView(layout)
	    .setPositiveButton(R.string.confirm_delete_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteAction();
			}
			
	    }).setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
	
	/**
	 * show the type list dialog (use the old field "priority" as "type")
	 */
	private void priorityDialog() {
		//when edit,show the current priority
		int checkedIndex = 0;
	    String[] priority = Utils.getAllTypes(this);
	    final String[] usedPriority = new String[priority.length -1];

		String currentPriority = priorityTextView.getText().toString();
		for(int i = 1; i < priority.length; i++) {
			int currentIndex = i - 1;
			usedPriority[currentIndex] = priority[i];//remove the first item--all
			if(currentPriority != null && currentPriority.equals(priority[i])) {
				checkedIndex = currentIndex;
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

	private void remindSettingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();
		remindSettingDialog = inflater.inflate(R.layout.remind_setting_dialog, null);
		String remindSetting = remindSettingTextView.getText().toString();
		final EditText remindDayEditText = (EditText)remindSettingDialog.findViewById(R.id.remind_day);
		final TextView remindHourTextView = (TextView)remindSettingDialog.findViewById(R.id.remind_hour);
		final TextView remindHourShowText = (TextView)remindSettingDialog.findViewById(R.id.remind_hour_show_text);
		remindHourShowText.setText(this.getRemindHourShowText(this, getResources().
				getString(R.string.default_remind_hour).toString()));
		if(remindSetting != null && !"".equals(remindSetting)) {
			String[] arr = remindSetting.split(";");
			if(arr.length == 2) {
				remindDayEditText.setText(arr[0]);
				remindHourTextView.setText(arr[1]);
				remindHourShowText.setText(this.getRemindHourShowText(this, arr[1]));
			}
		}
		remindSettingDialog.findViewById(R.id.remind_hour_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				timePicker(remindHourTextView, remindHourShowText);
				remindDayEditText.clearFocus();
			}
		});
		builder.setView(remindSettingDialog).setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (remindDayEditText.getText() == null || "".equals(remindDayEditText.getText().toString())) {
					remindDayEditText.setText(getResources().getString(R.string.default_remind_day));//default = 1
				}
				remindSettingTextView.setText(remindDayEditText.getText() + ";" + remindHourTextView.getText());
				remindSettingShowTextView.setText(getRemindSettingShowText(remindDayEditText.getText().toString(),
						remindHourTextView.getText().toString()));

			}
		}).setNegativeButton(R.string.go_back_label, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}

	/**
	private void showHourDialog() {
		int checkedIndex = 0;
		final String[] hours = getResources().getStringArray(R.array.remind_hours_array);

		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.remind_setting_dialog, null);
		final TextView remindHourTextView = (TextView)layout.findViewById(R.id.remind_hour);
		final TextView remindHourShowText = (TextView)layout.findViewById(R.id.remind_hour_show_text);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_hour_dialog).
				setSingleChoiceItems(hours, checkedIndex, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						priorityTextView.setText(hours[which]);

						dialog.dismiss();
					}
				});

		Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}**/

	private String getRemindSettingShowText(String day, String hour) {
		return getResources().getString(R.string.remind_label1) + day +
				getResources().getString(R.string.remind_label2)+ hour +
				getResources().getString(R.string.remind_label3);
	}


	/**
	private void remindDialog() {
		String currentRemind = remindTextView.getText().toString();
		final String[] remindArray = this.getResources().getStringArray(R.array.remind);
		int checkedIndex = 0;
		for (int i = 0; i < remindArray.length; i++) {
			if(currentRemind != null && currentRemind.equals(remindArray[i])) {
				checkedIndex = i;
				break;
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_type_dialog).
				setSingleChoiceItems(remindArray, checkedIndex, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						remindTextView.setText(remindArray[which]);
						dialog.dismiss();
					}
				});

		Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}**/
	
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
	 * show timepicker dialog, unused 
	 */
	private void timePicker(final TextView remindHourTextView, final TextView remindHourShowText) {
		//LayoutInflater inflater = this.getLayoutInflater();
		 //inflater.inflate(R.layout.remind_setting_dialog, null);
		//final TextView remindHourTextView = (TextView)remindSettingDialog.findViewById(R.id.remind_hour);
		//final TextView remindHourShowText = (TextView)remindSettingDialog.findViewById(R.id.remind_hour_show_text);

		// Use the current time as the default values for the picker
		int hour = 0;
		int minute = 0;
		
		//final TextView timeTextView = (TextView)findViewById(id);
		String time = remindHourTextView.getText().toString();
		
		boolean flag = false;//time has some wrong,then choose current time
		if(time != null && !"".equals(time)) {
			String[] arr = time.split(":");
			if(arr.length != 2) {
				flag = true;
			} else {
				hour = Integer.parseInt(arr[0]);
				minute = Integer.parseInt(arr[1]);
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
				StringBuilder sb = new StringBuilder();
				sb.append(hourOfDay).append(":");
				if(minute < 10) {
					sb.append("0").append(minute);
				} else {
					sb.append(minute);
				}

				remindHourTextView.setText(sb.toString());
				remindHourShowText.setText(getRemindHourShowText(view.getContext(), sb.toString()));
			}
        	
        }, hour, minute,true).show();
	}

	private String getRemindHourShowText(Context context, String content) {
		return context.getResources().getString(R.string.at_remind_hour) + content;
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
			String remindSetting = remindSettingTextView.getText().toString();
			String remindSettingShowText = remindSettingShowTextView.getText().toString();
			if(remindSetting != null && !"".equals(remindSetting) &&
					!getResources().getString(R.string.remind_default).equals(remindSettingShowText)) {
				values.put(CountDown.REMIND_DATE, remindSetting);
			}
//			values.put(CountDown.REMIND_BELL, reminderTextView.getText().toString());
			values.put(CountDown.TOP_INDEX, topSwitchText);
			if(Constant.TOP_SWITCH_ON.equals(topSwitchText)) {
				//update all the data, set the top_switch=0
				ContentValues contentValues2 = new ContentValues();
				contentValues2.put(CountDown.TOP_INDEX, Constant.TOP_SWITCH_OFF);
				//first, update all the data topindex with value:0, then update current data topindex with value:1
				updateAllData(contentValues2);
			}

			values.put(CountDown.REMARK, remarkEditText.getText().toString());
			values.put(CountDown.CREATED_DATE, System.currentTimeMillis());//created time
			
			int count = getContentResolver().update(mUri, values, null, null);
			if(count == 1) {
	            //set countdown reminder
	            setCountDownReminder();
	            
	            if(mState == STATE_EDIT) {
	            	//update widget 
		            updateWidgets();
	            }
	            
				Toast.makeText(getApplicationContext(), R.string.save_successfully, Toast.LENGTH_SHORT).show();
				mCursor.close();
				mCursor = null;
				
				//return to main page
				toMainActivity();
			}
			
		}
		
	}

	private String getRemindDate(String remindSetting, String endDate) {
		String[] arr = remindSetting.split(";");
		String result = null;
		if(arr.length == 2) {
			try{
				int day = Integer.parseInt(arr[0]);
				String[] hourArr = arr[1].split(":");
				int hour = 0;
				int minute = 0;
				if(hourArr.length == 2) {
					hour = Integer.parseInt(hourArr[0]);
					minute = Integer.parseInt(hourArr[1]);
				}

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(sdf.parse(endDate));
				cal.add(Calendar.DATE, -day);
				long time = cal.getTimeInMillis() + hour * 60 * 60 * 1000 + minute * 60 * 1000;
				cal.setTimeInMillis(time);
				Date dateBefore = cal.getTime();
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				result = sdf.format(dateBefore);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * update all data's topindex field with the same value : Constant.TOP_SWITCH_OFF
	 * @param contentValues
	 */
	private void updateAllData(ContentValues contentValues) {
		/**
		Cursor cursor = managedQuery(CountDown.CONTENT_URI, new String[] {CountDown._ID}, null, null, null); 
		while(cursor.moveToNext()) {
			Integer id = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
			Uri uri = ContentUris.withAppendedId(CountDown.CONTENT_URI, id);
			getContentResolver().update(uri, contentValues, null, null);
		}**/
		
		//more simple code
		getContentResolver().update(CountDown.CONTENT_URI, contentValues, null, null);
	}
	
	/**
	 * cancel modify
	 */
	private void cancel() {
		if(mCursor != null) {
			if (mState == STATE_EDIT) {
			} else if(mState == STATE_INSERT) {
				deleteRecord();
			}
		}
		
		toMainActivity();
	}
	
	/**
	 * return to MainActivity
	 */
	private void toMainActivity() {
		//shutdown this activity
		finish();
		//to main page
		/**Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(CountDown.PRIORITY, priorityTextView.getText());
		startActivity(intent);**/
	}
	
	
	/**
	 * delete action
	 */
	private void deleteAction() {
		if(deleteRecordAndWidgets(mCursor) > 0) {
			toMainActivity();
			Toast.makeText(this, R.string.operationSuccess, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.deleteError, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * delete countdown record
	 */
	private int deleteRecordAndWidgets(Cursor cursor) {
		int result = getContentResolver().delete(mUri, null, null);
		if(result > 0) {
			//notice widget to delete itself
			String widgetIds = cursor.getString(cursor.getColumnIndex(CountDown.WIDGET_IDS));
			
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

			//cancel relative remind
			Integer id = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
			deleteAlarmDataAndCancelAlarm(this , id);
		}
		
		return result;
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
	private void setCountDownReminder() {
		if(mCursor != null && mCursor.moveToFirst()) {
			String remindSetting = remindSettingTextView.getText().toString();
			if(remindSetting == null || "".equals(remindSetting)) {
				return;
			}
			Integer _ID = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(CountDown._ID)));
			String title = titleTextView.getText().toString();
			String endDate = endDateTextView.getText().toString();
			String remindDate = getRemindDate(remindSetting, endDate);
			if(remindDate == null || "".equals(remindDate)) {
				return;
			}

			//set alarm
			PendingIntent sender = PendingIntent.getBroadcast(this,
					_ID, setIntentForAlarm(this, _ID, title, endDate),
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//firstly, cancel the old one
			am.cancel(sender);
			//secondly, create a new one
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = sdf.parse(remindDate);
				long triggerTime = sdf.parse(remindDate).getTime();
				am.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
				//am.setExact(AlarmManager.RTC_WAKEUP, triggerTime, sender);

				//save alarm data for recreate alarms after rebooted
				saveAlarmData(_ID, triggerTime, title, endDate);
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Intent setIntentForAlarm(Context context , int _ID, String title, String endDate) {
		Intent intent = new Intent(Constant.SEND_NOTIFICATION);
		intent.setClass(context, CommonReceiver.class);

		//transfer data to receiver
		Bundle extras = new Bundle();
		extras.putInt(CountDown._ID, _ID);
		extras.putString(CountDown.TITLE, title);
		extras.putString(CountDown.END_DATE, endDate);

		intent.putExtras(extras);
		return intent;
	}
	
	/**
	 * save alarm data for system boot-up
	 * @param _ID
	 * @param triggerTime
	 * @param title
	 * @param endDate
	 */
	private void saveAlarmData(int _ID , long triggerTime,
			String title , String endDate) {
		SharedPreferences.Editor prefs = this.getSharedPreferences(Constant.ALARM_DATA_FILE, Context.MODE_PRIVATE).edit();

		StringBuilder sb = new StringBuilder();
		sb.append(_ID).append(Constant.SPLIT_SEMICOLON).
				append(triggerTime).append(Constant.SPLIT_SEMICOLON).
				append(title).append(Constant.SPLIT_SEMICOLON).append(endDate);
		
		prefs.putString(Utils.appendAppWidgetId(CountDown._ID, _ID), sb.toString());
		prefs.commit();
	}

	 public static void deleteAlarmDataAndCancelAlarm(Context context, int id) {
		//delete alarm data
		SharedPreferences prefs = context.getSharedPreferences(Constant.ALARM_DATA_FILE,
				Context.MODE_PRIVATE);
		String title = prefs.getString(CountDown.TITLE, "");
		String endDate = prefs.getString(CountDown.END_DATE, "");
		//remove
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(Utils.appendAppWidgetId(CountDown._ID, id));
		editor.commit();

		//cancel alarm
		PendingIntent sender = PendingIntent.getBroadcast(context,
				id, setIntentForAlarm(context, id, title, endDate),
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		am.cancel(sender);



	}
	
	/**
	 * sync the data in the prefs file with the updated data (for widgets)
	 */
	private void updateWidgets() {
		if(mCursor != null) {
			//save update data
			String widgetIds = mCursor.getString(mCursor.getColumnIndex(CountDown.WIDGET_IDS));
			if(widgetIds == null || "".equals(widgetIds)) {
				return;
			} 
			
			String[] appWidgetIds = widgetIds.split(",");
			int[] intWidgetIds = new int[appWidgetIds.length];
			
			int _ID = mCursor.getInt(mCursor.getColumnIndex(CountDown._ID));
			int index = 0;
			for(String mAppWidgetId : appWidgetIds) {
				try{
					intWidgetIds[index] = Integer.parseInt(mAppWidgetId);
					index++;
				} catch(Exception e) {
					return;
				}
				String title = titleTextView.getText().toString();
				String priority = priorityTextView.getText().toString();
				String endDate = endDateTextView.getText().toString();
				//String endTime = StringUtil.underLineFilter(endTimeTextView.getText().toString());
				//save data for widget update
				WidgetConfigure.saveToPreference(this.getApplicationContext(), Integer.parseInt(mAppWidgetId), _ID, title, null, endDate, priority);
			}
			
			//create broadcast to update widget
			Intent intent = new Intent(Constant.UPDATE_WIDGET);
			Bundle extras = new Bundle();
			extras.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_ID, intWidgetIds);
			intent.putExtras(extras);
			this.sendBroadcast(intent);
		}
	}
	
	private void shareAction () {
		// ?????????Activity???????????????
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
       // ???��???????
        StringBuilder shareContent = new StringBuilder();
        int topLeftDays = CountDownAppWidgetProvider.getDayDiff(endDateTextView.getText() + "");
		if(topLeftDays < 0) {
			topLeftDays*=-1;
			shareContent.append(titleTextView.getText()).append(" ");
			shareContent.append(getResources().getString(R.string.days_status_passed_detail));
		} else {
			shareContent.append(getResources().getString(R.string.leftDayLabel)).append(" ");
			shareContent.append(titleTextView.getText()).append(" ");
			shareContent.append(getResources().getString(R.string.days_status_left));
		}
		shareContent.append(topLeftDays).append(getResources().getString(R.string.days));
		
        //mController.setShareContent(shareContent.toString());
       // ???��?????, ????2?????url???
		UMImage umimage = new UMImage(this, R.drawable.ic_launcher);
		String shareTitle = getResources().getString(R.string.share_from) +
				getResources().getString(R.string.app_label);
		String targetUrl = "http://app.mi.com/detail/71268";
        //mController.setShareMedia(umimage);
       
	   //############????QQ??? ???#################
	   //????1????Activity?? ????2?????????QQ?????????APP ID??????3?????????QQ?????????APP kEY.
       QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1102297892","OatpCSNxVdJpCinS");
       QZoneShareContent qzone = new QZoneShareContent();
       //???��???????
       qzone.setShareContent(shareTitle);
       //??????????????URL
       qzone.setTargetUrl(targetUrl);
       //???��???????????
       qzone.setTitle(shareContent.toString());
       //???��?????
       qzone.setShareImage(umimage);
       mController.setShareMedia(qzone);
       qZoneSsoHandler.addToSocialSDK();
       //############????QQ??? ????#################
       
      //############???????????? ???#################
       String appID = "wx4486dace47a3bd5b";
       String appSecret = "5a4ab424eb39b4b20dbc950c455a88b2";
    // ????????
       UMWXHandler wxHandler = new UMWXHandler(this ,appID,appSecret);
       wxHandler.addToSocialSDK();
       
       //?????????????????
       WeiXinShareContent weixinContent = new WeiXinShareContent();
       //???��???????
       weixinContent.setShareContent(shareTitle);
       //????title
       weixinContent.setTitle(shareContent.toString());
       //???��??????????URL
       weixinContent.setTargetUrl(targetUrl);
       //???��?????
       weixinContent.setShareImage(umimage);
       mController.setShareMedia(weixinContent);
       
       //???��???????
       // ???????????
       UMWXHandler wxCircleHandler = new UMWXHandler(this ,appID,appSecret);
       wxCircleHandler.setToCircle(true);
       wxCircleHandler.addToSocialSDK();

       CircleShareContent circleMedia = new CircleShareContent();
       circleMedia.setShareContent(shareTitle);
       //?????????title
       circleMedia.setTitle(shareContent.toString());
       circleMedia.setShareImage(umimage);
       circleMedia.setTargetUrl(targetUrl);
       mController.setShareMedia(circleMedia);
     //############???????????? ????#################
       
     //############????qq ???#################
     //????1????Activity?? ????2?????????QQ?????????APP ID??????3?????????QQ?????????APP kEY.
       UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1102297892","OatpCSNxVdJpCinS");
       QQShareContent qqShareContent = new QQShareContent();
       //???��???????
       qqShareContent.setShareContent(shareTitle);
       //???��???title
       qqShareContent.setTitle(shareContent.toString());
       //???��?????
       qqShareContent.setShareImage(umimage);
       //??????????????????????
       qqShareContent.setTargetUrl(targetUrl);
       mController.setShareMedia(qqShareContent);
       qqSsoHandler.addToSocialSDK();  
      //############????qq ????#################
       
        mController.getConfig().removePlatform( SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		//mController.openShare(this, false);
		mController.openShare(this, new SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                    SocializeEntity entity) {
            	if (eCode == 200) {
                    Toast.makeText(CountDownEdit.this, R.string.share_succ, Toast.LENGTH_SHORT)
                        .show();
                  } else {
                    Toast.makeText(CountDownEdit.this,
                        getResources().getString(R.string.share_fail) + " : error code : " + eCode, Toast.LENGTH_SHORT)
                        .show();
                  }
            }
        });
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
//		case R.id.endtime:
//			timePicker(R.id.endtime);
//			break;
//		case R.id.remindFromDate:
//			datePicker(R.id.remindFromDate);
//			break;
		case R.id.remind_setting_row:
			remindSettingDialog();
			break;
		case R.id.add_cancel:
			cancel();
			break;
		case R.id.add_ok:
			save();
			break;
		case R.id.delete_action:
			showDeleteConfirmDialog(getResources().getString(R.string.delete_task_confirm));
			break;
		case R.id.share_action:
			shareAction();
			break;
		default:
			break;
		}
	}
}
