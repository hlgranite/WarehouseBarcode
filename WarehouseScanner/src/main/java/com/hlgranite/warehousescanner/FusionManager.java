package com.hlgranite.warehousescanner;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by yeang-shing.then on 9/18/13.
 * source: http://code.google.com/p/google-api-java-client/source/browse/fusiontables-cmdline-sample/src/main/java/com/google/api/services/samples/fusiontables/cmdline/FusionTablesSample.java?repo=samples
 */
public class FusionManager {

    protected final String urlPrefix = "https://www.googleapis.com/fusiontables/v1/query?sql=";
    protected final String stockTable = "1hyYCTWWMIXFtnd83UL6G_4ZoTDNJSoUGKwzazuM";
    protected String apiKey;
    protected ArrayList<Stock> stocks;

    /**
     * Get stock collection from web response.
     * @return
     */
    public ArrayList<Stock> getStocks() {
        this.stocks = new ArrayList<Stock>();
        String url = urlPrefix + "SELECT * FROM " + stockTable + "&key=" + apiKey;
        new DownloadStocks().execute(validateUrl(url));
        return this.stocks;
    }

    private class DownloadStocks extends AsyncTask<String, Void, ArrayList<Stock>> {
        StringBuilder builder = new StringBuilder();
        JSONArray result = null;
        JSONObject object = null;

        @Override
        protected ArrayList<Stock> doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            HttpResponse response = null;

            try {
                response = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String line = "";
                while(null != (line = reader.readLine())) {
                    builder.append(line);
                }

                JSONParser parser = new JSONParser();
                object = (JSONObject)parser.parse(validateJson(builder.toString()));
                Log.i("INFO", object.toString());
            } catch(ClientProtocolException e) {
                Log.e("ERROR", e.getMessage());
            } catch(IOException e) {
                Log.e("ERROR", e.getMessage());
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage());
                Log.e("ERROR", "Position: "+e.getPosition());
            }

            return stocks;
        }

        @Override
        protected void onPostExecute(ArrayList<Stock> stocks) {
            super.onPostExecute(stocks);

            Object o = object.get("rows");
            JSONArray rows = (JSONArray)o;
            for(int i=0;i<rows.size();i++) {
                o = rows.get(i);
                JSONArray obj = (JSONArray)o;
                String code = obj.get(0).toString();
                String name = obj.get(1).toString();
                String description = obj.get(2).toString();
                String imageUrl = obj.get(3).toString();
                stocks.add(new Stock(code,name,description,imageUrl));
            }
        }
    }

    /**
     * TODO: Validate an http url and parse unsupported character to html compatible.
     * @param url
     * @return
     */
    private String validateUrl(String url) {
        url = url.replace(" ", "%20");
        return url;
    }

    private String validateJson(String response) {
        String output = "";
        int length = response.length();
        output = response.substring(1,length-2);
        output = "[" + output + "]";
        //output = output.replace(": ", ":");
        //output = output.replace("\n", "");
        Log.i("INFO", "Json: "+output);
        return response;// output;
    }

    public FusionManager(String apiKey) {
        this.apiKey = apiKey;
    }
}
