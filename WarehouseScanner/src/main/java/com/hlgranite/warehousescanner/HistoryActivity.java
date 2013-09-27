package com.hlgranite.warehousescanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listView = (ListView)findViewById(R.id.listView);
        if(listView.getAdapter() != null) {
            WorkOrderAdapter existing = (WorkOrderAdapter)listView.getAdapter();
            existing.clear();
        }

        FusionManager.getInstance().resetWorkOrder();
        new RetrieveHistory(this).execute();
    }

    private class RetrieveHistory extends AsyncTask<String, Void, ArrayList<WorkOrder>> {
        private Context context;

        public RetrieveHistory(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<WorkOrder> doInBackground(String... params) {
            return FusionManager.getInstance().getWorkOrders();
        }

        @Override
        protected void onPostExecute(ArrayList<WorkOrder> workOrders) {
            super.onPostExecute(workOrders);

            if(listView.getAdapter() == null) {
                WorkOrderAdapter adapter = new WorkOrderAdapter(context, workOrders);
                listView.setAdapter(adapter);
            } else {
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
