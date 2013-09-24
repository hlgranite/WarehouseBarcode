package com.hlgranite.warehousescanner;

import android.util.Log;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/24/13.
 */
public class Shipment {
    private Barcode barcode;
    public Barcode getBarcode() {
        return this.barcode;
    }

    private Date date;
    public Date getDate() {
        return this.date;
    }

    private int quantity;
    public int getQuantity() {
        return this.quantity;
    }

    public Shipment(Barcode barcode, int quantity) {
        //Log.i("INFO", "new Shipment: " + barcode.toString() + " with "+quantity+"pcs");
        this.barcode = barcode;
        // todo: this.date = date;
        this.quantity = quantity;
    }
}
