package com.hlgranite.warehousescanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BalanceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        final ListView listView = (ListView)findViewById(R.id.listView);
        // TODO: Get data from fusion table
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};
        final ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<values.length;i++) {
            list.add(values[i]);
        }

        String url = "";//TODO: url

        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory factory = httpTransport.createRequestFactory(null);
        //JacksonFactory jsonFactory = new JacksonFactory();
        GenericUrl genericUrl = new GenericUrl(url);
        HttpRequest request = null;
        HttpResponse response = null;

        try {
            request = factory.buildGetRequest(genericUrl);
            response = request.execute();
        } catch(IOException ex) {
            Log.e("ERROR", ex.getMessage());
        }


//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//        HttpResponse response = null;
//
//        try {
//            response = httpClient.execute(httpPost);
//        } catch(ClientProtocolException ex) {
//            Log.e("ERROR", ex.getMessage());
//        } catch(IOException ex) {
//            Log.e("ERROR", ex.getMessage());
//        }

        FusionManager fusionManager = null;
        try {
            fusionManager = response.parseAs(FusionManager.class);
        } catch (IOException ex) {
            Log.e("ERROR", ex.getMessage());
        }
        List<Stock> stocks = fusionManager.getStocks();
        final InventoryArrayAdapter adapter = new InventoryArrayAdapter(this, stocks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.balance, menu);
        return true;
    }
    
}
