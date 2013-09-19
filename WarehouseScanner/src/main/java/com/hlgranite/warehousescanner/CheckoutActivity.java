package com.hlgranite.warehousescanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class CheckoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button manual = (Button)findViewById(R.id.manual);
        manual.setOnClickListener(manualClick);
    }

    protected View.OnClickListener manualClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Barcode barcode = new Barcode("BLUE12001600B13P", new Date(), "ONE", "XDDT346346");
            FusionManager.checkout(barcode);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout, menu);
        return true;
    }
    
}
