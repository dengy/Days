package com.android.internal.widget.chinesecalendar.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.android.internal.widget.chinesecalendar.calendar.CalendarUtils;


public class Easter {
	int m;
	int d;

	public Easter(int y) {
		int term2 = CalendarUtils.sTerm(y, 5);
		Date dayTerm2 = new Date(Date.UTC(y, 2, term2, 0, 0, 0));
		LunarDate lDayTerm2 = new LunarDate(dayTerm2);

		int lMlen = 0;
		if (lDayTerm2.date < 15)
			lMlen = 15 - lDayTerm2.date;
		else
			lMlen = (lDayTerm2.isLeap ? CalendarUtils.leapDays(y)
					: CalendarUtils.monthDays(y, lDayTerm2.month))
					- lDayTerm2.date + 15;
		Date l15 = new Date(dayTerm2.getTime() + 86400000 * lMlen);
		Calendar c = Calendar.getInstance();
		c.setTime(l15);
		DateFormat gmtFormat = new SimpleDateFormat("yyyy-mm-dd");
		TimeZone gmtTime = TimeZone.getTimeZone("UTC");

		gmtFormat.setTimeZone(gmtTime);
		String s = gmtFormat.format(c.getTime());
		String[] arys = s.split("-");
		Date dayEaster = new Date(l15.getTime() + 86400000
				* (7 - Integer.parseInt(arys[2])));
		s = gmtFormat.format(dayEaster);
		arys = s.split("-");
		this.m = Integer.parseInt(arys[1]);
		this.d = Integer.parseInt(arys[2]);
	}

}
