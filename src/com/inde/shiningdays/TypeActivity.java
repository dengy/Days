package com.inde.shiningdays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inde.shiningdays.util.BaseActivity;
import com.inde.shiningdays.util.SharedPrefsUtil;
import com.inde.shiningdays.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class TypeActivity extends BaseActivity implements OnClickListener{
	private ListView defaultTypeList;
	private ListView customTypeList;
	private View mAddType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_type);
		initViews();
	}
	
	@Override
    public void onResume() {
    	super.onResume();
    	//umeng sdk
    	MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
    	super.onPause();
    	//umeng sdk
    	MobclickAgent.onPause(this);
    }
	
	private void initViews() {
		defaultTypeList = (ListView) this.findViewById(R.id.default_type_list);
		customTypeList = (ListView) this.findViewById(R.id.custom_type_list);
		mAddType = this.findViewById(R.id.add_type);
		mAddType.setOnClickListener(this);
		findViewById(R.id.type_back).setOnClickListener(this);
		
		
		SimpleAdapter defaultAdapter = new SimpleAdapter(this, getDefaultData(this, false), R.layout.default_type_list_item,
				new String[] {"icon", "type"}, new int[] {R.id.default_type_icon, R.id.default_type});
		defaultTypeList.setAdapter(defaultAdapter);
		loadCustomTypeList(false);
	}

	private void loadCustomTypeList(boolean isFocus) {
		CustomTypeAdapter customAdapter = new CustomTypeAdapter(this, getCustomData(this, false), R.layout.custom_type_list_item,
				new String[] {"icon", "type"}, new int[] {R.id.delete_custom_type, R.id.custom_type}, isFocus);
		customTypeList.setAdapter(customAdapter);
	}
	public static List<Map<String, Object>> getDefaultData(Context context, boolean isContainsAll) {
		String[] mTypes = Utils.getDefaultTypes(context);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        
        if(isContainsAll) {
        	map = new HashMap<String, Object>();
        	map.put("type", mTypes[0]);
            map.put("icon", R.drawable.all_icon);
            list.add(map);
        }
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[1]);
        map.put("icon", R.drawable.life_icon);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[2]);
        map.put("icon", R.drawable.work_icon);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[3]);
        map.put("icon", R.drawable.study_icon);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[4]);
        map.put("icon", R.drawable.memorial_day_icon);
        list.add(map);
        return list;
	}
	
	public static List<Map<String, Object>> getCustomData(Context context, boolean isDrawerList) {
//		String[] customTypes = Utils.getCustomTypes(context);
//		int index = customTypes.length;
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for(int i = 0; i < index; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("typeKey", customTypes[i]);
//	        map.put("type", customTypes[i]);
//	        if(isDrawerList) {
//	        	map.put("icon", R.drawable.custom_type_icon);
//	        } else {
//	        	map.put("icon", R.drawable.type_delete);
//	        }
//	        
//	        
//	        list.add(map);			 
//		}
		
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(context, Constant.CUSTOM_TYPE_LIST_PREF);
		Map<String,?> typeMap = prefs.getAll();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(String key : typeMap.keySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("typeKey", key);
	        map.put("type", typeMap.get(key));
	        if(isDrawerList) {
	        	map.put("icon", R.drawable.custom_type_icon);
	        } else {
	        	map.put("icon", R.drawable.type_delete);
	        }
	        
	        list.add(map);			 

		}
		
		
        return list;
	}
	
	/**
	 * show a dialog to input title,click Ok to save and click Cancel to quit
	 */
	private void showTypeDialog(final String typeKey, String currentType) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    final View layout = inflater.inflate(R.layout.add_type, null);
	    final EditText inputTitleEditText = (EditText)layout.findViewById(R.id.input_type);
		if(currentType != null && !"".equals(currentType)) {
			inputTitleEditText.setText(currentType);
		}
	    
	    builder.setView(layout)
	    .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//typeEditText.setText(inputTitleEditText.getText());
				String inputType = inputTitleEditText.getText().toString();
				if(!"".equals(inputType)) {
					saveType(typeKey, inputType);
				} else {
					Toast.makeText(layout.getContext(), R.string.typeMust, Toast.LENGTH_SHORT).show();
				}
				
			}
			
	    }).setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
	
	private void saveType(String typeKey, String inputType) {
		if(inputType == null) {
			return;
		}
		
		inputType = inputType.trim();
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(this, Constant.CUSTOM_TYPE_LIST_PREF);
		
		if(typeKey == null) {//add
			Map map = prefs.getAll();
			if(map.containsValue(inputType)) {
				Toast.makeText(this, inputType + " " + getResources().getString(R.string.already_exist), Toast.LENGTH_SHORT).show();
				return;
			}
			
			int index = getMaxIndexInPrefs(prefs);
			Editor editor = prefs.edit();
			editor.putString((++index) + "", inputType);
			editor.commit();
		} else {//edit
			Editor editor = prefs.edit();
			editor.putString(typeKey, inputType);
			editor.commit();
		}
		
		//SimpleAdapter adapter = (SimpleAdapter)customTypeList.getAdapter();
		//adapter.notifyDataSetChanged();
		//customTypeList.setAdapter(adapter);
		loadCustomTypeList(true);
	}
	
	private int getMaxIndexInPrefs(SharedPreferences prefs) {
		Map<String,?> typeMap = prefs.getAll();
		//[] keySet = (String[])typeMap.keySet().toArray();
		if(typeMap.keySet().size() == 0)
			return 0;
		
		List<Integer> keySet = new ArrayList<Integer>();
		for(String key : typeMap.keySet()) {
			keySet.add(Integer.parseInt(key));
		}
		return Collections.max(keySet);
		
	}
	
	private void deleteType(String typeKey) {
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(this , Constant.CUSTOM_TYPE_LIST_PREF);
		String type = prefs.getString(typeKey, null);
		if(type != null) {
			Cursor cursor = getCountDownByType(type);
			int count = cursor.getCount();
			if(count > 0) {
				showDeleteConfirmDialog(getResources().getString(R.string.delete_type_with_tasks_confirm), cursor, typeKey, type);
			} else {
				delete(typeKey);
			}
		}
	}
	
	private void showDeleteConfirmDialog(String title, final Cursor cursor, final String typeKey, final String type) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View layout = inflater.inflate(R.layout.confirm_dialog, null);
	    TextView confirmTitle = (TextView)layout.findViewById(R.id.confirmTitle);
	    confirmTitle.setText(title); 
	    
	    builder.setView(layout)
	    .setPositiveButton("ȷ��ɾ��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updateCountDownType(cursor, getResources().getString(R.string.default_type));
				delete(typeKey);
			}
			
	    }).setNegativeButton("����", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
	
	private void updateCountDownType(Cursor cursor, String type) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CountDown.PRIORITY, type);
		while(cursor.moveToNext()) {
			Integer id = cursor.getInt(cursor.getColumnIndex(CountDown._ID));
			Uri uri = ContentUris.withAppendedId(CountDown.CONTENT_URI, id);
			getContentResolver().update(uri, contentValues, null, null);
		}
	}
	
	private Cursor getCountDownByType(String type)  {
		Cursor cursor = this.managedQuery(CountDown.CONTENT_TYPE_URI, new String[] {CountDown._ID, CountDown.TITLE, CountDown.STARRED, 
	        	CountDown.END_DATE, CountDown.END_TIME, CountDown.REMIND_BELL, CountDown.STATE, CountDown.PRIORITY, 
	        	CountDown.WIDGET_IDS}, CountDown.PRIORITY + "=?", new String[]{type},
	                CountDown.DEFAULT_SORT_ORDER); 
		
		return cursor;
	}
	
	private void delete(String typeKey) {
		SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(this , Constant.CUSTOM_TYPE_LIST_PREF);
		Editor editor = prefs.edit();
		editor.remove(typeKey);
		editor.commit();
		Toast.makeText(this, R.string.operationSuccess, Toast.LENGTH_SHORT).show();
		loadCustomTypeList(false);
	}
	
	class CustomTypeAdapter extends SimpleAdapter {

	    private List<Map<String, Object>> results;

	    public CustomTypeAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to, boolean isFocus) {
	        super(context, data, resource, from, to);
	        this.results = data;
	    }

	    public View getView(int position, View convertView, ViewGroup parent){
	    	final int index = position;
	    	View view = super.getView(position, convertView, parent);
	    	final TextView customType = (TextView)view.findViewById(R.id.custom_type);
	    	View editCustomType = view.findViewById(R.id.edit_custom_type);
	    	editCustomType.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String typeKey = results.get(index).get("typeKey").toString();
					showTypeDialog(typeKey, customType.getText().toString());
				}
	    		
	    	});
	    	final View deleteCustomType = view.findViewById(R.id.delete_custom_type);
	    	deleteCustomType.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//reallyDeleteButton.setVisibility(View.VISIBLE);
					//deleteCustomType.setVisibility(View.GONE);
					String typeKey = results.get(index).get("typeKey").toString();
					deleteType(typeKey);
				}
	    		
	    	});
	    		        
	        return view;
	    }
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.add_type:
				showTypeDialog(null, null);
				break;
			case R.id.type_back:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
		}
	}

}
