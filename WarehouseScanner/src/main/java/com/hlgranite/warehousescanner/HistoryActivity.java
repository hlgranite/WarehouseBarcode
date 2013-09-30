package com.hlgranite.warehousescanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    /** Determine focus back on screen after click on menu */
    private boolean isBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("INFO", "HistoryActivity.onResume");
        new RetrieveHistory(this).execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("INFO", "HistoryActivity.onWindowFocusChanged");

        isBack = !isBack;
        if(isBack) {
            new RetrieveHistory(this).execute();
        }
    }

    private class RetrieveHistory extends AsyncTask<String, Void, ArrayList<WorkOrder>> {
        private Context context;

        public RetrieveHistory(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<WorkOrder> doInBackground(String... params) {
            return FusionManager.getInstance().getWorkOrders(50);
        }

        @Override
        protected void onPostExecute(ArrayList<WorkOrder> workOrders) {
            super.onPostExecute(workOrders);

            final ListView listView = (ListView)findViewById(R.id.listView);
            if(listView.getAdapter() == null) {
                WorkOrderAdapter adapter = new WorkOrderAdapter(context, workOrders);
                listView.setAdapter(adapter);
            } else if(listView.getAdapter().getCount() == 0) {
                WorkOrderAdapter existing = (WorkOrderAdapter)listView.getAdapter();
                for(WorkOrder workOrder: workOrders) {
                    existing.add(workOrder);
                }
                existing.notifyDataSetChanged();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }
    
}
