package com.hlgranite.warehousescanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManualActivity extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private EditText editText;
    private Spinner spinner;
    private String customer;
    private Barcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        String barcodeString = getIntent().getStringExtra("barcode");
        Log.i("INFO", "Barcode: " + barcodeString);
        if(barcodeString != null && !barcodeString.isEmpty()) {
            barcode = new Barcode(barcodeString);
        }

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(checkoutClick);

        // set today date as default
        editText = (EditText)findViewById(R.id.editText);
        editText.setText(Unit.DateFormatter.format(calendar.getTime()));

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
        if(barcode != null) {
            editText2.setText(barcode.getNumber());
            editText2.setEnabled(false);
            new RetrieveStock(this).execute(barcode.getStockCode());
        } else {
            editText2.requestFocus();
            new RetrieveCustomer(this).execute();
        }

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
    }

    protected DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(Unit.DateFormatter.format(calendar.getTime()));
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

                // get date value and tight to current time.
                Date now = new Date();
                Date date = Unit.DateFormatter.parse(editText.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR_OF_DAY, now.getHours());
                cal.add(Calendar.MINUTE, now.getMinutes());
                cal.add(Calendar.SECOND, now.getSeconds());

                Barcode barcode = new Barcode(editText2.getText().toString());
                WorkOrder order = new WorkOrder(0, barcode, cal.getTime(), customer, editText3.getText().toString());
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

    private class RetrieveStock extends AsyncTask<String, Void, Stock> {
        private Context context;
        public RetrieveStock(Context context) {
            this.context = context;
        }

        @Override
        protected Stock doInBackground(String... params) {
            return FusionManager.getInstance().getStock(params[0]);
        }

        @Override
        protected void onPostExecute(Stock stock) {
            super.onPostExecute(stock);
            loadBarcodeInfo(stock);
        }
    }

    /**
     * Preload barcode data.
     */
    private void loadBarcodeInfo(Stock stock) {

        TextView textView7 = (TextView)findViewById(R.id.textView7);
        textView7.setText(stock.getName());

        TextView textView6 = (TextView)findViewById(R.id.textView6);
        String info = barcode.getWidth()+"x"+barcode.getLength()+Unit.Mm;

        double width =  Area.round(barcode.getWidth()/Unit.InchRatio, 3);
        double length = Area.round(barcode.getLength()/Unit.InchRatio, 3);
        info += "\n" + width + Unit.Inch + "x" + length + Unit.Inch;
        info += "\n(" + Unit.toFeetLabel(width) + "x" +Unit.toFeetLabel(length) + ")";

        if(barcode.getLastUpdated() != null) {
            info += "\nShipped in " + DateFormat.getDateInstance().format(barcode.getShipDate());
        }

        if(FusionManager.getInstance().getStockImage().size() > 0) {
            info += "\nBalance " + stock.getItems().get(barcode) + Unit.Piece;
            info += " at " + FusionManager.getInstance().getWarehouse(barcode.getWarehouse());
        }

        Log.i("INFO", info);
        textView6.setText(info);

        if(FusionManager.getInstance().getStockImage().size() > 0) {
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            Bitmap image = FusionManager.getInstance().getStockImage().get(barcode.getStockCode());
            imageView.setImageBitmap(image);
            new RetrieveCustomer(this).execute();
        } else {
            new DownloadImageTask(this).execute(stock.getImageUrl());
        }
    }

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

    /**
     * Download image from internet asynchronously.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;

        public DownloadImageTask(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try{
                URL request = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(request.openConnection().getInputStream());
                return bitmap;
            } catch(IOException ex) {
                Log.e("ERROR", "Could not load bitmap from: " + params[1]);
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            new RetrieveCustomer(context).execute();
        }
    }
}