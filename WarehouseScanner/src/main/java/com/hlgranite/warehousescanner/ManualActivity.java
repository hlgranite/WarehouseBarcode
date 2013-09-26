package com.hlgranite.warehousescanner;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class ManualActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(checkoutClick);

        //EditText editText = (EditText)findViewById(R.id.editText);
        // TODO: editText.setText(new Date().toString());

        EditText editText4 = (EditText)findViewById(R.id.editText4);
        editText4.setText("1");
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

            EditText editText2 = (EditText)findViewById(R.id.editText2);

            Barcode barcode = new Barcode(editText2.getText().toString());
            WorkOrder order = new WorkOrder(barcode, new Date(), "ONE", "TEST"+new Random().nextInt(9999));
            FusionManager.getInstance().checkout(order);

            setResult(Activity.RESULT_OK);
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