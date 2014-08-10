package android.first.countdown.util;

import android.first.countdown.CountDown;

public class StringUtil {
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

}
