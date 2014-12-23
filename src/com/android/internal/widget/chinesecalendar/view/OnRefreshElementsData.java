package com.android.internal.widget.chinesecalendar.view;

import java.util.ArrayList;

import com.android.internal.widget.chinesecalendar.calendar.CalendarDataElement;



public interface OnRefreshElementsData {
	public void onReturnData(int year, int month, int date);
	public void onSetPostion(int position);
	public void onReturnDayElement(CalendarDataElement element);
	public void onSelectRow(ArrayList<CalendarDataElement> curCalList, int position);
	public void onSetBottomHeight(int dataSize);
}
