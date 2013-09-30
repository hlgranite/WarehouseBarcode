package com.hlgranite.warehousescanner;

import java.util.Comparator;

/**
 * Created by yeang-shing.then on 10/1/13.
 */
public class AreaComparator implements Comparator<Stock> {

    @Override
    public int compare(Stock lhs, Stock rhs) {
        lhs.getBalance();
        Long area1 = lhs.getArea().getValue();

        rhs.getBalance();
        Long area2 = rhs.getArea().getValue();
        return area1.compareTo(area2);
    }
}
