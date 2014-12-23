package com.android.internal.widget.chinesecalendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.widget.chinesecalendar.calendar.CalendarData;
import com.android.internal.widget.chinesecalendar.calendar.CalendarDataElement;
import com.android.internal.widget.chinesecalendar.calendar.CalendarLunarToSolar;
import com.android.internal.widget.chinesecalendar.calendar.CalendarUtils;
import com.android.internal.widget.chinesecalendar.calendar.LunarDate;
import com.inde.shiningdays.R;

public class DateSettingDialog extends AlertDialog implements OnClickListener {

	private final int VISIBLE_COUNT = 3;
	
	public static int year;
	public static int month;
	public static int date;

	TextView mTitleTv;
	Button okButton;
	Button cancelButton;
	RadioButton solarisButton;
	RadioButton lunarButton;
	ConstellationWheelView yearWheel;
	ConstellationWheelView monthWheel;
	ConstellationWheelView dateWheel;

	private int mSearchYear;
	private int mSearchMonth;
	private int mSearchDate;
	
	Resources r;
	ArrayList<String> lunarMonthes;
	ArrayList<String> lunarDates;
	
	private Activity mContext;
	private OnRefreshElementsData mListener;

	public DateSettingDialog(Context context) {
		super(context);
		mContext = (Activity)context;
	}

