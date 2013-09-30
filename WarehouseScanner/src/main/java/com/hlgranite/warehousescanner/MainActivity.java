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

        FusionManager.getInstance().setApi(getResources().getString(R.string.api));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String unit = sharedPreferences.getString("prefUnit", Unit.Meter);
        FusionManager.getInstance().setUnit(unit);

        String email = sharedPreferences.getString("prefUsername", "NULL");
        String password = sharedPreferences.getString("prefPassword", "NULL");
        Log.i("INFO", "Email: "+email);
        Log.i("INFO", "Unit: "+unit);
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
                menu.clear();
                inflater.inflate(R.menu.checkout, menu);
                break;
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
            case R.id.action_refresh_history:
                Log.i("INFO", "Reset history");
                FusionManager.getInstance().reset();
                final ListView listViewH = (ListView)getTabHost().getCurrentView().findViewById(R.id.listView);
                if(listViewH.getAdapter() != null) {
                    WorkOrderAdapter existing = (WorkOrderAdapter)listViewH.getAdapter();
                    existing.clear();
                }
                break;
            case R.id.action_refresh_balance:
                Log.i("INFO", "Reset Fusion table");
                FusionManager.getInstance().reset();
                final ListView listView0 = (ListView)getTabHost().getCurrentView().findViewById(R.id.listView);
                if(listView0.getAdapter() != null) {
                    InventoryAdapter existing = (InventoryAdapter)listView0.getAdapter();
                    existing.clear();
                }
                break;
            case R.id.action_sort_area:
                Log.i("INFO", "Sort by area");
                final ListView listView = (ListView)getTabHost().getCurrentView().findViewById(R.id.listView);
                InventoryAdapter adapter = (InventoryAdapter)listView.getAdapter();
                adapter.sort(new Comparator<Stock>() {
                    @Override
                    public int compare(Stock lhs, Stock rhs) {
                        lhs.getBalance();
                        rhs.getBalance();
                        Long area1 = lhs.getArea().getValue();
                        Long area2 = rhs.getArea().getValue();
                        return area1.compareTo(area2);
                    }
                });
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_sort_quantity:
                Log.i("INFO", "Sort by quantity");
                final ListView listView2 = (ListView)getTabHost().getCurrentView().findViewById(R.id.listView);
                InventoryAdapter adapter2 = (InventoryAdapter)listView2.getAdapter();
                adapter2.sort(new Comparator<Stock>() {
                    @Override
                    public int compare(Stock lhs, Stock rhs) {
                        return lhs.getBalance().compareTo(rhs.getBalance());
                    }
                });
                adapter2.notifyDataSetChanged();
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
        Log.i("INFO", "MainActiviy.onActiviyResult");

        switch(requestCode) {
            case RESULT_SETTINGS:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String unit = sharedPreferences.getString("prefUnit", Unit.Meter);
                FusionManager.getInstance().setUnit(unit);
                if(!FusionManager.getInstance().getAuthenticate()) {
                    String email = sharedPreferences.getString("prefUsername", "NULL");
                    String password = sharedPreferences.getString("prefPassword", "NULL");
                    Log.i("INFO", "Email: "+email);
                    Log.i("INFO", "Unit: "+unit);
                    new Authenticate().execute(email, password);
                }
                break;
        }
    }

    private class Authenticate extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            FusionManager.getInstance().authenticate(params[0], params[1]);
            return null;
        }
    }
}
