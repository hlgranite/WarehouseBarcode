package com.hlgranite.warehousescanner;

/**
 * Warehouse object.
 * Created by yeang-shing.then on 10/5/13.
 */
public class Warehouse {
    private String code;
    public String getCode() {
        return this.code;
    }
    private String name;
    public String getName() {
        return this.name;
    }
    public Warehouse(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
