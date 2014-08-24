package android.first.countdown.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.first.countdown.Constant;
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

}
