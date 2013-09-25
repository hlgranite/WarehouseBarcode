package com.hlgranite.warehousescanner;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.Random;

public class ManualActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(checkoutClick);
    }

    protected View.OnClickListener checkoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Set value into work order
            Log.i("INFO", "Checkout!");

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
            FusionManager.getInstance().checkout(order);
            ManualActivity.this.finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manual, menu);
        return true;
    }

    private int generateNumber() {
        return new Random().nextInt(9999);
    }
    
}