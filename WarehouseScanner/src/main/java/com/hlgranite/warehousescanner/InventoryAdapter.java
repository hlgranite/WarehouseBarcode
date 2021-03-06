package com.hlgranite.warehousescanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Obsoleted.
 * Created by yeang-shing.then on 9/18/13.
 * http://www.vogella.com/articles/AndroidListView/article.html
 */
public class InventoryAdapter extends ArrayAdapter<Stock> {
    private final Context context;
    private final ArrayList<Stock> values;
    private ImageView imageView;

    public InventoryAdapter(Context context, ArrayList<Stock> values) {
        super(context, R.layout.layout_stock, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_stock, parent, false);

        Stock stock = values.get(position);

        TextView textView = (TextView)rowView.findViewById(R.id.textView);
        textView.setText(stock.getName());

        TextView textView2 = (TextView)rowView.findViewById(R.id.textView2);
        textView2.setText(stock.getBalance().toString());

        TextView textView3 = (TextView)rowView.findViewById(R.id.textView3);
        if(stock.getArea().getValue() > 0) {
            if(FusionManager.getInstance().getUnit().equals(Unit.Feet)) {
                textView3.setText(stock.getArea().toSquareFeet());
            } else {
                textView3.setText(stock.getArea().toString());
            }
        }

        String url = stock.getImageUrl();
        if(url != null && !url.isEmpty()) {
            imageView = (ImageView)rowView.findViewById(R.id.imageView);
            new DownloadImageTask().execute(url);
        }

        return rowView;
    }

    /**
     * Download image from internet asynchronously.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try{
                URL request = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(request.openConnection().getInputStream());
                return bitmap;
            } catch(IOException ex) {
                Log.e("ERROR", "Could not load bitmap from: " + params[0]);
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}
