package com.hlgranite.warehousescanner;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/19/13.
 */
public class Barcode {

    private String stockCode;
    public String getStockCode() {
        return this.stockCode;
    }

    private int width;
    public int getWidth() {
        return this.width;
    }

    private int length;
    public int getLength() {
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
        if(number.length() >= 15) {
            this.shipment = number.substring(12,15);
            this.shipDate = FusionManager.getInstance().getShipDate(this.shipment);
            this.lastUpdated = FusionManager.getInstance().getShipDate(this.shipment);
        }
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

    private Date shipDate;
    /**
     * Return ship date for this barcode.
     * @return
     */
    public Date getShipDate() {
        return this.shipDate;
    }

    private Date lastUpdated;
    /**
     * Return last updated date. This will be recent checkout date if not return shipment date.
     * @return
     */
    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public Barcode(String number) {
        setNumber(number);
    }
    public Barcode(String stockCode, int width, int length, String shipment, String warehouse) {
        this.stockCode = stockCode;
        this.width = width;
        this.length = length;
        this.shipment = shipment;
        this.shipDate = FusionManager.getInstance().getShipDate(this.shipment);
        this.lastUpdated = FusionManager.getInstance().getShipDate(this.shipment);
        this.warehouseCode = warehouse;
        this.number = toString();
    }

    /**
     * Keep the last updated date either shipment or checkout date.
     * @param date
     */
    public void setLastUpdated(Date date) {
        if(date == null) return;

        if(this.shipDate == null) this.shipDate = date;
        if(this.lastUpdated == null) {
            this.lastUpdated = date;
        } else {
            if(date.compareTo(this.lastUpdated) > 0) {
                this.lastUpdated = date;
            }
        }
    }

    @Override
    public String toString() {
        return this.stockCode+String.format(Unit.LENGTH_FORMAT, this.width)
                + String.format(Unit.LENGTH_FORMAT, this.length)+this.shipment+this.warehouseCode;
    }

    //<editor-fold desc="Determine uniqueness when calling Collections.contains">
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        Barcode b = (Barcode)obj;
        return toString().equals(b.toString());
    }
    //</editor-fold>
}
