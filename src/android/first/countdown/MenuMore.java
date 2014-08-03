package android.first.countdown;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.first.countdown.util.StringUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class MenuMore extends Activity implements OnClickListener{
	private static final String TAG = "ItemList";
	private View rate;
	private View functions;
	private View feedback;
	private View about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_more);
		initViews();
		
		
        
	}
	
	private void initViews() {
		rate = findViewById(R.id.rate);
		functions = findViewById(R.id.functions);
		feedback = findViewById(R.id.feedback);
		about = findViewById(R.id.about);
		rate.setOnClickListener(this);
		functions.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
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
	}
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
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
	
	
	private void showAboutDialog() {
		//AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlarmDialogTheme));
	    // Get the layout inflater
	    //LayoutInflater inflater = this.getLayoutInflater();
	    
	    //View layout = inflater.inflate(R.layout.about_dialog, null);
	    View layout = View.inflate(new ContextThemeWrapper(this, R.style.AlarmDialogTheme), 
	    		R.layout.about_dialog, null);
	    ((TextView)layout.findViewById(R.id.version)).setText("Version:" + getVersionName());
	    builder.setView(layout);
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}
	
	private void showFunctionDialog() {
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlarmDialogTheme));
	    // Get the layout inflater
	    //LayoutInflater inflater = this.getLayoutInflater();
	    
	    //View layout = inflater.inflate(R.layout.help_dialog, null);
	    View layout = View.inflate(new ContextThemeWrapper(this, R.style.AlarmDialogTheme), 
	    		R.layout.help_dialog, null);
	    builder.setView(layout);
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}
	
	private String getVersionName() {
		 // ����packagemanager������
        PackageManager packageManager = getPackageManager();
        // getPackageName()������������������0������������������
        PackageInfo packInfo;
        String version = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        
        return version;
	     
	}
	
	private void showFeedbackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View layout = inflater.inflate(R.layout.feedback_dialog, null);
	    final EditText feedback = (EditText)layout.findViewById(R.id.feedback_content);
	    builder.setView(layout);
	    builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content = feedback.getText().toString();
				if(content != null && !"".equals(content.trim())) {
					sendMail(content);
				}
			}
		}).setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(true);//cancel window when touch outside
		d.show();
	}
	
	private void sendMail(String content) {
		Intent intent = new Intent(Intent.ACTION_SEND); 
		intent.setType("message/rfc822");
		
		//mail address
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]    
				{getResources().getString(R.string.mail)});  
//		intent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.mail));  

		//mail title
		intent.putExtra(Intent.EXTRA_SUBJECT, Constant.MAIL_TITLE);  
		
		//mail content
		intent.putExtra(Intent.EXTRA_TEXT, content);  
		
		try {
			startActivity(Intent.createChooser(intent, Constant.MAIL_SENDING));    
		}catch(android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.about:
			showAboutDialog();
			break;
		case R.id.functions:
			showFunctionDialog();
			break;
		case R.id.rate:
			Intent goToMarket = new Intent(Intent.ACTION_VIEW);
			goToMarket.setData(Uri.parse("market://details?id=" + getPackageName()));
			startActivity(goToMarket); 
			break;
		case R.id.feedback:
			showFeedbackDialog();
			break;
		case R.id.more_back:
			finish();
			break;
		default:
			break;
		}
		
	}
}
