package com.hlgranite.warehousescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeang-shing.then on 9/24/13.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        try {
            // display value in setting if any
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String email = sharedPreferences.getString("prefUsername", "");
            if(!email.isEmpty()) {
                EditTextPreference prefUsername = (EditTextPreference)findPreference("prefUsername");
                prefUsername.setSummary(email);
            }
            String password = sharedPreferences.getString("prefPassword", "");
            if(!password.isEmpty()) {
                EditTextPreference prefPassword = (EditTextPreference)findPreference("prefPassword");
                prefPassword.setSummary(toAsterisk(prefPassword.getText()));
            }

            // display unit
            ListPreference listPreference = (ListPreference)findPreference("prefUnit");
            String unit = sharedPreferences.getString("prefUnit", Unit.Meter);
            listPreference.setSummary(getUnit(unit));

            // display danger info
            String area = sharedPreferences.getString("prefDangerArea", "0");
            EditTextPreference prefDangerArea = (EditTextPreference)findPreference("prefDangerArea");
            prefDangerArea.setSummary(area + Unit.Meter + Area.SQUARE);

            String qty = sharedPreferences.getString("prefDangerQuantity", "0");
            EditTextPreference prefDangerQuantity = (EditTextPreference)findPreference("prefDangerQuantity");
            prefDangerQuantity.setSummary(qty + Unit.Piece);

            // set version
            Preference prefVersion = findPreference("prefVersion");
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            prefVersion.setSummary(version);

            Preference prefEmail = findPreference("prefEmail");
            prefEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.contact_email)});
                    intent.setType("message/rfc822");//"application/octet-stream");
                    startActivity(Intent.createChooser(intent, "Sending email"));
                    return false;
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref instanceof EditTextPreference) {
            EditTextPreference editText = (EditTextPreference)pref;
            if(editText.getKey().toLowerCase().contains("password")) {
                editText.setSummary(toAsterisk(editText.getText()));
            } else {
                String summary = editText.getText();
                if(editText.getKey().equals("prefDangerArea")) {
                    summary += Unit.Meter + Area.SQUARE;
                } else if(editText.getKey().equals("prefDangerQuantity")) {
                    summary += Unit.Piece;
                }
                editText.setSummary(summary);
            }
        } else if(pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference)pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    /**
     * Replace all string into asterisk for hiding purpose.
     * @param value
     * @return
     */
    private String toAsterisk(String value) {
        String output = "";
        for(char c: value.toCharArray()) {
            output += "*";
        }
        return output;
    }

    private HashMap<String, String> getUnitOptions() {
        HashMap<String, String> output = new HashMap<String, String>();
        String[] keys = getResources().getStringArray(R.array.unitOptions);
        String[] values = getResources().getStringArray(R.array.unitValues);
        HashMap<String, String> unitOptions = new HashMap<String, String>();
        for(int i=0;i<keys.length;i++) {
            output.put(keys[i], values[i]);
        }
        return output;
    }

    /**
     * Return unit display name by provided value.
     * @param key
     * @return
     */
    private String getUnit(String key) {
        HashMap<String, String> unitOptions = getUnitOptions();
        for(Map.Entry<String, String> entry : unitOptions.entrySet()) {
            if(entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }

        return "";
    }
}