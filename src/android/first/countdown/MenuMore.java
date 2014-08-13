package android.first.countdown;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class MenuMore extends Activity implements OnClickListener{
	private View rate;
	private View functions;
	private View feedback;
	private View about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		findViewById(R.id.more_back).setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//umeng sdk
    	MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//umeng sdk
    	MobclickAgent.onPause(this);
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
        PackageManager packageManager = getPackageManager();
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
			Toast.makeText(this, R.string.no_email_app_installed, Toast.LENGTH_SHORT).show();
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
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
}
