package com.hlgranite.warehousescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

public class CheckoutActivity extends Activity {

    private FusionManager fusionManager = null;
    private final int MANUAL_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(scanClick);

        Button manual = (Button)findViewById(R.id.manual);
        manual.setOnClickListener(manualClick);

        fusionManager = FusionManager.getInstance();
    }

    protected View.OnClickListener scanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Launch camera for scan barcode
            Log.i("INFO", "Launch camera for scan barcode");
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

        switch(requestCode) {
            case MANUAL_ACTIVITY:
                Toast.makeText(getApplicationContext(), "Checkout successfully!", Toast.LENGTH_LONG).show();
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
