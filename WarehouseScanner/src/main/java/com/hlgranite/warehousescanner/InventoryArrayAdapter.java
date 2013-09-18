package com.hlgranite.warehousescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yeang-shing.then on 9/18/13.
 */
public class InventoryArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public InventoryArrayAdapter(Context context, String[] values) {
        super(context, R.layout.layout_stock, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_stock, parent, false);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        // TODO: imageView.setImageResource();

        TextView textView = (TextView)rowView.findViewById(R.id.textView);
        textView.setText(values[position]);

        return rowView;
    }

}
