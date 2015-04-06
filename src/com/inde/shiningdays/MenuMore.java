package com.inde.shiningdays;

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
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.interstitial.AdsMogoInterstitialManager;
import com.adsmogo.util.AdsMogoTargeting;
import com.inde.shiningdays.util.BaseActivity;
import com.inde.shiningdays.util.Utils;
import com.umeng.analytics.MobclickAgent;




public class MenuMore extends BaseActivity implements OnClickListener{
    private View openLock;
	private View rate;
	private View functions;
	private View feedback;
	private View about;
    private boolean isNeedLockPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_more);
		initViews();

        /** 代码方式添加广告，如果您使用XML配置方式添加广告，不需要以下代码 **/
        AdsMogoLayout adsMogoLayoutCode = new AdsMogoLayout(this, Constant.MONGO_ID);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告出现的位置(悬浮于底部)
        params.bottomMargin = 20;
        //adsMogoLayoutCode.setAdsMogoListener(this);
        params.gravity = Gravity.BOTTOM;
        addContentView(adsMogoLayoutCode, params);
        /*********************** 代码添加广告结束 ************************/
	}

	private void initViews() {
		rate = findViewById(R.id.rate);
		functions = findViewById(R.id.functions);
		feedback = findViewById(R.id.feedback);
		about = findViewById(R.id.about);
        openLock = findViewById(R.id.open_lock);

		rate.setOnClickListener(this);
		functions.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
        openLock.setOnClickListener(this);
		findViewById(R.id.more_back).setOnClickListener(this);
	}

    private void initLockPwdViews() {
        View modifyLock = findViewById(R.id.modify_lock);
        View closeLock = findViewById(R.id.close_lock);
        if(Utils.isNeedLockPwd(this)) {
            isNeedLockPwd = true;
            openLock.setVisibility(View.GONE);
            if(modifyLock != null) {
                modifyLock.setVisibility(View.VISIBLE);
                modifyLock.setOnClickListener(this);
            }
            if(closeLock != null) {
                closeLock.setVisibility(View.VISIBLE);
                closeLock.setOnClickListener(this);
            }
        } else {
            openLock.setVisibility(View.VISIBLE);
            if(modifyLock != null) {
                modifyLock.setVisibility(View.GONE);
            }
            if(closeLock != null) {
                closeLock.setVisibility(View.GONE);
            }
        }
    }


	
	@Override
	protected void onResume() {
		super.onResume();
        initLockPwdViews();
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
	    ((TextView)layout.findViewById(R.id.version)).setText(getResources().getString(R.string.version)+ getVersionName());
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
        Utils.sendEmail(this, getResources().getString(R.string.mail),
                getResources().getString(R.string.email_title),
                content);
	}
	
	@Override
	public void onClick(View v) {
        Intent intent;
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
        case R.id.open_lock:
            intent = new Intent(this, PasswordEdit.class);
			startActivity(intent);
            break;
        case R.id.modify_lock:
            intent = new Intent(this, LockScreen.class);
            intent.putExtra(Constant.LOCK_OPERATION, Constant.MODIFY_LOCK);
            startActivity(intent);
            break;
        case R.id.close_lock:
            intent = new Intent(this, LockScreen.class);
            intent.putExtra(Constant.LOCK_OPERATION, Constant.CLOSE_LOCK);
            startActivity(intent);
            break;
		case R.id.more_back:
		    finish();
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
}
