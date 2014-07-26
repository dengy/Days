package android.first.countdown;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AlarmExecutor extends Activity implements OnClickListener{
	private MediaPlayer mediaPlayer;
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown_reminder);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			Integer _ID = bundle.getInt(CountDown._ID);
			String title = bundle.getString(CountDown.TITLE);
			String reminder = bundle.getString(CountDown.REMIND_BELL);
			String endDate = bundle.getString(CountDown.END_DATE);
			
			((TextView)findViewById(R.id.reminderTitle)).setText(title);
			findViewById(R.id.closeReminder).setOnClickListener(this);
			
			if(reminder != null && !"".equals(reminder)) {
				startReminder(reminder, this);
			}
			
			if(endDate != null && !"".equals(endDate)) {
				stopAlarm(endDate, _ID);
			}
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.closeReminder:
			stopMusic();
			break;
		}
	}
	
	private void startReminder(String reminder, Context context) {
		String[] arr = reminder.split("\\+");
		String bell1 = getResources().getString(R.string.bell1);
		String bell2 = getResources().getString(R.string.bell2);
		String bell3 = getResources().getString(R.string.bell3);
		for(String s : arr) {
			if(bell1.equals(s)) {
				playMusic(R.raw.marimba, context);
			} else if(bell2.equals(s)) {
				playMusic(R.raw.happy, context);
			} else if(bell3.equals(s)) {
				playMusic(R.raw.goodnewday, context);
			} 
			
			if(getResources().getString(R.string.vibrator).equals(s)) {
				startVibrator();
			}
		}
	}
	
	private void playMusic(int musicName, Context context) {
		mediaPlayer = MediaPlayer.create(context, musicName);
		//mediaPlayer.setLooping(true);
		mediaPlayer.start(); // no need to call prepare(); create() does that for you
	}
	
	private void stopMusic() {
		
		if(mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
		if(vibrator != null) {
			vibrator.cancel();
			vibrator = null;
		}
		
		//shutdown this activity
		finish();
	}
	
	private void startVibrator() {
		vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		long [] pattern = {100,400,100,400};
		vibrator.vibrate(pattern, 2);
	}
	
	private void stopAlarm(String endDate , int _ID) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sf.format(now.getTime());
		
		if(endDate.equals(currentDate)) {
			Intent intent = new Intent(this, AlarmReceiver.class);
			PendingIntent operation = PendingIntent.getBroadcast(this,
					_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			//cancel alarm
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			am.cancel(operation);
		}
	}

}
