package com.hlgranite.warehousescanner;

import android.content.Intent;
import android.os.Bundle;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        // Checkout tab
        TabSpec scanSpec = tabHost.newTabSpec("CHECKOUT");
        scanSpec.setIndicator("CHECKOUT");
        Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
        scanSpec.setContent(checkoutIntent);

        // History tab
        TabSpec historySpec = tabHost.newTabSpec("HISTORY");
        historySpec.setIndicator("HISTORY");
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        historySpec.setContent(historyIntent);

        // Balance tab
        TabSpec balanceSpec = tabHost.newTabSpec("BALANCE");
        balanceSpec.setIndicator("BALANCE");
        Intent balanceIntent = new Intent(this, BalanceActivity.class);
        balanceSpec.setContent(balanceIntent);

        tabHost.addTab(scanSpec);
        tabHost.addTab(historySpec);
        tabHost.addTab(balanceSpec);
    }
    
}
