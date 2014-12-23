package com.android.internal.widget.chinesecalendar.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import com.inde.shiningdays.R;

public class CalendarData {
	public static Context context = null;
	public static String[] nStr1 = null;

	public static int[] nStr1Id = new int[] { R.string.zzzzz_ri, R.string.zzzzz_one,
			R.string.zzzzz_two, R.string.zzzzz_three, R.string.zzzzz_four, R.string.zzzzz_five,
			R.string.zzzzz_six, R.string.zzzzz_seven, R.string.zzzzz_eight, R.string.zzzzz_nine,
			R.string.zzzzz_ten };
	public int length;
	public int firstWeek;
	public String lunarYear;
	public String lunarMonth;
	public ArrayList<CalendarDataElement> asList = new ArrayList<CalendarDataElement>();

	public int solarisYear;
	boolean wholeMonth = true;
	int dayCount = 0;
	public int solarisMonth;

	/**
	 * @param y 年
	 * @param m 月
	 */
	public CalendarData(int y, int m) 
	{
		this.length = CalendarUtils.solarDays(y, m);
		firstWeek = CalendarUtils.getDay(y, m, 1);

		fillElements(y, m);
	}

	/**
	 * @param y 年
	 * @param m 月
	 * @param wholeMonth
	 * @param dayCounts 天数
	 */
	public CalendarData(int y, int m, boolean wholeMonth, int dayCounts) {
		this.length = CalendarUtils.solarDays(y, m);
		firstWeek = CalendarUtils.getDay(y, m, 1);
		wholeMonth = false;
		dayCount = dayCounts;
		fillElements(y, m);
	}

