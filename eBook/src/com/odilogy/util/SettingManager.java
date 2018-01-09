package com.odilogy.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingManager {
	// Shared Preferences
    SharedPreferences pref;
 
    // Editor for Shared preferences
    Editor editor;
 
    // Context
    Context _context;
 
    // Shared pref mode
    int PRIVATE_MODE = 0;
    public static final int LATEST = 0;
    public static final int CATEGORY = 1;
    public static final int LIBRARY = 2;
    public static final int SEARCH = 3;
    public static final Integer CATEGORY_SEARCH = 4;
 
    // Sharedpref file name
    private static final String PREF_NAME = "ebookstoreprefs";
 
    // All Shared Preferences Keys
    public static final String CURRENT_TAB = "current_tab";
    public static final String IS_FIRST_TIME = "is_first_time";
        
    public static final String DEVICE_ID = "prefDeviceId";
 
    // 
 
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

	private static final int MAX_LENGTH = 8;
 
    // Constructor
    public SettingManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        //pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();
    }
    
    /**
     * set flag for first time user launch app
     * */
    public void setFirstTime(){
    	editor.putBoolean(IS_FIRST_TIME, false);
    	editor.commit();
    }
    
    /**
     * get configuration is this first time user launch this app
     * */
    public boolean isFirstTime(){
    	boolean pertama = pref.getBoolean(IS_FIRST_TIME, true);
    	return pertama;
    }
    
    
    /**
     * set unique user id
     * */
    public void setPrefDeviceId(String dev_id){
    	editor.putString(DEVICE_ID, dev_id);
    	editor.commit();
    }
    
    
    
    /**
     * get current category
     * */
    public int getCurrentTab(){
    	int currentCat = pref.getInt(CURRENT_TAB, 0);
    	return currentCat;
    }
    
    public void setCurrentTab(int tab){
    	editor.putInt(CURRENT_TAB, tab);
    	editor.commit();
    }
}
