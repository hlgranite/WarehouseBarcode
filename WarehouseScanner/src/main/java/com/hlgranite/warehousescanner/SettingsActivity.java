package com.hlgranite.warehousescanner;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by yeang-shing.then on 9/24/13.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}