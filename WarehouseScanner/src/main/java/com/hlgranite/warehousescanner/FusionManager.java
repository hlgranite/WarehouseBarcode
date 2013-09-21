package com.hlgranite.warehousescanner;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yeang-shing.then on 9/18/13.
 * source: http://code.google.com/p/google-api-java-client/source/browse/fusiontables-cmdline-sample/src/main/java/com/google/api/services/samples/fusiontables/cmdline/FusionTablesSample.java?repo=samples
 */
public class FusionManager {

    private static FusionManager instance;

    private String apiKey = null;
    /**
     * Auth token for read/write fusion table
     */
    private String auth = "";
    private final String urlPrefix = "https://www.googleapis.com/fusiontables/v1/query";
    private final String stockTableId = "1hyYCTWWMIXFtnd83UL6G_4ZoTDNJSoUGKwzazuM";
    private final String stockOutTableId = "1tBDriL2j2nByrDSXP1bAIgKJ71I3atNdfPlcEX4";

    private ArrayList<Stock> stocks;

    protected FusionManager() {

    }

    /**
     * Always return a same datastore object.
     * @return
     */
    public static FusionManager getInstance() {
        if(instance == null) {
            // TODO: Handle multithreaded
            instance = new FusionManager();
        }

        return instance;
    }

    /**
     * Set api key for datastore.
     * @param api Google SDK API key for fusion table.
     */
    public void setApi(String api) {
        this.apiKey = api;
    }

    /**
     * Authenticate with google account to retrieve auth token.
     * @param email
     * @param password
     */
    public void authenticate(String email, String password) {

        try {
//            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost("https://www.google.com/accounts/ClientLogin");
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("Email", email));
//            params.add(new BasicNameValuePair("Passwd", password));
//            params.add(new BasicNameValuePair("service", "fusiontables"));
//            //params.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
//            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//            post.setEntity(ent);
//            new PostWeb(post).execute();

            String url = "https://www.google.com/accounts/ClientLogin?Email="+email+"&Passwd="+password+"&service=fusiontables";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = null;
            StringBuilder builder = new StringBuilder();
            response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String line = "";
            while(null != (line = reader.readLine())) {
                if(line.startsWith("Auth=")) {
                    this.auth = line.replace("Auth=","");
                    Log.i("INFO", "Auth: "+line);
                }
                builder.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e("ERROR", e.getMessage());
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    /**
     * Get stock collection from web response.
     * @return
     */
    public ArrayList<Stock> getStocks() {
        this.stocks = new ArrayList<Stock>();
        String url = urlPrefix + "?sql=SELECT * FROM " + stockTableId + "&key=" + apiKey;

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
            reader.close();
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
                this.stocks.add(new Stock(code,name,description,imageUrl));
            }
        }

        return this.stocks;
    }

    private String toDateFormat(Date date) {
        //date.getYear()+"-"+date.getMonth()+"-"

        return "";
    }

    /**
     * Insert a record into StockOuts fusion table.
     * Source: https://github.com/jedld/GiNote/blob/master/src/com/dayosoft/utils/FusionTableService.java
     * @param barcode
     */
    public void checkout(Barcode barcode) {
        try {
            HttpPost post = new HttpPost(urlPrefix);

            String sql = "INSERT INTO " + stockOutTableId + " (barcode,date,sold,reference) VALUES (";
            sql += "'"+barcode.getNumber()+"','"+String.format("%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1tS",barcode.getDate())+"','"+barcode.getCustomer()+"','"+barcode.getReference()+"'";
            sql += ")";
            Log.i("INFO", sql);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sql", sql));
            params.add(new BasicNameValuePair("key", apiKey));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            post.setHeader("Authorization", "GoogleLogin auth="+auth);

            new PostWeb(post).execute();
        } catch(IOException e) {
            Log.e("ERROR", e.getMessage());
        }
    }
    /**
     * Add a new stock into fusion table.
     * @param stock
     */
    public void addStock(Stock stock) {
        try {
            HttpPost post = new HttpPost(urlPrefix);

            String sql = "INSERT INTO " + stockTableId + " (code,name,description,imageUrl) VALUES (";
            sql += "'"  +stock.getCode() + "','" + stock.getName() + "','" + stock.getDescription() + "','" + stock.getImageUrl() + "'";
            sql += ")";
            Log.i("INFO", sql);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sql", sql));
            params.add(new BasicNameValuePair("key", apiKey));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            post.setHeader("Authorization", "GoogleLogin auth="+auth);

            new PostWeb(post).execute();
        } catch(IOException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private class PostWeb extends AsyncTask<String, Void, HttpEntity> {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = null;

        public PostWeb(HttpPost post) {
            this.post = post;
        }

        @Override
        protected HttpEntity doInBackground(String... params) {
            HttpEntity entity = null;
            try {
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());
            }
            return entity;
        }

        @Override
        protected void onPostExecute(HttpEntity httpEntity) {
            super.onPostExecute(httpEntity);

            if(httpEntity != null) {
                try {
                    Log.i("INFO", EntityUtils.toString(httpEntity));
                } catch (IOException e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        }
    }

    /**
     * Validate an http url and parse unsupported character to html compatible.
     * @param url
     * @return
     */
    private static String validateUrl(String url) {
        url = url.replace(" ", "+");// "%20");
        return url;
    }
}