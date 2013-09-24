package com.hlgranite.warehousescanner;

import android.util.Log;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/19/13.
 */
public class Barcode {

    private String stockCode;
    private Integer width;
    private Integer length;
    private String shipment;
    private String warehouseCode;

    private String number;
    public void setNumber(String number) {
        this.number = number;
        if(number.length() >= 4)
            this.stockCode = number.substring(0, 4);
        if(number.length() >= 8)
            this.width = Integer.parseInt(number.substring(4,8));
        if(number.length() >= 12)
            this.length = Integer.parseInt(number.substring(8,12));
        if(number.length() >= 15)
            this.shipment = number.substring(12,15);
        if(number.length() >= 16)
            this.warehouseCode = number.substring(15,16);
        Log.i("INFO", "Stock:" + stockCode);
        Log.i("INFO", "Width: " + width);
        Log.i("INFO", "Length: " + length);
        Log.i("INFO", "Shipment: " + shipment);
        Log.i("INFO", "Warehouse: " + warehouseCode);
    }
    public String getNumber() {
        return this.number;
    }

    private Date date;
    public Date getDate() {
        return this.date;
    }

    private String customer;
    public String getCustomer() {
        return this.customer;
    }

    private String reference;
    public String getReference() {
        return this.reference;
    }

    private int quantity;
    public int getQuantity() {
        return this.quantity;
    }

    public Barcode(String number, Date date, String customer, String reference) {
        setNumber(number);
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }
    public Barcode(String number, Date date, String customer, String reference, int quantity) {
        setNumber(number);
        this.date = date;
        this.customer = customer;
        this.reference = reference;
        this.quantity = quantity;
    }
}
