package com.androidhive.loginandregister;

import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class PrefsActivity extends PreferenceActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.prefs);
                setTheme(android.R.style.Animation_Translucent);
                //findViewById(android.R.id.list).setBackgroundColor(Color.WHITE);
        }
}