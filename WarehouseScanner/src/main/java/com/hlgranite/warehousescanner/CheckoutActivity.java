package com.hlgranite.warehousescanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.Random;

public class CheckoutActivity extends Activity {

    private FusionManager fusionManager = null;

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
        }
    };


    protected View.OnClickListener manualClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int width = 0;
            while( (width = generateNumber()) < 1000) {
                width = generateNumber();
            }
            int length = 0;
            while( (length = generateNumber()) < 1000) {
                length = generateNumber();
            }
            Barcode barcode = new Barcode("BLUE"+width+length+"B13P");
            WorkOrder order = new WorkOrder(barcode, new Date(), "ONE", "TEST"+new Random().nextInt(9999));
            fusionManager.checkout(order);
        }
    };

    private int generateNumber() {
        return new Random().nextInt(9999);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout, menu);
        return true;
    }
    
}
