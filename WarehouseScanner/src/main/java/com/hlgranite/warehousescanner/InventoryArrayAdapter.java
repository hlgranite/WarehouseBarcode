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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeang-shing.then on 9/18/13.
 * http://www.vogella.com/articles/AndroidListView/article.html
 */
public class InventoryArrayAdapter extends ArrayAdapter<Stock> {
    private final Context context;
    private final ArrayList<Stock> values;
    private ImageView imageView;

    public InventoryArrayAdapter(Context context, ArrayList<Stock> values) {
        super(context, R.layout.layout_stock, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_stock, parent, false);

        TextView textView = (TextView)rowView.findViewById(R.id.textView);
        textView.setText(values.get(position).getName());

        TextView textView2 = (TextView)rowView.findViewById(R.id.balance);
        textView2.setText(values.get(position).getBalance().toString());

        String url = values.get(position).getImageUrl();
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
