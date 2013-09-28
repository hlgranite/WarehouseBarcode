package com.hlgranite.warehousescanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ManualActivity extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // TODO: Make dateFormat as global variable
    private EditText editText;
    private Spinner spinner;
    private String customer;

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

        // bind customer into dropdownlist
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customer = (String)parent.getItemAtPosition(position);
                Log.i("INFO", customer + " selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        new RetrieveCustomer(this).execute();
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

    protected Button.OnClickListener checkoutClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Set value into work order
            Log.i("INFO", "Checkout!");

            try {
                EditText editText2 = (EditText)findViewById(R.id.editText2);
                EditText editText3 = (EditText)findViewById(R.id.editText3);
                EditText editText4 = (EditText)findViewById(R.id.editText4);

                // get date value and tight to time as well.
                Date now = new Date();
                Date date = dateFormat.parse(editText.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR_OF_DAY, now.getHours());
                cal.add(Calendar.MINUTE, now.getMinutes());
                cal.add(Calendar.SECOND, now.getSeconds());

                Barcode barcode = new Barcode(editText2.getText().toString());
                WorkOrder order = new WorkOrder(barcode, cal.getTime(), customer, editText3.getText().toString());
                FusionManager.getInstance().checkout(order, Integer.parseInt(editText4.getText().toString()));

                setResult(Activity.RESULT_OK);
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage());
            }
            ManualActivity.this.finish();
        }
    };

// disable menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.manual, menu);
//        return true;
//    }

    private class RetrieveCustomer extends AsyncTask<String, Void, ArrayList<String>> {
        private Context context;

        public RetrieveCustomer(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            return FusionManager.getInstance().getCustomers();
        }

        @Override
        protected void onPostExecute(ArrayList<String> customers) {
            super.onPostExecute(customers);
            // Bind into customer spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManualActivity.this, android.R.layout.simple_spinner_dropdown_item, customers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
    
}