package com.hlgranite.warehousescanner;

import android.content.Intent;
import android.os.Bundle;
import android.app.TabActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Main entrance for TabHost.
 * TODO: Change to fragment layout.
 * http://stackoverflow.com/questions/17687717/tutorial-to-implement-the-use-of-tabhost-in-android-2-2-viewpager-and-fragment
 */
public class MainActivity extends TabActivity {

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

        FusionManager manager = new FusionManager(getResources().getString(R.string.api));
        // TODO: Authenticate user account to get auth token
        //FusionManager.authenticate(getResources().getString(R.string.email), getResources().getString(R.string.password));
    }
}
