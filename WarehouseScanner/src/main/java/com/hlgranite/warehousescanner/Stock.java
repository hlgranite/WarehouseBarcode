package com.hlgranite.warehousescanner;

import java.util.Random;

/**
 * Created by yeang-shing.then on 9/18/13.
 */
public class Stock {

    private String code;
    public String getCode() {
        return this.code;
    }

    private String name;
    public String getName() {
        return this.name;
    }

    private String description;
    public String getDescription() {
        return this.description;
    }

    private String imageUrl; // TODO: Get smaller bitmap for this field in fusion table record to reduce loading time.
    public String getImageUrl() {
        return this.imageUrl;
    }

    private Integer balance;
    public Integer getBalance() {
        // TODO: Calculate balance
        this.balance = new Random().nextInt(100);
        return this.balance;
    }

    public Stock(String code, String name, String description, String imageUrl) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
