package com.hlgranite.warehousescanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Expandable inventory adapter.
 * See http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 * Created by yeang-shing.then on 9/30/13.
 */
public class InventoryExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ImageView imageView;

    private List<Stock> headerList;
    public void clearGroup() {
        this.headerList.clear();
    }
    public void setGroupList(List<Stock> stocks) {
        this.headerList = stocks;
    }

    private Map<Stock, Map<Barcode,Integer>> childList;
    public void clearChildList() {
        this.childList.clear();
    }
    public void setChildList(Map<Stock, Map<Barcode,Integer>> child) {
        this.childList = child;
    }

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

        Integer balance = stock.getBalance();
        TextView textView2 = (TextView)groupView.findViewById(R.id.textView2);
        textView2.setText(balance.toString());

        Long balanceArea = 0L;
        TextView textView3 = (TextView)groupView.findViewById(R.id.textView3);
        if(stock.getArea().getValue() > 0) {
            balanceArea = stock.getArea().getValue();
            if(FusionManager.getInstance().getUnit().equals(Unit.Feet)) {
                textView3.setText(stock.getArea().toSquareFeet());
            } else {
                textView3.setText(stock.getArea().toString());
            }
        }
        highlight(groupView, balance, balanceArea);

        imageView = (ImageView)groupView.findViewById(R.id.imageView);
        Map<String, Bitmap> images = FusionManager.getInstance().getStockImage();
        if(images.containsKey(stock.getCode())) {
            Bitmap bitmap = images.get(stock.getCode());
            imageView.setImageBitmap(bitmap);
        } else {
            String url = stock.getImageUrl();
            if(url != null && !url.isEmpty()) {
                new DownloadImageTask().execute(stock.getCode(), url);
            }
        }

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

                highlight(rowView, qty, area.getValue());
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

    /**
     * Download image from internet asynchronously.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private String stockCode;

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try{
                stockCode = params[0].toString();
                URL request = new URL(params[1]);
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
            FusionManager.getInstance().addStockImage(stockCode, bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Highlight in holo red if meet danger level.
     * @param rowView
     * @param qty
     * @param area
     */
    private void highlight(View rowView, Integer qty, Long area) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rowView.getContext());
        Long minArea = parseLong(sharedPreferences.getString("prefDangerArea", "0"));
        double meter2 = area / (double)1000000;
        if(meter2 <= minArea) {
            LinearLayout linearLayout = (LinearLayout)rowView.findViewById(R.id.linearLayout);
            linearLayout.setBackgroundColor(rowView.getResources().getColor(android.R.color.holo_red_light));
        }
        int minQuantity = parseInt(sharedPreferences.getString("prefDangerQuantity", "0"));
        if(qty <= minQuantity) {
            LinearLayout linearLayout = (LinearLayout)rowView.findViewById(R.id.linearLayout);
            linearLayout.setBackgroundColor(rowView.getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private int parseInt(String value) {
        int result = 0;
        if(!value.isEmpty()) {
            result = Integer.parseInt(value);
        }

        return result;
    }
    private Long parseLong(String value) {
        Long result = 0L;
        if(!value.isEmpty()) {
            result = Long.parseLong(value);
        }

        return result;
    }

}
