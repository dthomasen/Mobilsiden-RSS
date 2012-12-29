package dk.whooper.mobilsiden.screens;

import dk.whooper.mobilsiden.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {    
	    super.onCreate(savedInstanceState);       
	    addPreferencesFromResource(R.xml.preferences);      
	}

}
