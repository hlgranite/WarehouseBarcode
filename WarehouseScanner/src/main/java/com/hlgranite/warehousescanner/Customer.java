package com.hlgranite.warehousescanner;

/**
 * Created by yeang-shing.then on 9/26/13.
 */
public class Customer {

    private String code;
    public String getCode() {
        return this.code;
    }

    private String name;
    public String getName() {
        return this.name;
    }

    public Customer(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
