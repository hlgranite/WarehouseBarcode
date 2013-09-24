package com.hlgranite.warehousescanner;

import android.util.Log;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/19/13.
 */
public class Barcode {

    private String stockCode;
    public String getStockCode() {
        return this.stockCode;
    }

    private Integer width;
    public Integer getWidth() {
        return this.width;
    }

    private Integer length;
    public Integer getLength() {
        return this.length;
    }

    private String shipment;
    public String getShipment() {
        return this.shipment;
    }

    private String warehouseCode;
    public String getWarehouse() {
        return this.warehouseCode;
    }

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
//        Log.i("INFO", "Stock: " + stockCode);
//        Log.i("INFO", "Width: " + width);
//        Log.i("INFO", "Length: " + length);
//        Log.i("INFO", "Shipment: " + shipment);
//        Log.i("INFO", "Warehouse: " + warehouseCode);
    }
    public String getNumber() {
        return this.number;
    }

    public Barcode(String number) {
        setNumber(number);
    }
    public Barcode(String stockCode, Integer width, Integer length, String shipment, String warehouse) {
        this.stockCode = stockCode;
        this.width = width;
        this.length = length;
        this.shipment = shipment;
        this.warehouseCode = warehouse;
    }

    @Override
    public String toString() {
        return this.stockCode+this.width.toString()+this.length.toString()+this.shipment+this.warehouseCode;
    }
}
