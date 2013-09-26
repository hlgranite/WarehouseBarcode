package com.hlgranite.warehousescanner;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ManualActivity extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // TODO: Make dateFormat as global variable
    protected EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(checkoutClick);

        // set today date as default
        editText = (EditText)findViewById(R.id.editText);
        editText.setText(dateFormat.format(calendar.getTime()));

        // show datepicker when click on date editText
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManualActivity.this, dateDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        EditText editText4 = (EditText)findViewById(R.id.editText4);
        editText4.setText("1");

        // set focus on barcode entry when popup
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        editText2.requestFocus();
    }

    protected DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        }
    };

    protected View.OnClickListener checkoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Set value into work order
            Log.i("INFO", "Checkout!");

            try {
                EditText editText2 = (EditText)findViewById(R.id.editText2);
                Barcode barcode = new Barcode(editText2.getText().toString());
                WorkOrder order = new WorkOrder(barcode, dateFormat.parse(editText.getText().toString()), "ONE", "TEST"+new Random().nextInt(9999));
                FusionManager.getInstance().checkout(order);
                setResult(Activity.RESULT_OK);
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage());
            }
            ManualActivity.this.finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manual, menu);
        return true;
    }

    private class RetrieveCustomer extends AsyncTask<String, Void, ArrayList<Customer>> {

        @Override
        protected ArrayList<Customer> doInBackground(String... params) {
            return FusionManager.getInstance().getCustomers();
        }

        @Override
        protected void onPostExecute(ArrayList<Customer> customers) {
            super.onPostExecute(customers);
            // TODO: Bind into customer spinner
            //ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(this, R.id.spinner, customers);

        }
    }
    
}