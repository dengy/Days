/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inde.shiningdays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.inde.shiningdays.util.SharedPrefsUtil;
import com.inde.shiningdays.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View mDrawerLeft;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTypes;
    private View mEditType;
    private String currentType;
    private int currentTypePosition;
    private ArrayList<Integer> rateCountList = new ArrayList<Integer>(Arrays.asList(30, 60, 90, 120, 150, 180, 210, 250, 300));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //umeng sdk
        MobclickAgent.updateOnlineConfig(this);

        mTitle = mDrawerTitle = getTitle();
        initViews();
                
        //start update widget service
        startUpdateWidgetService();
        
        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.type_list_footer, null, false);
        mEditType = footerView.findViewById(R.id.edit_type);
         //edit type button click
         mEditType.setOnClickListener(new OnClickListener() {

 			@Override
 			public void onClick(View v) {
 				Intent intent = new Intent(v.getContext(), TypeActivity.class);
 				startActivity(intent);
 				finish();
 			}
         	
         });
        mDrawerList.addFooterView(footerView);
        mDrawerList.setAdapter(getSimpleAdapter());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer_used,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
       
        //enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectItem(0, CountDown.DEFAULT_SORT_ORDER);
        }
        //init drawer list
        initDrawerList();
        //show rate dialog
        showRateDialog();
    }
    
    private void firstOpenAppDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    final View layout = inflater.inflate(R.layout.first_open_app_dialog, null);
	    
	    builder.setView(layout).setPositiveButton(R.string.confirm_ok_label, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
	    });
	    
	    Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}
    
    /**
     * whether to open the rate dialog
     */
    private void showRateDialog() {
    	SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(this, Constant.COUNT_DOWN_SETTING_PREF);
    	int appOpenedCount = prefs.getInt(Constant.APP_OPENED_COUNT, 1);
    	boolean isOpenRateDialog = prefs.getBoolean(Constant.IS_OPEN_RATE_DIALOG, true);
    	if(isOpenRateDialog) {
    		if(rateCountList.contains(appOpenedCount)) {
    			openRateDialog();
    		}
    		
    		//update the appOpenedCount
    		if(appOpenedCount < 100000) {
    			appOpenedCount++;
        		Editor editor = prefs.edit();
        		editor.putInt(Constant.APP_OPENED_COUNT, appOpenedCount);
        		editor.commit();
    		}
    	}
    }
    
    private void openRateDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    final View layout = inflater.inflate(R.layout.rate_dialog, null);
	    builder.setView(layout);
	    final Dialog dialog = builder.create();

	    //rate right now 
	    layout.findViewById(R.id.rate_right_now).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//open the market list
				Intent goToMarket = new Intent(Intent.ACTION_VIEW);
				goToMarket.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity(goToMarket); 
				//modify the isOpenRateDialog = false
				SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(v.getContext(), 
						Constant.COUNT_DOWN_SETTING_PREF);
				Editor editor = prefs.edit();
				editor.putBoolean(Constant.IS_OPEN_RATE_DIALOG, false);
				editor.commit();
				//dismiss dialog
				dialog.dismiss();
			}
	    });
	    //rate later
	    layout.findViewById(R.id.rate_later).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    //rate never
	    layout.findViewById(R.id.rate_never).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//modify the isOpenRateDialog = false
				SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(v.getContext(), 
						Constant.COUNT_DOWN_SETTING_PREF);
				Editor editor = prefs.edit();
				editor.putBoolean(Constant.IS_OPEN_RATE_DIALOG, false);
				editor.commit();
				//dismiss dialog
				dialog.dismiss();
			}
	    });
	    
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.show();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    
    private void startUpdateWidgetService() {
    	Intent i = new Intent();
		i.setClass(this, UpdateWidgetService.class);
		startService(i);
    }
    
    private void initDrawerList() {
    	SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs(this, Constant.COUNT_DOWN_SETTING_PREF);
    	boolean isFirstOpenApp = prefs.getBoolean(Constant.IS_FIRST_OPEN_APP, true);
    	if(isFirstOpenApp) {
    		mDrawerLayout.openDrawer(mDrawerLeft);
    		
    		firstOpenAppDialog();
    	} 
//    	else {
//    		Bundle bundle = this.getIntent().getExtras();
//    		if(bundle != null) {
//    			boolean isOpen = bundle.getBoolean(Constant.IS_OPEN_DRAWER_LIST);
//    			if(isOpen) {
//    				mDrawerLayout.openDrawer(mDrawerLeft);
//    			}
//    		}
//    	}
    	
    }
    
    private SimpleAdapter getSimpleAdapter() {
    	List<Map<String, Object>> data = TypeActivity.getDefaultData(this, true);
    	data.addAll(TypeActivity.getCustomData(this, true));
    	SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.drawer_list_item,
				new String[] {"icon", "type"}, new int[] {R.id.type_icon, R.id.type_item});
    	return adapter;
    }
    
    private void initViews() {
    	 mTypes = Utils.getAllTypes(this);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerList = (ListView) findViewById(R.id.drawer_list);
         mDrawerLeft = findViewById(R.id.drawerLeft);
        // mEditType = findViewById(R.id.edit_type);
         
         //edit type button click
//         mEditType.setOnClickListener(new OnClickListener() {
//
// 			@Override
// 			public void onClick(View v) {
// 				Intent intent = new Intent(v.getContext(), TypeActivity.class);
// 				startActivity(intent);
// 				finish();
// 			}
//         	
//         });

         //pull back button click
         findViewById(R.id.pull_back_click).setOnClickListener(new OnClickListener() {
  			@Override
  			public void onClick(View v) {
  				mDrawerLayout.closeDrawer(mDrawerLeft);
  			}
          	
          });
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLeft);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        Intent intent;
        switch(item.getItemId()) {
        case R.id.action_add:
            intent = new Intent(Intent.ACTION_INSERT,getIntent().getData());
            intent.putExtra(CountDown.PRIORITY, currentType);
			startActivity(intent);
            return true;
        case R.id.action_menumore:
            showPopupMenu();
        	//intent = new Intent(this, MenuMore.class);
			//startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private  void showPopupMenu() {
        final View menuItemView = findViewById(R.id.action_menumore); // SAME ID AS MENU ID
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.more_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.sort_end_date_asc:
                        selectItem(currentTypePosition, CountDown.SORT_END_DATE_ASC);
                        return true;
                    case R.id.sort_update_date_desc:
                        selectItem(currentTypePosition, CountDown.DEFAULT_SORT_ORDER);
                        return true;
                    case R.id.action_setting:
                        Intent intent = new Intent(menuItemView.getContext(), MenuMore.class);
                        startActivity(intent);
                    default:
                        return true;
                }
            }
        });
        popupMenu.show();
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position, CountDown.DEFAULT_SORT_ORDER);
        }
    }

    private void selectItem(int position, String orderBy) {
        // update the main content by replacing fragments
        //Fragment fragment = new PlanetFragment();
//    	Intent intent = this.getIntent();
//    	Bundle extras = intent.getExtras();
//    	if(extras != null) {
//    		currentType = extras.getString(CountDown.PRIORITY);
//    	} else {
//    		currentType = mTypes[position];
//    	}
    	if(position == mTypes.length) {
    		return;
    	}

        currentTypePosition = position;
    	currentType = mTypes[position];
    	Fragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        args.putString(CountDown.PRIORITY, currentType);
        args.putString(CountDown.ORDER_BY, orderBy);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        StringBuilder title = new StringBuilder();
        title.append(getResources().getString(R.string.app_name)).append(".").append(currentType);
        setTitle(title.toString());
        mDrawerLayout.closeDrawer(mDrawerLeft);
        
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}