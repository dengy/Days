package android.first.countdown.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.first.countdown.Constant;
import android.first.countdown.R;

public class SharedPrefsUtil {
	public static SharedPreferences getSharedPrefs(Context context, String fileName) {
		SharedPreferences prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefs;
	}
	
	public static String[] getDefaultTypes(Context context) {
		return context.getResources().getStringArray(R.array.priority);
	}
	
	public static String[] getCustomTypes(Context context) { 
		SharedPreferences prefs = getSharedPrefs(context, Constant.CUSTOM_TYPE_LIST_PREF);
		Map<String,?> map = prefs.getAll();
		String[] results = new String[map.size()];
		for(int i = 0; i < map.size(); i++) {
			results[i] = map.get((i+1)+"") + "";
		}
		/*Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		int i = 0;
		while(it.hasNext()) {
			String key = it.next();
			results[i++] = map.get(key).toString();
		}*/
		return results;
	}
	
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
