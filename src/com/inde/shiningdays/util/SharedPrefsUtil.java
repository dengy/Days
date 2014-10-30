package com.inde.shiningdays.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtil {
	public static SharedPreferences getSharedPrefs(Context context, String fileName) {
		SharedPreferences prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefs;
	}
	
}
