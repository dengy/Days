package android.first.countdown;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.first.countdown.ViewHolder;
import android.first.countdown.util.StringUtil;

class MyCountDownTimer extends CountDownTimer {
	public static String TAG = "MyCountDownTimer";
	ViewHolder viewHolder;
	long difference;

	public MyCountDownTimer(long millisInFuture, long countDownInterval, ViewHolder viewHolder) {
		super(millisInFuture, countDownInterval);
		this.viewHolder = viewHolder;
		this.difference = millisInFuture;
		
	}

	@Override
	public void onFinish() {
		Log.i(TAG, "onFinish");
		//hinden countdownLayout
		viewHolder.countdownLayout.setVisibility(View.GONE);
    	
    	/*long expireTimes = difference * (-1);
    	int expireDays = (int) ((expireTimes / 1000) / 86400);
    	int expireHours = (int) (((expireTimes / 1000) - (expireDays
                * 86400)) / 3600);
        int expireMinutes = (int) (((expireTimes / 1000) - ((expireDays
                * 86400) + (expireHours * 3600))) / 60);
                * 
        StringBuilder sb = new StringBuilder();
        if(expireDays != 0 && expireHours != 0 && expireMinutes != 0) {
        	sb.append("已过期 ").append(expireDays).append("天 ").append(expireHours).append("时 ")
        	.append(expireMinutes).append("分");
        } else if(expireDays == 0 && expireHours != 0 && expireMinutes != 0) {
        	sb.append("已过期 ").append(expireHours).append("时 ")
        	.append(expireMinutes).append("分");
        } else if(expireDays == 0 && expireHours == 0 && expireMinutes != 0) {
        	sb.append("已过期 ").append(expireMinutes).append("分");
        }
        viewHolder.expireView.setText(sb.toString());
        */
        
    	viewHolder.expireView.setText(Constant.EXPIRED);
    	//show expire textview
    	viewHolder.expireView.setVisibility(View.VISIBLE);
    	//delete alarm data
    	Log.i(TAG, "delete alarm data:" + viewHolder._ID+"");
    	viewHolder.prefs.remove(StringUtil.appendAppWidgetId(CountDown._ID, viewHolder._ID));
    	
    	viewHolder.prefs.commit();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		//show countdownLayout
		viewHolder.countdownLayout.setVisibility(View.VISIBLE);
		//hidden expire textview
		viewHolder.expireView.setVisibility(View.GONE);
		
		int days = (int) ((millisUntilFinished / 1000) / 86400);
        int hours = (int) (((millisUntilFinished / 1000) - (days
                * 86400)) / 3600);
        int minutes = (int) (((millisUntilFinished / 1000) - ((days
                * 86400) + (hours * 3600))) / 60);
//        int seconds = (int) ((millisUntilFinished / 1000) % 60);
    	viewHolder.daysView.setText(days + "");
//    	viewHolder.hoursView.setText(hours + "");
//    	viewHolder.minutesView.setText(minutes + "");
//    	viewHolder.secondsView.setText(seconds + "");
	}
}