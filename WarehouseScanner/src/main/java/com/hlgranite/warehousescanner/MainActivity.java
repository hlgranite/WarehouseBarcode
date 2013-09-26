package com.hlgranite.warehousescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.TabActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.util.Comparator;

/**
 * Main entrance for TabHost.
 * TODO: Change to fragment layout.
 * http://stackoverflow.com/questions/17687717/tutorial-to-implement-the-use-of-tabhost-in-android-2-2-viewpager-and-fragment
 */
public class MainActivity extends TabActivity {

    public static final int RESULT_SETTINGS = 100;

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
        Log.i("INFO", "Email: "+email);//+" Password: "+password);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        int currentTab = getTabHost().getCurrentTab();
        switch(currentTab) {
            case 0:
//                menu.clear();
//                inflater.inflate(R.menu.main, menu);
//                break;
            case 1:
                menu.clear();
                inflater.inflate(R.menu.history, menu);
                break;
            case 2:
                menu.clear();
                inflater.inflate(R.menu.balance, menu);
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
//            case R.id.action_refresh:
//                Log.i("INFO", "Reset Fusion table");
//                FusionManager.getInstance().reset();
//                break;
            case R.id.action_sort_quantity:
                final ListView listView = (ListView)getTabHost().getCurrentView().findViewById(R.id.listView);//(ListView)findViewById(R.id.listView);
                InventoryArrayAdapter adapter = (InventoryArrayAdapter)listView.getAdapter();
                Log.i("INFO", "Sort stock by quantity");
                adapter.sort(new Comparator<Stock>() {
                    @Override
                    public int compare(Stock lhs, Stock rhs) {
                        return lhs.getBalance().compareTo(rhs.getBalance());
                    }
                });
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_sort_size:
                // TODO: Sort by dimension
                break;
            case R.id.action_settings:
                Log.i("INFO", "MainActivity.setting");
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
