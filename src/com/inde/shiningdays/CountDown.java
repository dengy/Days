package com.inde.shiningdays;

import android.net.Uri;
import android.provider.BaseColumns;

public final class CountDown implements BaseColumns{
	
    // This class cannot be instantiated
    private CountDown() {}
    
    public static final String AUTHORITY = "com.inde.shiningdays.CountDown";
    
    /**
     * The content:// style URL for this table
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/countdown");
    
    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

    /**
     * The default sort order for this table
     */
    public static final String ORDER_BY = "orderby";
    public static final String DEFAULT_SORT_ORDER = "created DESC";
    public static final String SORT_END_DATE_ASC = "endDateTime ASC";
    
    
    public static final String TITLE = "title";
    
    public static final String REMARK = "remark";
    
    public static final String END_DATE = "enddate";
    public static final String END_DATE_TIME = "endDateTime";
    public static final String END_TIME = "endtime";
    public static final String REMIND_DATE = "reminddate";
    public static final String REMIND_BELL = "remindbell";
    public static final String PRIORITY = "priority";
    public static final String STARRED = "starred";
    public static final String STATE = "state";
    public static final String CREATED_DATE = "created";
    public static final String WIDGET_IDS = "widgetIds";
    
    public static final String STATE_OPENED = "Opened";
    public static final String STATE_CLOSED = "Closed";
    
    public static final String TOP_INDEX = "topindex";
    
    public static final String TOP_SORT_ORDER =  TOP_INDEX + " DESC," + CREATED_DATE + " DESC";
    
    public static final Uri CONTENT_STATE_OPEN_URI = Uri.parse("content://" + AUTHORITY + "/countdown/state/" + STATE_OPENED);
    public static final Uri CONTENT_STATE_CLOSE_URI = Uri.parse("content://" + AUTHORITY + "/countdown/state/" + STATE_CLOSED);
    
    public static final Uri CONTENT_TYPE_URI = Uri.parse("content://" + AUTHORITY + "/countdown/type");
    
    public static final String COUNT_DOWN = "countdown";
    
    //public static final String DEFAULT_FOR_DATE = "_";


}