	public DateSettingDialog(Context context, String currentDate) {
		super(context, android.R.style.Theme_Holo_Light_Dialog);
		mContext = (Activity)context;

		boolean flag = false;
		if(currentDate != null) {
			String[] arr = currentDate.split("-");
			if(arr.length == 3) {
				try {
					int year = Integer.parseInt(arr[0]);
					int month = Integer.parseInt(arr[1]);
					int day = Integer.parseInt(arr[2]);
					DateSettingDialog.year = year;
					DateSettingDialog.month = month -1;
					DateSettingDialog.date = day;
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if(!flag) {
			Calendar cd = Calendar.getInstance();
			DateSettingDialog.year = cd.get(Calendar.YEAR);
			DateSettingDialog.month = cd.get(Calendar.MONTH);
			DateSettingDialog.date = cd.get(Calendar.DAY_OF_MONTH);
		}

		CalendarUtils.init(context); // 初始化农历信息，包括黄历信息、宜忌信息、犯冲信息、五行、值日
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.zzzzz_date_setting_edit, null);
		setContentView(layout);

		r = this.getContext().getResources();
		this.lunarMonthes = new ArrayList<String>();
		for (int mIndex = 1; mIndex <= 12; mIndex++) {
			lunarMonthes.add(CalendarUtils.getLunarMonth(r, mIndex));
		}

		this.lunarDates = new ArrayList<String>();
		for (int mIndex = 1; mIndex <= 30; mIndex++) {
			lunarDates.add(CalendarUtils.getLunarDay(mIndex));
		}
		
		mTitleTv = (TextView) layout.findViewById(R.id.zzzzz_dialog_title);
		solarisButton = (RadioButton) findViewById(R.id.zzzzz_solarisButton);
		solarisButton.setChecked(true);
		lunarButton = (RadioButton) findViewById(R.id.zzzzz_lunarButton);
		solarisButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setSolarisTime(isChecked);
			}
		});
		lunarButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setLunarTime(isChecked);
			}

		});
		yearWheel = (ConstellationWheelView) findViewById(R.id.zzzzz_yearWheel);
		monthWheel = (ConstellationWheelView) findViewById(R.id.zzzzz_monthWheel);
		dateWheel = (ConstellationWheelView) findViewById(R.id.zzzzz_dateWheel);
		refreshAdapter(true, year, month, date);

		okButton = (Button) findViewById(R.id.zzzzz_okButton);
		cancelButton = (Button) findViewById(R.id.zzzzz_cancelButton);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	void refreshAdapter(boolean isSolar, int newYear, int newMonth,
			int newDate) {
		if (isSolar) {
			yearWheel.setAdapter(new NumericWheelAdapter(1900, 2100));
			yearWheel.setCurrentItem(newYear - 1900);
			yearWheel.setCyclic(true);
//			yearWheel.setLabel(r.getString(R.string.zzzzz_yearStr));
			yearWheel.setVisibleItems(VISIBLE_COUNT);

			monthWheel.setAdapter(new NumericWheelAdapter(1, 12));
			monthWheel.setCurrentItem(newMonth);
			monthWheel.setCyclic(true);
//			monthWheel.setLabel(r.getString(R.string.zzzzz_monthStr));
			monthWheel.setVisibleItems(VISIBLE_COUNT);

			int days = CalendarUtils.solarDays(newYear, newMonth);
			dateWheel.setAdapter(new NumericWheelAdapter(1, days));
			dateWheel.setCurrentItem(newDate - 1);
			dateWheel.setCyclic(true);
//			dateWheel.setLabel(r.getString(R.string.zzzzz_dayStr));
			dateWheel.setVisibleItems(VISIBLE_COUNT);

			setChangingListener(isSolar);
		} else {
			yearWheel.setAdapter(new NumericWheelAdapter(1900, 2100));
			yearWheel.setCurrentItem(newYear - 1900);
			yearWheel.setCyclic(true);
//			yearWheel.setLabel(r.getString(R.string.zzzzz_yearStr));
			yearWheel.setVisibleItems(VISIBLE_COUNT);

			// month date lunar adapter.
			monthWheel.setAdapter(new ArrayWheelAdapter<String>(
					(String[]) (lunarMonthes.toArray(new String[1]))));
			monthWheel.setCurrentItem(newMonth);
//			monthWheel.setLabel(r.getString(R.string.zzzzz_monthStr));
			monthWheel.setVisibleItems(VISIBLE_COUNT);
			monthWheel.setCyclic(true);

			int lunarMonthDayCount = CalendarUtils.monthDays(newYear,
					newMonth + 1);
			if (lunarMonthDayCount > lunarDates.size()) {
				for (int temp = lunarDates.size(); temp < lunarMonthDayCount; temp++) {
					lunarDates.add(CalendarUtils.getLunarDay(temp + 1));
				}
			} else if (lunarMonthDayCount < lunarDates.size()) {
				for (int temp = lunarDates.size() - 1; temp > lunarMonthDayCount - 1; temp--) {
					lunarDates.remove(temp);
				}
			}
			dateWheel.setAdapter(new ArrayWheelAdapter<String>(
					(String[]) (lunarDates.toArray(new String[1]))));
			dateWheel.setCurrentItem(newDate - 1);
			dateWheel.setLabel("");
			dateWheel.setVisibleItems(VISIBLE_COUNT);
			dateWheel.setCyclic(true);

			setChangingListener(isSolar);
		}
	}

	void setChangingListener(boolean isSolar) {
		final boolean isLunar = !isSolar;
		monthWheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(ComWheelView wheel, int oldValue,
					int newValue) {
				if (!isLunar) {
					int days = CalendarUtils.solarDays(yearWheel.getCurrentItem() + 1900, newValue);
					dateWheel.setAdapter(new NumericWheelAdapter(1, days));
					dateWheel.setCurrentItem(date - 1);
					dateWheel.setCyclic(true);
					dateWheel.setLabel(r.getString(R.string.zzzzz_dayStr));
					dateWheel.setVisibleItems(VISIBLE_COUNT);
				} else {
					int lunarMonthDayCount = CalendarUtils.monthDays(yearWheel.getCurrentItem() + 1900, newValue + 1);
					if (lunarMonthDayCount > lunarDates.size()) {
						for (int temp = lunarDates.size(); temp < lunarMonthDayCount; temp++) {
							lunarDates.add(CalendarUtils.getLunarDay(temp + 1));
						}
					} else if (lunarMonthDayCount < lunarDates.size()) {
						for (int temp = lunarDates.size() - 1; temp > lunarMonthDayCount - 1; temp--) {
							lunarDates.remove(temp);
						}
					}
					dateWheel.setAdapter(new ArrayWheelAdapter<String>((String[]) (lunarDates.toArray(new String[1]))));
					dateWheel.setCurrentItem(date - 1);
					dateWheel.setLabel("");
					dateWheel.setVisibleItems(VISIBLE_COUNT);
					dateWheel.setCyclic(true);
				}
			}
		});

		yearWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(ComWheelView wheel, int oldValue,
					int newValue) {
				if (!isLunar) {
					int days = CalendarUtils.solarDays(newValue + 1900, monthWheel.getCurrentItem());
					dateWheel.setAdapter(new NumericWheelAdapter(1, days));
					int dd = date;
					if (date - 1 >= days) {
						dd = days;
					}
					dateWheel.setCurrentItem(dd - 1);
					dateWheel.setCyclic(true);
					dateWheel.setLabel(r.getString(R.string.zzzzz_dayStr));
					dateWheel.setVisibleItems(VISIBLE_COUNT);
				} else {
					int lunarMonthDayCount = CalendarUtils.monthDays(
							newValue + 1900,
							monthWheel.getCurrentItem() + 1);
					if (lunarMonthDayCount > lunarDates.size()) {
						for (int temp = lunarDates.size(); temp < lunarMonthDayCount; temp++) {
							lunarDates.add(CalendarUtils.getLunarDay(temp + 1));
						}
					} else if (lunarMonthDayCount < lunarDates.size()) {
						for (int temp = lunarDates.size() - 1; temp > lunarMonthDayCount - 1; temp--) {
							lunarDates.remove(temp);
						}
					}
					dateWheel.setAdapter(new ArrayWheelAdapter<String>((String[]) (lunarDates.toArray(new String[1]))));
					dateWheel.setCurrentItem(date - 1);
					dateWheel.setLabel("");
					dateWheel.setVisibleItems(VISIBLE_COUNT);
					dateWheel.setCyclic(true);
				}
			}

		});

		dateWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(ComWheelView wheel, int oldValue, int newValue) {
				
			}
			
		});
	}
	
	private void setLunarTime(boolean isChecked) {
		if (isChecked) {
			@SuppressWarnings("deprecation")
			Date cc2 = new Date(yearWheel.getCurrentItem() + 1900,
					monthWheel.getCurrentItem(), dateWheel.getCurrentItem() + 1);
			LunarDate ld = new LunarDate(cc2);
			if (ld.isNotSupportLunarDate()) {
				Toast.makeText(mContext, r.getString(R.string.zzzzz_notSupportLunarMessage2), Toast.LENGTH_SHORT).show();
				return;
			}
			refreshAdapter(false, ld.year, ld.month - 1, ld.date);
		}
	}
	
	private void setSolarisTime(boolean isChecked) {
		if (isChecked) {
			Calendar cc = null;
			try {
				cc = CalendarLunarToSolar.sCalendarLundarToSolar(
						yearWheel.getCurrentItem() + 1900,
						monthWheel.getCurrentItem() + 1,
						dateWheel.getCurrentItem() + 1);
			} catch (Exception e) {
				Toast.makeText(mContext, r.getString(R.string.zzzzz_notSupportLunarMessage), Toast.LENGTH_SHORT).show();
				refreshAdapter(true, year, month, date);
				return;
			}
			int lyear = yearWheel.getCurrentItem() + 1900;
			int lmonth = monthWheel.getCurrentItem() + 1;
			int ldate = dateWheel.getCurrentItem() + 1;

			int year1 = cc.get(Calendar.YEAR);
			int month1 = cc.get(Calendar.MONTH);
			int date1 = cc.get(Calendar.DATE);

			CalendarData calendar = new CalendarData(year1, month1);
			CalendarDataElement cde = calendar.get(date1 - 1);

			if (cde.lYear == lyear && cde.lMonth == lmonth
					&& cde.lDay == ldate) {
			} else if (cde.lYear < lyear 
					|| (cde.lYear == lyear && cde.lMonth < lmonth)
					|| (cde.lYear == lyear
							&& cde.lMonth == lmonth && cde.lDay < ldate)) {
				int d = cde.sDay;

				int size = calendar.asList.size();
				if (d < size) {
					cde = calendar.get(d);
				} else {
					if (month1 == 11) {
						year1++;
						month1 = 0;
					} else {
						month1++;
					}
					calendar = new CalendarData(year1, month1);
					cde = calendar.get(0);
				}
			} else {
				int d = cde.sDay;
				if (cde.lYear == lyear && cde.lMonth == lmonth) {
					d -= cde.lDay - ldate;
				} else {
					d--;
				}
				if (d >= 1) {
					cde = calendar.get(d - 1);
				} else {
					if (month1 == 0) {
						year1--;
						month1 = 11;
					} else {
						month1--;
					}
					calendar = new CalendarData(year1, month1);
					cde = calendar.get(calendar.asList.size() - 1);
				}
			}
			refreshAdapter(true, cde.sYear, cde.sMonth - 1, cde.sDay);
		}
	}
	
	private boolean getConstellationTime() {
		if (solarisButton.isChecked()) {
			mSearchYear = yearWheel.getCurrentItem() + 1900;
			mSearchMonth = monthWheel.getCurrentItem();
			mSearchDate = dateWheel.getCurrentItem() + 1;
			if (!CalendarUtils.isSupportedYearMonth(mContext, mSearchYear, mSearchMonth )) {
				// 该年月不存在
				return false;
			}
			return true;
		}else if (lunarButton.isChecked()) { 
			int lyear = yearWheel.getCurrentItem() + 1900;
			int lmonth = monthWheel.getCurrentItem() + 1;
			int ldate = dateWheel.getCurrentItem() + 1;
			if (lyear > 2050) {
				Toast.makeText(mContext, r.getString(R.string.zzzzz_notSupport2050LunarMessage), Toast.LENGTH_SHORT).show();
				return false;
			}

			Calendar cd = null;
			try {
				cd = CalendarLunarToSolar.sCalendarLundarToSolar(lyear, lmonth, ldate);
			} catch (Exception e) {
				Toast.makeText(mContext, r.getString(R.string.zzzzz_notSupportLunarMessage), Toast.LENGTH_SHORT).show();
				return false;
			}
			int year1 = cd.get(Calendar.YEAR);
			int month1 = cd.get(Calendar.MONTH);
			int date1 = cd.get(Calendar.DATE);

			CalendarData cda = new CalendarData(year1, month1);
			CalendarDataElement ele = cda.get(date1 - 1);

			if (ele.lYear == lyear && ele.lMonth == lmonth && ele.lDay == ldate) {
				
			} else if (ele.lYear < lyear 
					|| (ele.lYear == lyear && ele.lMonth < lmonth)
					|| (ele.lYear == lyear && ele.lMonth == lmonth && ele.lDay < ldate)) {
				int d = ele.sDay;
				int size = cda.asList.size();
				if (d < size) {
					ele = cda.get(d);
				} else {
					if (month1 == 11) {
						month1 = 0;
						year1++;
					} else {
						month1++;
					}
					cda = new CalendarData(year1, month1);
					ele = cda.get(0);
				}
			} else {
				int d = ele.sDay;
				if (ele.lYear == lyear && ele.lMonth == lmonth) {
					d -= ele.lDay - ldate;
				} else {
					d--;
				}
				if (d >= 1) {
					ele = cda.get(d - 1);
				} else {
					if (month1 == 0) {
						month1 = 11;
						year1--;
					} else {
						month1--;
					}
					cda = new CalendarData(year1, month1);
					ele = cda.get(cda.asList.size() - 1);
				}
			}
			if (!CalendarUtils.isSupportedYearMonth(mContext, ele.sYear, ele.sMonth - 1)) {
				return false;
			}
			mSearchYear = ele.sYear;
			mSearchMonth = ele.sMonth - 1;
			mSearchDate = ele.sDay;
			return true;
		}
		return false;
	}
	
	public void setRefreshListener(OnRefreshElementsData listener){
		mListener = listener;
	}

	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.zzzzz_okButton:
			if(getConstellationTime()){
				mListener.onReturnData(mSearchYear, mSearchMonth, mSearchDate);
			}
			break;
		case R.id.zzzzz_cancelButton:
			break;
		}
		this.dismiss();
	}

}
