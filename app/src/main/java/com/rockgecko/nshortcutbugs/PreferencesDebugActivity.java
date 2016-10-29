package com.rockgecko.nshortcutbugs;

import android.os.Bundle;
import android.preference.PreferenceActivity;



public class PreferencesDebugActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {	    	   
	    super.onCreate(savedInstanceState);
		//Yes, it's deprecated, but it's so much easier to load simple preferences
		//like this, without fragments
		addPreferencesFromResource(R.xml.preferences);
	}


}
