package com.hlgranite.warehousescanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class BalanceActivity extends Activity {
    /** Determine focus back on screen after click on menu */
    private boolean isBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("INFO", "BalanceActivity.onResume()");
        new RetrieveShipment(this).execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("INFO", "BalanceActivity.onWindowFocusChanged");

        isBack = !isBack;
        if(isBack) {
            new RetrieveShipment(this).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.balance, menu);
        return true;
    }

    private class RetrieveShipment extends AsyncTask<String, Void, ArrayList<Shipment>> {

        private Context context;
        public RetrieveShipment(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Shipment> doInBackground(String... params) {
            return FusionManager.getInstance().getShipments();
        }

        @Override
        protected void onPostExecute(ArrayList<Shipment> shipments) {
            super.onPostExecute(shipments);
            new RetrieveWorkOrder(context).execute();
        }
    }

    private class RetrieveWorkOrder extends AsyncTask<String, Void, ArrayList<WorkOrder>> {

        private Context context;
        public RetrieveWorkOrder(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<WorkOrder> doInBackground(String... params) {
            FusionManager.getInstance().resetWorkOrder();
            return FusionManager.getInstance().getWorkOrders(0);
        }

        @Override
        protected void onPostExecute(ArrayList<WorkOrder> workOrders) {
            super.onPostExecute(workOrders);
            new RetrieveBalance(context).execute();
        }
    }

    private class RetrieveBalance extends AsyncTask<String, Void, ArrayList<Stock>> {

        private Context context;
        public RetrieveBalance(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Stock> doInBackground(String... params) {
            return FusionManager.getInstance().getStocks();
        }

        @Override
        protected void onPostExecute(ArrayList<Stock> stocks) {
            super.onPostExecute(stocks);

            final ListView listView = (ListView)findViewById(R.id.listView);
            if(listView.getAdapter() == null) {
                Log.i("INFO", "Set once for list adapter only");
                final InventoryAdapter adapter = new InventoryAdapter(context, stocks);
                adapter.sort(new Comparator<Stock>() {
                    @Override
                    public int compare(Stock lhs, Stock rhs) {
                        return lhs.getCode().compareTo(rhs.getCode());
                    }
                });
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final Stock item = (Stock) parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), item.getDescription(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
            } else if(listView.getAdapter().getCount() == 0) {
                Log.i("INFO", "Repopulate");
                InventoryAdapter existing = (InventoryAdapter)listView.getAdapter();
                for(Stock stock: stocks) {
                    existing.add(stock);
                }
                existing.sort(new Comparator<Stock>(){
                    @Override
                    public int compare(Stock lhs, Stock rhs) {
                        return lhs.getCode().compareTo(rhs.getCode());
                    }
                });
                existing.notifyDataSetChanged();
            }
        }
    }
    
}
