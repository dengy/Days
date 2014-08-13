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
	
}
