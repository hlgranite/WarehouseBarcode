package com.hlgranite.warehousescanner;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/24/13.
 */
public class WorkOrder {
    private Barcode barcode;
    public Barcode getBarcode() {
        return this.barcode;
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

    public WorkOrder(Barcode barcode, Date date, String customer, String reference) {
        this.barcode = barcode;
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }
    public WorkOrder(String barcode, Date date, String customer, String reference) {
        this.barcode = new Barcode(barcode);
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }
}