	public void fillElements(int y, int m) {
		if (!asList.isEmpty()) {
			asList.clear();
		}

		int beginIndex = 0;
		int endIndex = this.length;

		if (!wholeMonth) {
			if (dayCount > 0) {
				endIndex = dayCount;
			} else if (dayCount < 0) {
				beginIndex = this.length + dayCount;
			} else {
				return;
			}
		}

		String cM = CalendarUtils.cyclical((y - 1900) * 12 + m + 12);
		int lM2 = (y - 1900) * 12 + m + 12;
		int dayCyclical = (int) (Date.UTC(y - 1900, m, 1, 0, 0, 0) / 86400000 + 25567 + 10);
		int lD = 1;
		int lX = 0;
		Date sDObj = null;
		LunarDate lDObj = null;
		int lY = 0, lM = 0;
		boolean lL = false;
		int n = 0;
		int firstLM = 0;
		int[] lDPOS = new int[length];
		int term2 = CalendarUtils.sTerm(y, 2);
		String cY = "";
		int lY2 = 0;
		if (m < 2) {
			cY = CalendarUtils.cyclical(y - 1900 + 36 - 1);
			lY2 = (y - 1900 + 36 - 1);
		} else {
			cY = CalendarUtils.cyclical(y - 1900 + 36);
			lY2 = (y - 1900 + 36);
		}

		int firstNode = CalendarUtils.sTerm(y, m * 2);
		String cD = "";
		int lD2 = 0;
		int bsg = 0;
		int cs1 = 0;
		String xs1 = "";
		String dayglus = "";
		String xs = "", cs = "", fs = "";

		for (int i = beginIndex; i < endIndex; i++) {
			if (lD > lX) {
				sDObj = new Date(y, m, i + 1);
				lDObj = new LunarDate(sDObj);
				lY = lDObj.year;
				lM = lDObj.month;
				lD = lDObj.date;
				lL = lDObj.isLeap;
				lX = lL ? CalendarUtils.leapDays(lY) : CalendarUtils.monthDays(
						lY, lM);
				if (n == 0)
					firstLM = lM;
				lDPOS[n++] = i - lD + 1;
			}

			if (m == 1
					&& ((i + 1) == term2 || (i + 1 > term2 && beginIndex != 0))) {
				cY = CalendarUtils.cyclical(y - 1900 + 36);
				lY2 = (y - 1900 + 36);
			}
			if ((i + 1) == firstNode || (i + 1 > firstNode && beginIndex != 0)) {
				cM = CalendarUtils.cyclical((y - 1900) * 12 + m + 13);
				lM2 = (y - 1900) * 12 + m + 13;
			}

			cD = CalendarUtils.cyclical(dayCyclical + i);
			lD2 = (dayCyclical + i);
			lunarYear = cY;

			lunarMonth = cM;
			solarisYear = y;
			solarisMonth = m;
			CalendarDataElement element = new CalendarDataElement(y, m + 1,
					i + 1, nStr1[(i + this.firstWeek) % 7], lY, lM, lD++, lL,
					cY, cM, cD, (i + this.firstWeek) % 7);
			;
			this.asList.add(element);
			bsg = (lD2) % 12;
			cs1 = i + 1;

			if (element.lMonth == 12 && element.lDay == lX && !element.isLeap) {// andy
																				// add
				element.lunarFestival += CalendarData.context.getResources()
						.getString(R.string.zzzzz_chuxi);
			}

			// 初始化彭祖百忌信息
			this.asList.get(i - beginIndex).pgday = CalendarUtils.cyclical3(lD2);
			int SY2 = 0;
			if (solarisMonth == 0) {
				SY2 = solarisYear - 1;
			} else {
				SY2 = solarisYear;
			}
			// 初始化五行信息
			this.asList.get(i - beginIndex).sgz2 = CalendarUtils.jzny(lD2 % 10
					+ "" + lD2 % 12);
			// 初始化犯冲信息
			this.asList.get(i - beginIndex).sgz4 = CalendarUtils.CalConv(
					(lD2) % 10, (lD2) % 12);
			// 初始化大事不宜信息
			this.asList.get(i - beginIndex).sgz5 = CalendarUtils.CalConv2(
					lY2 % 12, lM2 % 12, (lD2) % 12, lY2 % 10, (lD2) % 10, lM,
					lD - 1, m + 1, cs1);
			// 初始化黑黄道十二值神信息
			this.asList.get(i - beginIndex).sgz6 = CalendarUtils.cyclical7(
					lM2 % 12, (lD2) % 12);
			// 初始化黑黄道十二吉凶日信息
			this.asList.get(i - beginIndex).sgz3 = CalendarUtils.cyclical6(
					lM2 % 12, (lD2) % 12);
			// 初始化宜忌信息
			this.asList.get(i - beginIndex).yj = CalendarUtils.jcr(this.asList
					.get(i - beginIndex).sgz3);
		}
		// 获取系统当前年月日
		Calendar cc = Calendar.getInstance();
		int currentYear = cc.get(Calendar.YEAR);
		int currentMonth = cc.get(Calendar.MONTH);
		int currentDay = cc.get(Calendar.DATE);

		int tmp1 = CalendarUtils.sTerm(y, m * 2) - 1;
		int tmp2 = CalendarUtils.sTerm(y, m * 2 + 1) - 1;
		boolean isError = false;//flag   the result is error.
		if (tmp1 - beginIndex < asList.size() && tmp1 - beginIndex > -1) {
			isError = needToModify(y,m,tmp1-beginIndex,asList);
			if(!isError)
				asList.get(tmp1 - beginIndex).solarTerms = CalendarUtils.solarTerm[m * 2];
			
			if (tmp2 - beginIndex < asList.size() && tmp2 - beginIndex > -1) {
				isError = needToModify(y,m,tmp2-beginIndex,asList);
				if(!isError)
					asList.get(tmp2 - beginIndex).solarTerms = CalendarUtils.solarTerm[m * 2 + 1];
			
				Pattern pattern = Pattern
						.compile("^(\\d{2})(\\d{2})([\\s\\*])(.+)$");
				Matcher matcher = null;
				for (int ii = 0; ii < CalendarUtils.sFtv.length; ii++) {
					matcher = pattern.matcher(CalendarUtils.sFtv[ii]);
					if (matcher.matches()) {
						String s = matcher.group();
						String s1 = matcher.group(1);
						String s2 = matcher.group(2);
						String s3 = matcher.group(3);
						String s4 = matcher.group(4);
						if (Integer.parseInt(s1) == (m + 1)) {
							int i3 = Integer.parseInt(s2) - 1 - beginIndex;
							if (i3 >= 0 && i3 < asList.size())
								asList.get(i3).solarFestival += s4 + " ";
						}
					}
				}
				
				pattern = Pattern.compile("^(\\d{2})(\\d)(\\d)([\\s\\*])(.+)$");
				int tmp3 = 0;
				for (int i2 = 0; i2 < CalendarUtils.wFtv.length; i2++) {
					matcher = pattern.matcher(CalendarUtils.wFtv[i2]);
					if (matcher.matches()) {
						String str = matcher.group(0);
						String str1 = matcher.group(1);
						String str2 = matcher.group(2);
						String str3 = matcher.group(3);
						String str4 = matcher.group(4);
						String str5 = matcher.group(5);
						if (Integer.parseInt(str1) == (m + 1)) {
							tmp1 = Integer.parseInt(str2);
							tmp2 = Integer.parseInt(str3);
							int i4 = ((this.firstWeek > tmp2) ? 7 : 0) + 7
									* (tmp1 - 1) + tmp2 - this.firstWeek
									- beginIndex;
							if (tmp1 < 5) {
								if (i4 >= 0 && i4 < asList.size())
									asList.get(((this.firstWeek > tmp2) ? 7 : 0)
											+ 7
											* (tmp1 - 1)
											+ tmp2
											- this.firstWeek).solarFestival += str5
											+ " ";
							} else {
								tmp1 -= 5;
								tmp3 = (this.firstWeek + this.length - 1) % 7;
								int i5 = this.length - tmp3 - 7 * tmp1 + tmp2
										- (tmp2 > tmp3 ? 7 : 0) - 1;
								if (i5 >= 0 && i5 < asList.size())
									asList.get(i5).solarFestival += str5 + " ";
							}
						}
					}
				}
				pattern = Pattern.compile("^(\\d{2})(.{2})([\\s\\*])(.+)$");

				for (int i3 = 0; i3 < CalendarUtils.lFtv.length; i3++) {

					matcher = pattern.matcher(CalendarUtils.lFtv[i3]);
					if (matcher.matches()) {
						String st = matcher.group();
						String st1 = matcher.group(1);
						String st2 = matcher.group(2);
						String st3 = matcher.group(3);
						String st4 = matcher.group(4);

						tmp1 = Integer.parseInt(st1) - firstLM;
						if (tmp1 == -11)
							tmp1 = 1;
						if (tmp1 >= 0 && tmp1 < n) {
							tmp2 = lDPOS[tmp1] + Integer.parseInt(st2) - 1
									- beginIndex;
							if (tmp2 >= 0 && tmp2 < asList.size()) {
								if (asList.get(tmp2).lMonth == Integer
										.parseInt(st1)
										&& asList.get(tmp2).lDay == Integer
												.parseInt(st2)
										&& !asList.get(tmp2).isLeap) {
									asList.get(tmp2).lunarFestival += st4 + " ";
								} else if (st3.equals("*")) {
									if (asList.get(tmp2).lunarFestival
											.indexOf(st4) == -1)// andy add
										asList.get(tmp2).lunarFestival += st4
												+ " ";
								}
							}
						}
					}
					if (m == 2 || m == 3) {
						Easter estDay = new Easter(y);
						if (m == estDay.m)
							asList.get(estDay.d - 1 - beginIndex).solarFestival = asList
									.get(estDay.d - 1 - beginIndex).solarFestival
									+ context.getResources().getString(
											R.string.zzzzz_reLife);
					}
					if ((this.firstWeek + 12) % 7 == 5)
						asList.get(12 - beginIndex).solarFestival = context
								.getResources().getString(R.string.zzzzz_blackFriday);
					if (y == currentYear && m == currentMonth)
						asList.get(currentDay - 1 - beginIndex).isToday = true;
				}
			}
		}
	}

