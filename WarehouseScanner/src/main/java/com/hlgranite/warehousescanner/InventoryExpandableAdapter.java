package com.hlgranite.warehousescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Expandable inventory adapter.
 * See http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 * Created by yeang-shing.then on 9/30/13.
 */
public class InventoryExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Stock> headerList;
    private Map<Stock, Map<Barcode,Integer>> childList;

    public InventoryExpandableAdapter(Context context, List<Stock> headerList, Map<Stock, Map<Barcode, Integer>> childList) {
        this.context = context;
        this.headerList = headerList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(headerList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(headerList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View groupView = inflater.inflate(R.layout.layout_stock, parent, false);

        Stock stock = headerList.get(groupPosition);

        TextView textView = (TextView)groupView.findViewById(R.id.textView);
        textView.setText(stock.getName());

        TextView textView2 = (TextView)groupView.findViewById(R.id.textView2);
        textView2.setText(stock.getBalance().toString());

        TextView textView3 = (TextView)groupView.findViewById(R.id.textView3);
        if(stock.getArea().getValue() > 0) {
            if(FusionManager.getInstance().getUnit().equals(Unit.Feet)) {
                textView3.setText(stock.getArea().toSquareFeet());
            } else {
                textView3.setText(stock.getArea().toString());
            }
        }

//        TODO: String url = stock.getImageUrl();
//        if(url != null && !url.isEmpty()) {
//            imageView = (ImageView)rowView.findViewById(R.id.imageView);
//            new DownloadImageTask().execute(url);
//        }

        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_item, parent, false);

        Stock stock = headerList.get(groupPosition);
        Map<Barcode, Integer> pair = childList.get(stock);// (Map<Barcode, Integer>)getChild(groupPosition, childPosition);
        //Barcode barcode = (Barcode)childList.get(stock);

        int i = 0;
        for(Barcode barcode: pair.keySet()) {
            if(i == childPosition) {
                TextView textView = (TextView)rowView.findViewById(R.id.textView);
                textView.setText(barcode.toString());

                Integer qty =  pair.get(barcode);
                TextView textView2 = (TextView)rowView.findViewById(R.id.textView2);
                textView2.setText(qty.toString());

                TextView textView3 = (TextView)rowView.findViewById(R.id.textView3);
                Area area = new Area(qty * barcode.getWidth() * barcode.getLength());
                if(area.getValue() > 0) {
                    if(FusionManager.getInstance().getUnit().equals(Unit.Feet)) {
                        textView3.setText(area.toSquareFeet());
                    } else {
                        textView3.setText(area.toString());
                    }
                }
                break;
            }
            i++;
        }

        // TODO: Show last checkout date

        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
