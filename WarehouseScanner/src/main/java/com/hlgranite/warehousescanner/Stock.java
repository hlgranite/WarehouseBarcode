package com.hlgranite.warehousescanner;

import java.util.ArrayList;

/**
 * Created by yeang-shing.then on 9/18/13.
 */
public class Stock {

    protected FusionManager dataStore = FusionManager.getInstance();

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

    private String imageUrl;

    /**
     * TODO: Get smaller bitmap for this field in fusion table record to reduce loading time.
     * @return
     */
    public String getImageUrl() {
        return this.imageUrl;
    }

    private Integer balance;
    public Integer getBalance() {

        // Calculate balance
        this.balance = 0;
        for(Shipment shipment: dataStore.getShipments()) {
            if(shipment.getBarcode().getStockCode().equals(this.code)) {
                this.balance += shipment.getQuantity();
            }
        }
        for(WorkOrder workOrder: dataStore.getWorkOrders()) {
            if(workOrder.getBarcode().getStockCode().equals(this.code)) {
                this.balance --;
            }
        }

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
