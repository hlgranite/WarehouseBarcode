package com.hlgranite.warehousescanner;

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
    protected static ArrayList<Stock> stocks;

    /**
     * Get stock collection from web response.
     * @return
     */
    public ArrayList<Stock> getStocks() {
        this.stocks = new ArrayList<Stock>();
        String url = urlPrefix + "SELECT * FROM " + stockTable + "&key=" + apiKey;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(validateUrl(url));
        HttpResponse response = null;
        StringBuilder builder = new StringBuilder();
        JSONObject object = null;

        try {
            response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String line = "";
            while(null != (line = reader.readLine())) {
                builder.append(line);
            }

            JSONParser parser = new JSONParser();
            object = (JSONObject)parser.parse(builder.toString());
        } catch(ClientProtocolException e) {
            Log.e("ERROR", e.getMessage());
        } catch(IOException e) {
            Log.e("ERROR", e.getMessage());
        } catch (ParseException e) {
            Log.e("ERROR", e.getMessage());
            Log.e("ERROR", "Position: "+e.getPosition());
        }

        Object o = object.get("rows");
        if(o != null) {
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

        return this.stocks;
    }

    /**
     * Validate an http url and parse unsupported character to html compatible.
     * @param url
     * @return
     */
    private String validateUrl(String url) {
        url = url.replace(" ", "+");// "%20");
        return url;
    }

    public FusionManager(String apiKey) {
        this.apiKey = apiKey;
    }
}
