package com.hlgranite.warehousescanner;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/24/13.
 */
public class WorkOrder {
    private long id;
    public long getId() {
        return this.id;
    }
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

    public WorkOrder(long id, Barcode barcode, Date date, String customer, String reference) {
        this.id = id;
        this.barcode = barcode;
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }
    public WorkOrder(long id, String barcode, Date date, String customer, String reference) {
        this.id = id;
        this.barcode = new Barcode(barcode);
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }

}
