package android.first.countdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.first.countdown.util.SharedPrefsUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TypeActivity extends Activity {
	private ListView defaultTypeList;
	private ListView customTypeList;
	private View mEditType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_type);
		initViews();
		
	}
	
	private void initViews() {
		defaultTypeList = (ListView) this.findViewById(R.id.default_type_list);
		customTypeList = (ListView) this.findViewById(R.id.custom_type_list);
		mEditType = this.findViewById(R.id.add_type);
		
		
		SimpleAdapter defaultAdapter = new SimpleAdapter(this, getDefaultData(), R.layout.default_type_list_item,
				new String[] {"icon", "type"}, new int[] {R.id.default_type_icon, R.id.default_type});
		defaultTypeList.setAdapter(defaultAdapter);
		loadCustomTypeList(false);
		mEditType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(v.getContext(), Constant.CUSTOM_TYPE_LIST_PREF);
				int index = prefs.getAll().size();
				Editor editor = prefs.edit();
				editor.putString((++index) + "", "ÐÂ·ÖÀà");
				editor.commit();
				//SimpleAdapter adapter = (SimpleAdapter)customTypeList.getAdapter();
				//adapter.notifyDataSetChanged();
				//customTypeList.setAdapter(adapter);
				loadCustomTypeList(true);
			}
			
		});
	}
	private void loadCustomTypeList(boolean isFocus) {
		CustomTypeAdapter customAdapter = new CustomTypeAdapter(this, getCustomData(), R.layout.custom_type_list_item,
				new String[] {"icon", "type"}, new int[] {R.id.delete_custom_type, R.id.edit_custom_type}, isFocus);
		customTypeList.setAdapter(customAdapter);
	}
	private List<Map<String, Object>> getDefaultData() {
		String[] mTypes = SharedPrefsUtil.getDefaultTypes(this);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", mTypes[0]);
        map.put("icon", R.drawable.alarm);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[1]);
        map.put("icon", R.drawable.alarm);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("type", mTypes[2]);
        map.put("icon", R.drawable.alarm);
        list.add(map);
        return list;
	}
	
	private List<Map<String, Object>> getCustomData() {
		String[] customTypes = SharedPrefsUtil.getCustomTypes(this);
		int index = customTypes.length;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < index; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("typeKey", (i+1)+"");
	        map.put("type", customTypes[i]);
	        map.put("icon", R.drawable.delete_type);
	        list.add(map);			 
		}
        return list;
	}
	
	class CustomTypeAdapter extends SimpleAdapter {

	    private List<Map<String, Object>> results;
	    private boolean isFocus;

	    public CustomTypeAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to, boolean isFocus) {
	        super(context, data, resource, from, to);
	        this.results = data;
	        this.isFocus = isFocus;
	    }

	    public View getView(int position, View convertView, ViewGroup parent){
	    	final int index = position;
	    	View view = super.getView(position, convertView, parent);
	    	final EditText editCustomType = (EditText)view.findViewById(R.id.edit_custom_type);
	    	final TextView reallyDeleteButton = (TextView)view.findViewById(R.id.really_delete_button);
	    	if(isFocus && (position == (results.size() -1))) {
	    		editCustomType.requestFocus();
	    	}
	    	Button clearCustomType = (Button)view.findViewById(R.id.clear_custom_type);
	    	clearCustomType.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editCustomType.setText("");
					editCustomType.requestFocus();
				}
	    		
	    	});
	    	final View deleteCustomType = view.findViewById(R.id.delete_custom_type);
	    	deleteCustomType.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//reallyDeleteButton.setVisibility(View.VISIBLE);
					//deleteCustomType.setVisibility(View.GONE);
					String typeKey = results.get(index).get("typeKey").toString();
					SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(v.getContext(), Constant.CUSTOM_TYPE_LIST_PREF);
					Editor editor = prefs.edit();
					editor.remove(typeKey);
					editor.commit();
					loadCustomTypeList(false);
				}
	    		
	    	});
	    	
	    	editCustomType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus) {
						//lose focus then save the text
						String text = editCustomType.getText().toString();
						if(text == null || "".equals(text)) {
							Toast.makeText(v.getContext(), R.string.type_not_empty, 30);
						} else {
							String typeKey = results.get(index).get("typeKey").toString();
							SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(v.getContext(), Constant.CUSTOM_TYPE_LIST_PREF);
							Editor editor = prefs.edit();
							editor.putString(typeKey, editCustomType.getText().toString());
							editor.commit();
						}
					}
					
				}
			});
	    		        
	        return view;
	    }
	}

}
