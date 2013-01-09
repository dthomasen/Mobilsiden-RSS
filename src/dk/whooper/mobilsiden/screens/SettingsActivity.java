package dk.whooper.mobilsiden.screens;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import dk.whooper.mobilsiden.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //add the prefernces.xml layout
        addPreferencesFromResource(R.xml.preferences);
        getActionBar().setDisplayShowTitleEnabled(false);

        //get the specified preferences using the key declared in preferences.xml
        ListPreference userAgentPref = (ListPreference) findPreference("user_agent");

        //get the description from the selected item
        userAgentPref.setSummary(userAgentPref.getEntry());

        //when the user choose other item the description changes too with the selected item
        userAgentPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                return true;
            }
        });

    }


}
