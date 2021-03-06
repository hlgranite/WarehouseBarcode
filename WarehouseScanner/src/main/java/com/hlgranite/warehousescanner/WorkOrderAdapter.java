package com.hlgranite.warehousescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by yeang-shing.then on 9/27/13.
 */
public class WorkOrderAdapter extends ArrayAdapter<WorkOrder> {
    private final Context context;
    private final ArrayList<WorkOrder> values;

    public WorkOrderAdapter(Context context, ArrayList<WorkOrder> values) {
        super(context, R.layout.layout_workorder, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View contentView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_workorder, parent, false);

        WorkOrder order = values.get(position);

        TextView textView = (TextView)rowView.findViewById(R.id.textView);
        textView.setText(order.getBarcode().toString());
        // see http://developer.android.com/reference/java/text/SimpleDateFormat.html
        TextView textView3 = (TextView)rowView.findViewById(R.id.textView3);
        textView3.setText(DateFormat.getDateTimeInstance().format(order.getDate()));

        TextView textView2 = (TextView)rowView.findViewById(R.id.textView2);
        textView2.setText(order.getCustomer());
        TextView textView4 = (TextView)rowView.findViewById(R.id.textView4);
        textView4.setText(order.getReference());

        return rowView;
    }

}
