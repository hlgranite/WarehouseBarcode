package com.hlgranite.warehousescanner;

import java.util.HashMap;
import java.util.Map;

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

    private Map<Barcode, Integer> items;
    public Map<Barcode, Integer> getItems() {
        return items;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    private Integer balance;
    public Integer getBalance() {

        // Calculate balance
        this.balance = 0;
        this.area = new Area();
        this.items = new HashMap<Barcode, Integer>();
        for(Shipment shipment: dataStore.getShipments()) {
            Barcode barcode = shipment.getBarcode();
            if(barcode.getStockCode().equals(this.code)) {
                int qty = shipment.getQuantity();
                if(this.items.containsKey(barcode)) {
                    Barcode old = getBarcodeItem(barcode);
                    old.setLastUpdated(shipment.getDate());

                    int oldQty = items.get(barcode);
                    this.items.remove(barcode);
                    this.items.put(old,qty+oldQty);
                } else {
                    barcode.setLastUpdated(shipment.getDate());
                    this.items.put(barcode, qty);
                }

                this.balance += qty;
                long totalArea = qty * barcode.getWidth() * barcode.getLength();
                area.add(totalArea);
            }
        }
        for(WorkOrder workOrder: dataStore.getWorkOrders(0)) {
            if(workOrder.getBarcode().getStockCode().equals(this.code)) {
                Barcode barcode = workOrder.getBarcode();
                if(this.items.containsKey(barcode)) {
                    int oldQty = items.get(barcode);
                    oldQty--;

                    Barcode old = getBarcodeItem(barcode);
                    old.setLastUpdated(workOrder.getDate());
                    this.items.remove(barcode);
                    this.items.put(old,oldQty);

                    this.balance --;
                    long totalArea = barcode.getWidth() * barcode.getLength();
                    this.area.deduct(totalArea);
                }

                // if not contains in item meaning user checkout wrong barcode
                // all deduct quantity must be a valid barcode.
            }
        }

        return this.balance;
    }

    private Barcode getBarcodeItem(Barcode barcode) {
        for(Barcode item: this.items.keySet()) {
            if(item.equals(barcode)) return item;
        }

        return null;
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
        this.items = new HashMap<Barcode, Integer>();
    }

    @Override
    public String toString() {
        return this.code;
    }
}
