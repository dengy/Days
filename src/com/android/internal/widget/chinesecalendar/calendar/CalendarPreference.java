package com.android.internal.widget.chinesecalendar.calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class CalendarPreference
{
    private SharedPreferences calendarSharedPreference = null;

    private static CalendarPreference calendarPreference;

    private static final String calendar_PREFERENCE = "calendarpre";
    
    private static String requesetCode ="requesetCode";

    public static synchronized CalendarPreference getInstance(Context context)
    {
        calendarPreference = new CalendarPreference(context);
        return calendarPreference;
    }

    private CalendarPreference(Context context)
    {
        calendarSharedPreference = context.getSharedPreferences(calendar_PREFERENCE, Context.MODE_WORLD_READABLE);
    }

    public  int  getRequestCode()
    {
        int code = calendarSharedPreference.getInt(requesetCode, 0);
        if(code >60000)
        {
            code = 0;
        }
        setRequestCode(code);
        return code;
    }
    public void setRequestCode(int requesetCode1)
    {
        calendarSharedPreference.edit().putInt(requesetCode, requesetCode1+1).commit();
    }

}
