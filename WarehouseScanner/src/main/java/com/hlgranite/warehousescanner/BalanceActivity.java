package com.hlgranite.warehousescanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BalanceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        // Get data from fusion table
        new RetrieveBalance(this).execute();
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

            final InventoryArrayAdapter adapter = new InventoryArrayAdapter(context, stocks);
            final ListView listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    final Stock item = (Stock)parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(), item.getDescription(), Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.balance, menu);
        return true;
    }
    
}
