package android.first.countdown;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolder{
	public TextView priorityView;
	public TextView titleView;
	public View remindBellImage;
//	public SlipButton finishSlipButton;
	public CheckBox finishCheckBox;
	public TextView enddateView;
	public TextView daysView;
	public TextView hoursView;
	public TextView minutesView;
	public TextView secondsView;
	public TextView expireView;
	public View countdownLayout;
	public Integer _ID;
	
	public SharedPreferences.Editor prefs;
	
}