package com.hlgranite.warehousescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    /** Determine focus back on screen after click on menu */
    private boolean isBack = true;
    /** selected index in listView */
    private int selectedIndex = -1;

    // todo: private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = (ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
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
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog = ProgressDialog.show(context, "Please wait...", "Retrieving data...");
        }

        @Override
        protected void onPostExecute(ArrayList<WorkOrder> workOrders) {
            super.onPostExecute(workOrders);

            //if(dialog != null) dialog.dismiss();
            Log.i("INFO", "Total history: "+workOrders.size());
            final ListView listView = (ListView)findViewById(R.id.listView);
            if(listView.getAdapter() == null) {
                WorkOrderAdapter adapter = new WorkOrderAdapter(context, workOrders);
                listView.setAdapter(adapter);
                listView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //v.setSelected(true);
                        return true;
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedIndex = position;
                        Log.i("INFO", "Selected position: "+position);
                        //view.setSelected(true);
                        return false;
                    }
                });
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.action_delete:
                ListView listView = (ListView)findViewById(R.id.listView);
                // Fail always null: WorkOrder workOrder = (WorkOrder)listView.getSelectedItem();
                if(selectedIndex > -1) { //if(workOrder != null)
                    WorkOrderAdapter adapter = (WorkOrderAdapter)listView.getAdapter();
                    WorkOrder workOrder = adapter.getItem(selectedIndex);
                    long id = workOrder.getId();
                    Log.i("INFO", "remove selected WorkOrder:"+id);
                    FusionManager.getInstance().removeWorkOrder(id);
                    //FusionManager.getInstance().resetWorkOrder();
                    adapter.remove(workOrder);// HACK: Not really accurate if http post failed.

                    // force to auto refresh after deletion
                    isBack = false;
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
