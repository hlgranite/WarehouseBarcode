package com.hlgranite.warehousescanner;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by yeang-shing.then on 9/18/13.
 */
public class Stock extends GenericJson {

    @Key
    private String code;
    public String getCode() {
        return this.code;
    }

    @Key
    private String name;
    public String getName() {
        return this.name;
    }

    @Key
    private String description;
    public String getDescription() {
        return this.description;
    }

    @Key
    private String imageUrl; // TODO: Get smaller bitmap for this field in fusion table record
    public String getImageUrl() {
        return this.imageUrl;
    }

    private Integer balance;
    public Integer getBalance() {
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
