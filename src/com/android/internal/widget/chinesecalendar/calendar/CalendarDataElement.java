package com.android.internal.widget.chinesecalendar.calendar;

import java.io.Serializable;

import android.content.res.Resources;
import com.inde.shiningdays.R;

public class CalendarDataElement implements Serializable {
	public boolean isToday;
	public int sYear;	// 公历年
	public int sMonth; // 公历月
	public int sDay;	// 公历日
	public String week;
	public int lYear;	// 农历年, 如牛
	public int lMonth; // 农历月, 如冬月
	public int lDay;   // 农历日, 如初一，如十五
	public boolean isLeap;
	public String cYear; // 干支年, 如庚寅年
	public String cMonth;// 干支月, 如戊子月
	public String cDay;  // 干支日, 如甲辰日
	public String solarTerms;//24节气
	public String solarFestival;// 公历节日
	public String lunarFestival;// 农历节日
	/**
	 * 彭祖忌日  返回彭祖百忌 甲不开仓 子不问卜
	 */
	public String pgday;	// "彭祖百忌"的信息
	/**
	 * 五行
	 */
	public String sgz2;		// "五行"的信息
	/**
	 * 犯冲
	 */
	public String sgz4;		// "冲"的信息
	public String sgz5;		// 日值岁破, 大事不宜; 日值月破, 大事不宜; 日值上朔, 大事不宜; 日值杨公十三忌, 大事不宜
	/**
	 * 黑黄道十二吉凶日，即建、除、满、平、定、执、破、 危、成、收、开、闭
	 */
	public String sgz3;// 
	public String yj;// 宜忌信息，以"|"分割，"|"之前为宜，"|"之后为忌
	/**
	 * "值日"的信息
	 */
	public String sgz6;  // "值日"的信息，即十二神+黄道黑道日，如青龙(黄道日)，天刑(黑道日)

	public String lunarDate;	// 农历日期，如初七
	public String lunarMonth;	// 农历月份，如腊月(12)，如冬月(11)

	public String animal;		// 十二生肖名称，如牛
	
	public String customFestival="";
	public int weekday;
	/**
	 * 星座信息，如1月20日~2月18日-0-水瓶座：
	 * 1月20日~2月18日是星座的时间区域
	 * 0是该星座详情的索引号
	 * 水瓶座是星座名
	 */
	public String mConstellation = "";	
	
	public boolean mIsSelected = false;
	public boolean mIsCurMonth = true;
	public boolean mIsUpDate = false;
	
	public  CalendarDataElement(CalendarDataElement src){
		this.isToday = src.isToday;
		this.weekday = src.weekday;
		this.sYear = src.sYear;
		this.sMonth = src.sMonth;
		this.sDay = src.sDay;
		this.week = src.week;

		this.lYear = src.lYear;
		this.lMonth =src. lMonth;
		this.lDay =src. lDay;
		this.isLeap =src. isLeap;

		this.cYear =src. cYear;
		this.cMonth =src. cMonth;
		this.cDay =src. cDay;

		this.solarTerms =src.solarTerms;
		this.solarFestival = src.solarFestival;
		this.lunarFestival = src.lunarFestival;

		this.lunarDate = src.lunarDate;
		this.lunarMonth = src.lunarMonth;
		this.animal = src.animal;
		
		pgday = src.pgday;
		// public String dGz;
		sgz2 = src.sgz2;
		sgz4 = src.sgz4;
		sgz5 = src.sgz5;
		this.weekday = src.weekday;
		this.customFestival = src.customFestival;
		sgz3 = src.sgz3;
		
		yj = src.yj;
		sgz6 = src.sgz6;
		mConstellation = src.mConstellation;
	}

	public CalendarDataElement(int sYear, int sMonth, int sDay, String week,
			int lYear, int lMonth, int lDay, boolean isLeap, String cYear,
			String cMonth, String cDay,int weekday) {
		this.isToday = false;
		this.weekday = weekday;
		this.sYear = sYear;
		this.sMonth = sMonth;
		this.sDay = sDay;
		this.week = week;

		this.lYear = lYear;
		this.lMonth = lMonth;
		this.lDay = lDay;
		this.isLeap = isLeap;

		this.cYear = cYear;
		this.cMonth = cMonth;
		this.cDay = cDay;

		this.solarTerms = "";
		this.solarFestival = "";
		this.lunarFestival = "";

		this.lunarDate = CalendarUtils.getLunarDay(lDay);
		this.lunarMonth = CalendarUtils.getLunarMonth(CalendarData.context.getResources(),lMonth);
		this.animal = CalendarUtils.getAnimal(this.cYear);
		this.mConstellation = CalendarUtils.getConstellation(sMonth, sDay);
	}

