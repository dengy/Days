package com.android.internal.widget.chinesecalendar.calendar;

import java.util.Date;

public class LunarDate {

	public int year;
	public int month;
	public int date;
	boolean isLeap;
	public boolean exceptionFlag;

	public LunarDate(int year, int month, int date, boolean leap) {
		super();
		this.year = year;
		this.month = month;
		this.date = date;
		this.isLeap = leap;
	}

	public LunarDate(Date objDate) {

		int i, leap = 0, temp = 0;
		int offset = (int) ((Date.UTC(objDate.getYear() - 1900,
				objDate.getMonth(), objDate.getDate(), 0, 0, 0) - Date.UTC(0,
				0, 31, 0, 0, 0)) / 86400000);
		for (i = 1900; i < 2100 && offset > 0; i++) {
			temp = CalendarUtils.lYearDays(i);
			offset -= temp;
		}
		if (offset < 0) {
			offset += temp;
			i--;
		}
		year = i;
		try{
			leap = CalendarUtils.leapMonth(i);
		
			isLeap = false;
			for (i = 1; i < 13 && offset > 0; i++) {
				if (leap > 0 && i == (leap + 1) && isLeap == false) {
					--i;
					isLeap = true;
					temp = CalendarUtils.leapDays(year);
				} else {
					temp = CalendarUtils.monthDays(year, i);
				}
				if (isLeap == true && i == (leap + 1))
					isLeap = false;
				offset -= temp;
			}
		}catch(Exception e){
			exceptionFlag = true;
			return;
		}
		if (offset == 0 && leap > 0 && i == leap + 1)
			if (isLeap) {
				isLeap = false;
			} else {
				isLeap = true;
				--i;
			}
		if (offset < 0) {
			offset += temp;
			--i;
		}
		month = i;
		date = offset + 1;
	}
	
	public boolean isNotSupportLunarDate(){
		return this.exceptionFlag;
	}

}
