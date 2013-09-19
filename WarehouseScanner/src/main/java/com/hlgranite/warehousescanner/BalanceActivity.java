package com.hlgranite.warehousescanner;

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
        String url = "";

        // Approach 2
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

        // Approach 1
//        HttpTransport httpTransport = new NetHttpTransport();
//        HttpRequestFactory factory = httpTransport.createRequestFactory(null);
//        GenericUrl genericUrl = new GenericUrl(url);
//        HttpRequest request = null;
//        HttpResponse response = null;
//
//        try {
//            request = factory.buildGetRequest(genericUrl);
//            response = request.execute();
//        } catch(IOException ex) {
//            Log.e("ERROR", ex.getMessage());
//        }
//
//        FusionManager fusionManager = null;
//        try {
//            fusionManager = response.parseAs(FusionManager.class);
//        } catch (IOException ex) {
//            Log.e("ERROR", ex.getMessage());
//        }
//        List<Stock> stocks = fusionManager.getStocks();

        ArrayList<Stock> stocks = new ArrayList<Stock>();
        stocks.add(new Stock("BLUE", "Blue Pearl", "", "http://www.maidstonefuneraldirectors.co.uk/wp-content/uploads/2012/08/blue_pearl-sample.jpg"));
        stocks.add(new Stock("RUBY", "Ruby Red", "", ""));
        stocks.add(new Stock("BLAC", "Black Galaxy", "", ""));

        final InventoryArrayAdapter adapter = new InventoryArrayAdapter(this, stocks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Stock item = (Stock)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG)
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
