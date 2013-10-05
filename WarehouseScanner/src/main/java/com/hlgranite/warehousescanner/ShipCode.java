package com.hlgranite.warehousescanner;

import java.util.Date;

/**
 * Represent shipment code and date.
 * Created by yeang-shing.then on 10/5/13.
 */
public class ShipCode {
    private String code;
    public String getCode() {
        return this.code;
    }
    private Date date;
    public Date getDate() {
        return this.date;
    }
    public ShipCode(String code, Date date) {
        this.code = code;
        this.date = date;
    }
}
