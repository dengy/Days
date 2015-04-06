package com.inde.shiningdays.util;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.inde.shiningdays.Constant;
import com.inde.shiningdays.R;

public class Utils {
	public static String appendAppWidgetId(String name, String appWidgetId) {
		StringBuilder sb = new StringBuilder();
		sb.append(appWidgetId).append("_").append(name);
		return sb.toString();
	}
	
	public static String appendAppWidgetId(String name, int appWidgetId) {
		StringBuilder sb = new StringBuilder();
		sb.append(appWidgetId).append("_").append(name);
		return sb.toString();
	}
	
//	public static String underLineFilter(String src) {
//		if(CountDown.DEFAULT_FOR_DATE.equals(src)) {
//			return "";
//		}
//		
//		return src;
//	}
	
	public static String[] getDefaultTypes(Context context) {
		return context.getResources().getStringArray(R.array.priority);
	}
	
	public static String[] getCustomTypes(Context context) { 
		
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(context, Constant.CUSTOM_TYPE_LIST_PREF);
		Map<String,?> map = prefs.getAll();
//		Set<String> set = map.keySet();
//		Iterator<String> it = set.iterator();
		String[] results = new String[map.size()];
//		while(it.hasNext()) {
//			String key = it.next();
//			results[index] = map.get(key) + "";
//		}
		
		int index = 0;
		for(String s: map.keySet()) {
			results[index++] = map.get(s) + "";
		}
		
		return results;
	}
	
	/**
	 * get all types (default types + custom types)
	 * @param context
	 * @return
	 */
	public static String[] getAllTypes(Context context) {
		String[] defaultTypes = getDefaultTypes(context);
		String[] customTypes = getCustomTypes(context);
		//String[] resultTypes = new String[defaultTypes.length + customTypes.length];
		String[] resultTypes = Arrays.copyOf(defaultTypes, defaultTypes.length + customTypes.length);
		int i = 0;
		for(String type : customTypes) {
			resultTypes[i + defaultTypes.length] = type;
			i++;
		}
		return resultTypes;
	}

	public static void cancelAlarmManager() {

	}

    /*public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }*/

    public static boolean isNeedLockPwd (Context context) {
        SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(context, Constant.COUNT_DOWN_SETTING_PREF);
        String lockPwd = prefs.getString(Constant.LOCK_PWD,"");
        if("".equals(lockPwd)) {
            return false;
        }
        return true;
    }

    public static void sendEmail(Context context, String email, String title, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        //mail address
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {email});
        //mail title
        intent.putExtra(Intent.EXTRA_SUBJECT, title);

        //mail content
        intent.putExtra(Intent.EXTRA_TEXT, content);

        try {
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.select_email_client)));
        }catch(android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, R.string.no_email_app_installed, Toast.LENGTH_SHORT).show();
        }
    }

}