	public CalendarDataElement get(int index) {
		return asList.get(index);
	}

	public int size() {
		return asList.size();
	}

	public static void main(String[] dd) {
		CalendarData c = new CalendarData(2011, 11);
		for (int i = 0; i < c.length; i++) {
			CalendarDataElement el = c.asList.get(i);
			el.printString();
		}
	}

	public static void init(Context context) {
		if (CalendarUtils.GAN == null || CalendarData.context == null) {// andy
			// add for test bug-687
			CalendarData.context = context.getApplicationContext();
			CalendarData.nStr1 = new String[CalendarData.nStr1Id.length];
			for (int i = 0; i < CalendarData.nStr1.length; i++) {
				CalendarData.nStr1[i] = CalendarData.context.getResources()
						.getString(CalendarData.nStr1Id[i]);
			}
			CalendarUtils.init(context);
		}
	}
	
	public static boolean needToModify(int y,int m, int d,ArrayList<CalendarDataElement> list){
		if( (y==2012 && m==11 && d==5)){
			list.get(6).solarTerms = CalendarUtils.solarTerm[22]; 
			return true;
		}else if(y==2013 && m==1 && d==2){
			list.get(3).solarTerms = CalendarUtils.solarTerm[2]; 
			return true;
		}else if(y==2012 && m==4 && d==20){
			list.get(19).solarTerms = CalendarUtils.solarTerm[9];
			return true;
		}else if(y==2012 && m==0 && d==19){
			list.get(20).solarTerms = CalendarUtils.solarTerm[1];
			return true;
		} else if(y==2013 && m==6 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[13];
			return true;
		} else if(y==2013 && m==11 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[23];
			return true;
		}else if(y==2014 && m==2 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[4];
			return true;
		}else if(y==2015 && m==0 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[0];
			return true;
		}else if(y==2016 && m==11 && d==5){
			list.get(6).solarTerms = CalendarUtils.solarTerm[22];
			return true;
		}else if(y==2017 && m==6 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[13];
			return true;
		}else if(y==2017 && m==11 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[23];
			return true;
		}else if(y==2018 && m==1 && d==17){
			list.get(18).solarTerms = CalendarUtils.solarTerm[3];
			return true;
		} else if(y==2018 && m==2 && d==19){
			list.get(20).solarTerms = CalendarUtils.solarTerm[5];
			return true;
		}else if(y==2019 && m==5 && d==21){
			list.get(20).solarTerms = CalendarUtils.solarTerm[11];
			return true;
		}else if(y==2020 && m==6 && d==6){
			list.get(5).solarTerms = CalendarUtils.solarTerm[12];
			return true;
		}else if(y==2020 && m==7 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[15];
			return true;
		}else if(y==2020 && m==11 && d==5){
			list.get(6).solarTerms = CalendarUtils.solarTerm[22];
			return true;
		}else if(y==2022 && m==1 && d==17){
			list.get(18).solarTerms = CalendarUtils.solarTerm[3];
			return true;
		}else if(y==2022 && m==8 && d==7){
			list.get(6).solarTerms = CalendarUtils.solarTerm[16];
			return true;
		}else if(y==2023 && m==5 && d==21){
			list.get(20).solarTerms = CalendarUtils.solarTerm[11];
			return true;
		}else if(y==2023 && m==9 && d==22){
			list.get(23).solarTerms = CalendarUtils.solarTerm[19];
			return true;
		}else if(y==2023 && m==10 && d==6){
			list.get(7).solarTerms = CalendarUtils.solarTerm[20];
			return true;
		}else if(y==2024 && m==7 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[15];
			return true;
		}else if(y==2026 && m==5 && d==5){
			list.get(4).solarTerms = CalendarUtils.solarTerm[10];
			return true;
		}else if(y==2030 && m==5 && d==5){
			list.get(4).solarTerms = CalendarUtils.solarTerm[10];
			return true;
		}else if(y==2033 && m==6 && d==21){
			list.get(22).solarTerms = CalendarUtils.solarTerm[13];
			return true;
		}else if(y==2034 && m==7 && d==6){
			list.get(7).solarTerms = CalendarUtils.solarTerm[14];
			return true;
		}else if(y==2035 && m==4 && d==5){
			list.get(4).solarTerms = CalendarUtils.solarTerm[8];
			return true;
		}else if(y==2035 && m==7 && d==7){
			list.get(6).solarTerms = CalendarUtils.solarTerm[14];
			return true;
		}else if(y==2037 && m==0 && d==18){
			list.get(19).solarTerms = CalendarUtils.solarTerm[1];
			return true;
		}else if(y==2039 && m==7 && d==7){
			list.get(6).solarTerms = CalendarUtils.solarTerm[14];
			return true;
		}else if(y==2040 && m==0 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[0];
			return true;
		}else if(y==2040 && m==9 && d==6){
			list.get(7).solarTerms = CalendarUtils.solarTerm[18];
			return true;
		}else if(y==2040 && m==10 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[21];
			return true;
		}else if(y==2041 && m==0 && d==18){
			list.get(19).solarTerms = CalendarUtils.solarTerm[1];
			return true;
		}else if(y==2041 && m==4 && d==20){
			list.get(19).solarTerms = CalendarUtils.solarTerm[9];
			return true;
		}else if(y==2042 && m==1 && d==2){
			list.get(3).solarTerms = CalendarUtils.solarTerm[2];
			return true;
		}else if(y==2043 && m==2 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[4];
			return true;
		}else if(y==2043 && m==7 && d==7){
			list.get(6).solarTerms = CalendarUtils.solarTerm[14];
			return true;
		}else if(y==2044 && m==0 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[0];
			return true;
		}else if(y==2044 && m==10 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[21];
			return true;
		}else if(y==2045 && m==0 && d==18){
			list.get(19).solarTerms = CalendarUtils.solarTerm[1];
			return true;
		}else if(y==2045 && m==3 && d==19){
			list.get(18).solarTerms = CalendarUtils.solarTerm[7];
			return true;
		}else if(y==2045 && m==4 && d==20){
			list.get(19).solarTerms = CalendarUtils.solarTerm[9];
			return true;
		}else if(y==2045 && m==11 && d==5){
			list.get(6).solarTerms = CalendarUtils.solarTerm[22];
			return true;
		}else if(y==2046 && m==1 && d==2){
			list.get(3).solarTerms = CalendarUtils.solarTerm[2];
			return true;
		}else if(y==2046 && m==6 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[13];
			return true;
		}else if(y==2046 && m==11 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[23];
			return true;
		}else if(y==2047 && m==2 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[4];
			return true;
		}else if(y==2048 && m==0 && d==4){
			list.get(5).solarTerms = CalendarUtils.solarTerm[0];
			return true;
		}else if(y==2048 && m==5 && d==20){
			list.get(19).solarTerms = CalendarUtils.solarTerm[11];
			return true;
		}else if(y==2049 && m==3 && d==3){
			list.get(4).solarTerms = CalendarUtils.solarTerm[6];
			return true;
		}else if(y==2049 && m==3 && d==18){
			list.get(19).solarTerms = CalendarUtils.solarTerm[7];
			return true;
		}else if(y==2049 && m==6 && d==6){
			list.get(5).solarTerms = CalendarUtils.solarTerm[12];
			return true;
		}else if(y==2049 && m==7 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[15];
			return true;
		}else if(y==2049 && m==11 && d==5){
			list.get(6).solarTerms = CalendarUtils.solarTerm[22];
			return true;
		}else if(y==2050 && m==6 && d==22){
			list.get(21).solarTerms = CalendarUtils.solarTerm[13];
			return true;
		}else if(y==2050 && m==8 && d==6){
			list.get(7).solarTerms = CalendarUtils.solarTerm[16];
			return true;
		}else if(y==2050 && m==11 && d==20){
			list.get(21).solarTerms = CalendarUtils.solarTerm[23];
			return true;
		}
		
		return false;
	}
}
