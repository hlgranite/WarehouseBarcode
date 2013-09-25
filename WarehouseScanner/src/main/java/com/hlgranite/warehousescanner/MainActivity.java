package com.hlgranite.warehousescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.TabActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Main entrance for TabHost.
 * TODO: Change to fragment layout.
 * http://stackoverflow.com/questions/17687717/tutorial-to-implement-the-use-of-tabhost-in-android-2-2-viewpager-and-fragment
 */
public class MainActivity extends TabActivity {

    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "MainActivity onCreate");
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        // Checkout tab
        String tabName = getResources().getString(R.string.checkout);
        TabSpec scanSpec = tabHost.newTabSpec(tabName);
        scanSpec.setIndicator(tabName);
        Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
        scanSpec.setContent(checkoutIntent);

        // History tab
        tabName = getResources().getString(R.string.history);
        TabSpec historySpec = tabHost.newTabSpec(tabName);
        historySpec.setIndicator(tabName);
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        historySpec.setContent(historyIntent);

        // Balance tab
        tabName = getResources().getString(R.string.balance);
        TabSpec balanceSpec = tabHost.newTabSpec(tabName);
        balanceSpec.setIndicator(tabName);
        Intent balanceIntent = new Intent(this, BalanceActivity.class);
        balanceSpec.setContent(balanceIntent);

        tabHost.addTab(scanSpec);
        tabHost.addTab(historySpec);
        tabHost.addTab(balanceSpec);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("INFO", "MainActivity onStart");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPreferences.getString("prefUsername", "NULL");
        String password = sharedPreferences.getString("prefPassword", "NULL");
        Log.i("INFO", "Email: "+email+" Password: "+password);
        if(!email.isEmpty() && !password.isEmpty()) {
            new Authenticate().execute(email, password);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
//            case R.id.action_refresh:
//                Log.i("INFO", "Reset Fusion table");
//                FusionManager.getInstance().reset();
//                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String email = sharedPreferences.getString("prefUsername", "NULL");
//        String password = sharedPreferences.getString("prefPassword", "NULL");
//        Log.i("INFO", "Email: "+email+" Password: "+password);
        switch(requestCode) {
            case RESULT_SETTINGS:
                //new Authenticate().execute(email, password);
                break;
        }
    }

    private class Authenticate extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Setup a singleton datastore object
            FusionManager fusionManager = FusionManager.getInstance();
            fusionManager.setApi(getResources().getString(R.string.api));
            fusionManager.authenticate(params[0], params[1]);
            return null;
        }
    }
}
