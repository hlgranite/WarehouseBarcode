package com.hlgranite.warehousescanner;

import java.util.Date;

/**
 * Created by yeang-shing.then on 9/19/13.
 */
public class Barcode {
    private String number;
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

    public Barcode(String number, Date date, String customer, String reference) {
        this.number = number;
        this.date = date;
        this.customer = customer;
        this.reference = reference;
    }
}
