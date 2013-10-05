package com.hlgranite.warehousescanner;

import android.graphics.Bitmap;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by yeang-shing.then on 9/18/13.
 * source: http://code.google.com/p/google-api-java-client/source/browse/fusiontables-cmdline-sample/src/main/java/com/google/api/services/samples/fusiontables/cmdline/FusionTablesSample.java?repo=samples
 */
public class FusionManager {

    private static FusionManager instance;

    private String apiKey = null;
    private CharSequence unit = Unit.Meter;

    /**
     * Auth token for read/write fusion table
     */
    private String auth = "";
    private final String urlPrefix = "https://www.googleapis.com/fusiontables/v1/query";

    private final String stockTableId = "1hyYCTWWMIXFtnd83UL6G_4ZoTDNJSoUGKwzazuM";
    private final String shipTableId = "1ScgXEsfcwRdiTgqO65_rWm6kiXkKwlfQtsFCiIM";
    private final String warehouseTableId = "1K0EohmDFo5H5O9WQRuL8oQFgAM2bUWzgboCYDRM";
    private final String stockInTableId = "1CHV0AH_1b79rVOs0TKR9VRLlBSOJ1PucqTJGLJk";

    private final String customerTableId = "1GWKZhiHRzza0v4THy8uhNJIStVqdiLOah_jTEuE";
    private final String stockOutTableId = "1tBDriL2j2nByrDSXP1bAIgKJ71I3atNdfPlcEX4";

    private ArrayList<Customer> customers;
    private ArrayList<Warehouse> warehouses;
    private ArrayList<ShipCode> shipCodes;

    private ArrayList<Shipment> shipments;
    private ArrayList<WorkOrder> workOrders;
    private ArrayList<Stock> stocks;
    private Map<String, Bitmap> stockImages;

    /**
     * Return a collection map of stock images.
     * @return
     */
    public Map<String, Bitmap> getStockImage() {
        return this.stockImages;
    }

    protected FusionManager() {
        this.isAuthenticated = false;
        this.stockImages = new HashMap<String, Bitmap>();
    }

    /**
     * Reset all data to null and retrieve again.
     */
    public void reset() {
        Log.i("INFO", "FusionManager.reset()");
        this.stocks = null;
        this.workOrders = null;
        this.shipments = null;
    }
    public void resetWorkOrder() {
        this.workOrders = null;
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

    public void setUnit(CharSequence unit) {
        this.unit = unit;
    }
    public CharSequence getUnit() {
        //Log.i("INFO", "Unit: "+this.unit);
        return this.unit;
    }

    private boolean isAuthenticated;
    public boolean getAuthenticate() {
        return this.isAuthenticated;
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
                    this.isAuthenticated = true;
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
     * Return a collection of customer code.
     * @return
     */
    public ArrayList<String> getCustomers() {

        ArrayList<String> output = new ArrayList<String>();

        String url = urlPrefix + "?sql=SELECT code FROM " + customerTableId + "&key=" + apiKey;
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
                String code = obj.get(0).toString().trim();
                output.add(code);
            }
        }

        return output;
    }

