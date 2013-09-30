package com.hlgranite.warehousescanner;

import java.util.Comparator;

/**
 * Sort stock collection based on stock code alphabetically.
 * Created by yeang-shing.then on 10/1/13.
 */
public class StockCodeComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock lhs, Stock rhs) {
        return lhs.getCode().compareTo(rhs.getCode());
    }
}
