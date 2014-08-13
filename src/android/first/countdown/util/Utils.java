package android.first.countdown.util;

import java.util.Arrays;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.first.countdown.Constant;
import android.first.countdown.CountDown;
import android.first.countdown.R;

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
		String[] results = new String[map.size()];
		for(int i = 0; i < map.size(); i++) {
			results[i] = map.get((i+1)+"") + "";
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

}