	public void printString() {
		System.out.println(isToday + " " + sYear + " " + sMonth + " " + sDay
				+ " " + week + " " + lYear + " " + lMonth + " " + lDay + " "
				+ isLeap + " " + cYear + " " + cMonth + " " + cDay + " "
				+ " " + solarTerms + " " + solarFestival + " "
				+ lunarFestival + " " + pgday + " " + " " + sgz2 + " " + sgz4
				+ " " + sgz5 + " " + sgz6 + " " + " " + " " + " " + sgz3 + " "
				+ yj + " " + " " + " " + " ");
	}

	/**
	 * 显示节假日信息，包括农历节日信息+阳历假日信息+阳历节日信息
	 * @return 节假日信息的字符串
	 */
	public String getDisplayString() {
		Resources r = CalendarData.context.getResources();
		if(!isEmptyString(this.customFestival)){
			if(customFestival.length() > 3){
				return customFestival.substring(0,3)+"..";
			}else{
				return customFestival;
			}
		}
		String fs1 = lunarFestival + this.solarTerms +this.solarFestival ;
		String fs2 = "";
		String fs="";
		if (this.lDay == 1) {
			String leap = this.isLeap ? r.getString(R.string.zzzzz_leap) : "";
			String big = "";
			if (leap.equals("")) {
				big = CalendarUtils.monthDays(lYear, lMonth) == 29 ? r.getString(R.string.zzzzz_small) : r.getString(R.string.zzzzz_big);
			}
			fs2 = leap + lunarMonth + r.getString(R.string.zzzzz_monthStr) + big;
		}
		if(!isEmptyString(fs1)){
			fs = fs1;
		}else if(!isEmptyString(fs2)){
			fs = fs2;
		}
		
		// System.out.println(fs);
		fs = fs.trim();
		if (isEmptyString(fs)) {
			return this.lunarDate;
		} else if (fs.length() > 3) {
			return fs.substring(0, 3) + "..";
		} else{
			return fs;
		}
	}

	/**
	 * 获取农历月，如腊月(大)
	 * @return
	 */
	public String getMonthStr() {
		// if(this.lDay==1){
		Resources r = CalendarData.context.getResources();
		String leap = this.isLeap ? r.getString(R.string.zzzzz_leap) : "";
		String big = "";
		if (leap.equals("")) {
			big = CalendarUtils.monthDays(lYear, lMonth) == 29 ? "("+r.getString(R.string.zzzzz_small)+")" : 
				"("+r.getString(R.string.zzzzz_big)+")";
		}
		return leap + lunarMonth + r.getString(R.string.zzzzz_monthStr) + big;
		// }else{
		// return lunarMonth+"��";
		// }
	}
	
	public String getMonthStr2() {
		// if(this.lDay==1){
		Resources r = CalendarData.context.getResources();
		String leap = this.isLeap ? r.getString(R.string.zzzzz_leap) : "";
		return leap + lunarMonth + r.getString(R.string.zzzzz_monthStr);
		// }else{
		// return lunarMonth+"��";
		// }
	}

	public static boolean isEmptyString(String s) {
		s = s.trim();
		return s == null || s.equals("");
	}

	public String toString() {
		return isToday + " " + sYear + " " + sMonth + " " + sDay + " " + week
				+ " " + lYear + " " + lMonth + " " + lDay + " " + isLeap + " "
				+ cYear + " " + cMonth + " " + cDay + " " + " "
				+ solarTerms + " " + solarFestival + " " + lunarFestival + " "
				+ pgday + " " + " " + sgz2 + " " + sgz4 + " " + sgz5 + " "
				+ sgz6 + " " + " " + " " + " " + sgz3 + " " + yj + " " + " "
				+ " " + " ";
	}
}
