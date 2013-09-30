package com.hlgranite.warehousescanner;

import java.util.Comparator;

/**
 * Created by yeang-shing.then on 10/1/13.
 */
public class QuantityComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock lhs, Stock rhs) {
        return lhs.getBalance().compareTo(rhs.getBalance());
    }
}
