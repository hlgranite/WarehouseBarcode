package com.hlgranite.warehousescanner;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CheckoutActivity extends Activity {

    private final int MANUAL_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(scanClick);

        Button manual = (Button)findViewById(R.id.manual);
        manual.setOnClickListener(manualClick);
    }

    protected View.OnClickListener scanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Launch camera for scan barcode
            Log.i("INFO", "Launch camera for scan barcode ");

            // see http://code.google.com/p/zxing/wiki/ScanningViaIntent
            IntentIntegrator integrator = new IntentIntegrator(CheckoutActivity.this);
            integrator.initiateScan();
        }
    };


    protected View.OnClickListener manualClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(CheckoutActivity.this, ManualActivity.class);
            startActivityForResult(i, MANUAL_ACTIVITY);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("INFO", "Request code: " + requestCode);

        switch(requestCode) {
            case MANUAL_ACTIVITY:
                Log.i("INFO", "Result code: "+resultCode);
                if(resultCode == Activity.RESULT_OK) {
                    // TODO: Not true, this must really return from FusionManager.checkout result
                    Toast.makeText(getApplicationContext(), "Checkout successfully!", Toast.LENGTH_LONG).show();

                    // HACK: clear all history force rebind
                    TabActivity parent = (TabActivity)getParent();
                    String tagName = getResources().getString(R.string.history);
                    Activity historyActivity = parent.getLocalActivityManager().getActivity(tagName);
                    ListView listViewH = (ListView)historyActivity.findViewById(R.id.listView);
                    if(listViewH != null && listViewH.getAdapter() != null) {
                        WorkOrderAdapter existing = (WorkOrderAdapter)listViewH.getAdapter();
                        existing.clear();
                        existing.notifyDataSetChanged();
                    }
                }
                break;
            //case SCAN_ACTIVITY:
            default:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(scanResult != null) {
                    String barcode = scanResult.getContents();
                    Log.i("INFO", "Capture barcode: " + barcode);
                    Intent i = new Intent(CheckoutActivity.this, ManualActivity.class);
                    i.putExtra("barcode", barcode);
                    startActivityForResult(i, MANUAL_ACTIVITY);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout, menu);
        return true;
    }

}
