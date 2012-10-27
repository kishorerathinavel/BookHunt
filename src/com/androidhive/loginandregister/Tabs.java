/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidhive.loginandregister;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * An example of tab content that launches an activity via {@link android.widget.TabHost.TabSpec#setContent(android.content.Intent)}
 */
public class Tabs extends TabActivity {
    public static TabHost tabHost;
    public static int count = 0;
    
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = preferences.edit();
		String Rollno = preferences.getString("RollNo",null);
		String Server = preferences.getString("Server", null);
		boolean firstTime = preferences.getBoolean("FirstTime", true);
		boolean SecondTime = preferences.getBoolean("SecondTime", true);
		boolean registered = preferences.getBoolean("registered", true);
		edit.putString("RollNo", Rollno);
		edit.putString("Server", "10.2.2.125");
		edit.commit();

		if(firstTime || registered)
		{
			Editor edit1 = preferences.edit();
        	edit1.putBoolean("FirstTime", false);
        	edit1.commit();
        	Intent i = new Intent(Tabs.this,LoginActivity.class);
        	startActivity(i);
        	this.finish();
		}
		else
		{
			setContentView(R.layout.tabs1);
			tabHost = getTabHost();

	        // Tab for Borrow
	        TabSpec borrow = tabHost.newTabSpec("Borrow");
	        borrow.setIndicator("Borrow", getResources().getDrawable(R.drawable.borrow));
	        Intent borrowIntent = new Intent(this, Borrow.class);
	        borrow.setContent(borrowIntent);
	        
	        // Tab for Market	
	        TabSpec market = tabHost.newTabSpec("Market");
	        market.setIndicator("Market", getResources().getDrawable(android.R.drawable.ic_menu_share));
	        Intent marketIntent = new Intent(this, MarketView.class);
	        market.setContent(marketIntent);

	        // Tab for Lend
	        TabSpec lend = tabHost.newTabSpec("Lend");
	        // setting Title and Icon for the Tab
	        lend.setIndicator("Lend", getResources().getDrawable(R.drawable.lend));
	        Intent lendIntent = new Intent(this, Lend.class);
	        lend.setContent(lendIntent);
	        
	       
	        // Adding all TabSpec to TabHost
	        
	        tabHost.addTab(borrow); // Adding borrow
	        tabHost.addTab(market); // Adding market
	        tabHost.addTab(lend); // Adding lend
	        
	        //tabHost.setCurrentTab(0);
		}
		
        
        

     }
    public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.menusettings:
        	Intent newIntent = new Intent (this,PrefsActivity.class);
    		startActivity(newIntent);
    		return true;
		}
		return true;
    }
}
