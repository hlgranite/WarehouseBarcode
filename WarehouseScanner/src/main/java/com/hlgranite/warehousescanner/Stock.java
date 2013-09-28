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
        this.area = new Area();
        for(Shipment shipment: dataStore.getShipments()) {
            if(shipment.getBarcode().getStockCode().equals(this.code)) {
                this.balance += shipment.getQuantity();
                area.add(shipment.getQuantity()*shipment.getBarcode().getWidth()*shipment.getBarcode().getLength());
            }
        }
        for(WorkOrder workOrder: dataStore.getWorkOrders()) {
            if(workOrder.getBarcode().getStockCode().equals(this.code)) {
                this.balance --;
                area.deduct(workOrder.getBarcode().getWidth()*workOrder.getBarcode().getLength());
            }
        }

        return this.balance;
    }
    private Area area;

    /**
     * Return total area of stock. Only contains value after called getBalance().
     * @return
     */
    public Area getArea() {
        return this.area;
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
