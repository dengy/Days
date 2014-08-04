package android.first.countdown;



import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CountDownProvider extends ContentProvider {
	private static final String TAG = "ContentProvider";

	private static final String DATABASE_NAME = "count_down.db";
    private static final int DATABASE_VERSION = 6;
    
    private static final int COUNTDOWN = 1;
    private static final int COUNTDOWN_ID = 2;
    private static final int COUNTDOWN_STATE_OPENED = 3;
    private static final int COUNTDOWN_STATE_CLOSED = 4;
    private static final int COUNTDOWN_TYPE = 5;
    private static HashMap<String, String> sCountdownProjectionMap;
    
    private static final String COUNTDOWN_TABLE_NAME = "countdown";
    private static final UriMatcher sUriMatcher;
    
    private DatabaseHelper mOpenHelper;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}

		@Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + COUNTDOWN_TABLE_NAME + " ("
                    + CountDown._ID + " INTEGER PRIMARY KEY,"
                    + CountDown.TITLE + " TEXT,"
                    + CountDown.STARRED + " INTEGER,"
                    + CountDown.PRIORITY + " INTEGER,"
                    + CountDown.END_DATE + " TEXT,"
                    + CountDown.END_TIME + " TEXT,"
                    + CountDown.REMIND_DATE + " TEXT,"
                    + CountDown.REMIND_BELL + " TEXT,"
                    + CountDown.REMARK + " TEXT,"
                    + CountDown.STATE + " TEXT,"
                    + CountDown.CREATED_DATE + " INTEGER,"
                    + CountDown.WIDGET_IDS + " TEXT,"
                    + CountDown.TOP_INDEX + " INTEGER"
                    + ");");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + COUNTDOWN_TABLE_NAME);
            onCreate(db);
		}
    	
    }
    
    static {
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	sUriMatcher.addURI(CountDown.AUTHORITY, "countdown/", COUNTDOWN);
    	sUriMatcher.addURI(CountDown.AUTHORITY, "countdown/#", COUNTDOWN_ID);
    	sUriMatcher.addURI(CountDown.AUTHORITY, "countdown/state/" + CountDown.STATE_OPENED, COUNTDOWN_STATE_OPENED);
    	sUriMatcher.addURI(CountDown.AUTHORITY, "countdown/state/" + CountDown.STATE_CLOSED, COUNTDOWN_STATE_CLOSED);
    	sUriMatcher.addURI(CountDown.AUTHORITY, "countdown/type", COUNTDOWN_TYPE);
    	
    	sCountdownProjectionMap = new HashMap<String,String>();
    	sCountdownProjectionMap.put(CountDown._ID,CountDown._ID);
    	sCountdownProjectionMap.put(CountDown.TITLE, CountDown.TITLE);
    	sCountdownProjectionMap.put(CountDown.PRIORITY, CountDown.PRIORITY);
    	sCountdownProjectionMap.put(CountDown.STARRED, CountDown.STARRED);
    	
    	sCountdownProjectionMap.put(CountDown.END_DATE, CountDown.END_DATE);
    	sCountdownProjectionMap.put(CountDown.END_TIME, CountDown.END_TIME);
    	sCountdownProjectionMap.put(CountDown.REMIND_DATE, CountDown.REMIND_DATE);
    	sCountdownProjectionMap.put(CountDown.REMIND_BELL, CountDown.REMIND_BELL);
    	sCountdownProjectionMap.put(CountDown.CREATED_DATE, CountDown.CREATED_DATE);
    	sCountdownProjectionMap.put(CountDown.REMARK, CountDown.REMARK);
    	sCountdownProjectionMap.put(CountDown.STATE, CountDown.STATE);//add on 2013.3.21
    	sCountdownProjectionMap.put(CountDown.WIDGET_IDS, CountDown.WIDGET_IDS);
    	sCountdownProjectionMap.put(CountDown.TOP_INDEX, CountDown.TOP_INDEX);
    }
    
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case COUNTDOWN:
            count = db.delete(COUNTDOWN_TABLE_NAME, where, whereArgs);
            break;
        case COUNTDOWN_ID:
            String countdownId = uri.getPathSegments().get(1);
            count = db.delete(COUNTDOWN_TABLE_NAME, CountDown._ID + "=" + countdownId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		int match = sUriMatcher.match(uri);
		switch(match) {
		case COUNTDOWN:
		case COUNTDOWN_STATE_OPENED:
		case COUNTDOWN_STATE_CLOSED:
		case COUNTDOWN_TYPE:
			return "vnd.android.cursor.dir/countdown";
		case COUNTDOWN_ID:
            return "vnd.android.cursor.item/countdown";
        default:
        	return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != COUNTDOWN) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        
        Long now = Long.valueOf(System.currentTimeMillis());
        
     // Make sure that the fields are all set
        if (values.containsKey(CountDown.TITLE) == false) {
        	Resources r = Resources.getSystem();
            values.put(CountDown.TITLE,  r.getString(android.R.string.untitled));
        }
        
        if (values.containsKey(CountDown.REMARK) == false) {
            values.put(CountDown.REMARK,  "");
        }
        
        if (values.containsKey(CountDown.PRIORITY) == false) {
            values.put(CountDown.PRIORITY,  Constant.DEFAULT_TYPE);
        }
        
        if (values.containsKey(CountDown.STARRED) == false) {
            values.put(CountDown.STARRED,  0);
        }
        
        if (values.containsKey(CountDown.END_DATE) == false) {
            values.put(CountDown.END_DATE,  "");
        }
        
        if (values.containsKey(CountDown.END_TIME) == false || 
        		CountDown.DEFAULT_FOR_DATE.equals(values.get(CountDown.END_TIME))) {
            values.put(CountDown.END_TIME,  "");
        }
        
        if (values.containsKey(CountDown.REMIND_DATE) == false ||
        		CountDown.DEFAULT_FOR_DATE.equals(values.get(CountDown.REMIND_DATE))) {
            values.put(CountDown.REMIND_DATE,  "");
        }
        
        if (values.containsKey(CountDown.REMIND_BELL) == false) {
            values.put(CountDown.REMIND_BELL,  "");
        }
        
        if (values.containsKey(CountDown.CREATED_DATE) == false) {
            values.put(CountDown.CREATED_DATE,  now);
        }
        
        if (values.containsKey(CountDown.STATE) == false) {
            values.put(CountDown.STATE,  CountDown.STATE_OPENED);
        }
        
        if (values.containsKey(CountDown.WIDGET_IDS) == false) {
            values.put(CountDown.WIDGET_IDS,  "");
        }
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(COUNTDOWN_TABLE_NAME, null, values);
        if (rowId > 0) {
        	 Uri noteUri = ContentUris.withAppendedId(CountDown.CONTENT_URI, rowId);
             getContext().getContentResolver().notifyChange(noteUri, null);
             return noteUri;
        }
        

        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(COUNTDOWN_TABLE_NAME);

		switch(sUriMatcher.match(uri)) {
		case COUNTDOWN:
			qb.setProjectionMap(sCountdownProjectionMap);
			break;
		case COUNTDOWN_ID:
			qb.setProjectionMap(sCountdownProjectionMap);
			qb.appendWhere(CountDown._ID + "=" + uri.getPathSegments().get(1));
			break;
		case COUNTDOWN_STATE_OPENED:
			qb.setProjectionMap(sCountdownProjectionMap);
			qb.appendWhere(CountDown.STATE + "=" + "'"+ CountDown.STATE_OPENED + "'");
			break;
		case COUNTDOWN_STATE_CLOSED:
			qb.setProjectionMap(sCountdownProjectionMap);
			qb.appendWhere(CountDown.STATE + "=" + "'" + CountDown.STATE_CLOSED + "'");
			break;
		case COUNTDOWN_TYPE:
			qb.setProjectionMap(sCountdownProjectionMap);
			//qb.appendWhere(CountDown.PRIORITY + "=" + selection);
			break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);

		}

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = CountDown.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        
        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		switch(sUriMatcher.match(uri)) {
		case COUNTDOWN:
			count = db.update(COUNTDOWN_TABLE_NAME, values, where, whereArgs);
            break;
		case COUNTDOWN_ID:
			String countdownId = uri.getPathSegments().get(1);
			if(values.size() == 1 && values.containsKey(CountDown.TOP_INDEX)) {
				//when only update top_index
				StringBuilder sql = new StringBuilder();
				sql.append("update ").append(COUNTDOWN_TABLE_NAME).append(" set ").append(CountDown.TOP_INDEX).
				append("=").append(values.getAsInteger(CountDown.TOP_INDEX)).append(" where ").append(CountDown._ID).
				append("=").append(countdownId);
				db.execSQL(sql.toString());
			} else if(values.size() == 1 && values.containsKey(CountDown.PRIORITY)) {
				//when only update top_index
				StringBuilder sql = new StringBuilder();
				sql.append("update ").append(COUNTDOWN_TABLE_NAME).append(" set ").append(CountDown.PRIORITY).
				append("=").append(values.getAsInteger(CountDown.PRIORITY)).append(" where ").append(CountDown._ID).
				append("=").append(countdownId);
				db.execSQL(sql.toString());
			} else {
				count = db.update(COUNTDOWN_TABLE_NAME, values, CountDown._ID + "=" + countdownId
	                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			}
			
            
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}