    public ArrayList<Warehouse> getWarehouses() {
        if(this.warehouses == null) {
            this.warehouses = new ArrayList<Warehouse>();
        } else {
            return this.warehouses;
        }

        String url = urlPrefix + "?sql=SELECT * FROM " + warehouseTableId + "&key=" + apiKey;
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

                String code = obj.get(0).toString().trim();// code
                String name = obj.get(1).toString().trim();// name

                Warehouse warehouse = new Warehouse(code, name);
                this.warehouses.add(warehouse);
            }
        }

        return this.warehouses;
    }
    public String getWarehouse(String code) {
        if(this.warehouses == null) return "";
        if(this.warehouses.size() == 0) return "";

        for(Warehouse warehouse: this.warehouses) {
            if(warehouse.getCode().equals(code)) {
                return warehouse.getName();
            }
        }

        return "";
    }

    public ArrayList<ShipCode> getShipCodes() {
        if(this.shipCodes == null) {
            this.shipCodes = new ArrayList<ShipCode>();
        } else {
            return this.shipCodes;
        }

        String url = urlPrefix + "?sql=SELECT * FROM " + shipTableId + "&key=" + apiKey;
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

                String code = obj.get(0).toString().trim();// code
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(obj.get(1).toString().trim());
                } catch (java.text.ParseException e) {
                    Log.e("ERROR", e.getMessage());
                }

                ShipCode shipCode = new ShipCode(code, date);
                this.shipCodes.add(shipCode);
            }
        }

        return this.shipCodes;
    }
    public Date getShipDate(String code) {
        if(this.shipCodes == null) return null;
        if(this.shipCodes.size() == 0) return null;

        for(ShipCode shipCode: this.shipCodes) {
            if(shipCode.getCode().equals(code)) {
                return shipCode.getDate();
            }
        }

        return null;
    }

    /**
     * Get stock collection from web response.
     * @return
     */
    public ArrayList<Stock> getStocks() {
        if(this.stocks == null) {
            this.stocks = new ArrayList<Stock>();
        } else {
            return this.stocks;
        }

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
                String code = obj.get(0).toString().trim();
                String name = obj.get(1).toString().trim();
                String description = obj.get(2).toString().trim();
                String imageUrl = obj.get(3).toString().trim();
                this.stocks.add(new Stock(code,name,description,imageUrl));
            }
        }

        return this.stocks;
    }
    public Stock getStock(String code) {
        Stock stock = null;
        if(this.stocks == null) {
            String url = urlPrefix + "?sql=SELECT * FROM " + stockTableId + " WHERE code='" + code + "'" + "&key=" + apiKey;
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
                    //String code = obj.get(0).toString().trim();
                    String name = obj.get(1).toString().trim();
                    String description = obj.get(2).toString().trim();
                    String imageUrl = obj.get(3).toString().trim();
                    stock = new Stock(code,name,description,imageUrl);
                }
            }
        } else {
            for(Stock s: this.stocks) {
                if(s.getCode().equals(code)) {
                    return s;
                }
            }
        }

        return stock;
    }

    public void addStockImage(String stockCode, Bitmap bitmap) {
        if(!this.stockImages.containsKey(stockCode)) {
            this.stockImages.put(stockCode, bitmap);
        }
    }
    /**
     * Insert a record into StockOuts fusion table.
     * Source: https://github.com/jedld/GiNote/blob/master/src/com/dayosoft/utils/FusionTableService.java
     * @param order
     */
    public void checkout(WorkOrder order, int quantity) {
        try {
            HttpPost post = new HttpPost(urlPrefix);

            String sql = "";
            for(int i=0;i<quantity;i++) {
                sql += "INSERT INTO " + stockOutTableId + " (barcode,date,sold,reference) VALUES (";
                sql += "'"+order.getBarcode().getNumber()+"','"+String.format("%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1tS", order.getDate())+"','"+order.getCustomer()+"','"+order.getReference()+"'";
                sql += ");";
            }
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
     * Rollback checkout wrongly by delete a record from StockOut table.
     * @param id
     */
    public void removeWorkOrder(long id) {
        if(id == 0) return;

        try {
            HttpPost post = new HttpPost(urlPrefix);

            String sql = "DELETE FROM " + stockOutTableId + " WHERE ROWID='"+id+"'";
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

    public ArrayList<Shipment> getShipments() {
        if(this.shipments == null) {
            this.shipments = new ArrayList<Shipment>();
        } else {
            return this.shipments;
        }

        String url = urlPrefix + "?sql=SELECT * FROM " + stockInTableId + "&key=" + apiKey;
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

                String code = obj.get(0).toString().trim();// stock code
                int width = Integer.parseInt(obj.get(1).toString().trim()); // length
                int length = Integer.parseInt(obj.get(2).toString().trim()); // width
                String shipment = obj.get(3).toString().trim();
                String warehouse = obj.get(4).toString().trim();

                Barcode barcode = new Barcode(code, width, length, shipment, warehouse);
                int quantity = Integer.parseInt(obj.get(5).toString().trim());
                this.shipments.add(new Shipment(barcode, quantity));
            }
        }

        return this.shipments;
    }

    /**
     * Retrieve history of work order after checkout.
     * @param limit Number of row to return. 0 for no limit.
     * @return
     */
    public ArrayList<WorkOrder> getWorkOrders(int limit) {
        if(this.workOrders == null) {
            this.workOrders = new ArrayList<WorkOrder>();
        } else {
            return this.workOrders;
        }

        String url = urlPrefix + "?sql=SELECT ROWID,barcode,date,sold,reference FROM " + stockOutTableId + " ORDER BY date DESC ";
        if(limit > 0) url += "LIMIT 50 ";
        url += "&key=" + apiKey;

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

                long id = Long.parseLong(obj.get(0).toString());
                String number = obj.get(1).toString().trim();
                Barcode barcode = new Barcode(number);
                Date date = parseDate(obj.get(2).toString());
                String customer = obj.get(3).toString().trim();
                String reference = obj.get(4).toString().trim();
                this.workOrders.add(new WorkOrder(id, barcode, date, customer, reference));
            }
        }

        return this.workOrders;
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
    private String validateUrl(String url) {
        url = url.replace(" ", "+");// "%20");
        return url;
    }

    /**
     * Return dateDialog from a dateDialog string input.
     * @param dateString
     * @return
     */
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dateString.length() == 10) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        Date converted = new Date();
        try {
            converted = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            Log.e("ERROR", e.getMessage());
        }

        return converted;
    }
}