package com.hlgranite.warehousescanner;

import java.math.BigDecimal;

/**
 * Created by yeang-shing.then on 9/29/13.
 */
public class Area {

    /**
     * Square character.
     */
    public static final char SQUARE = '\u00B2';

    private long value;
    public long getValue() {
        return this.value;
    }

    public Area() {
        this.value = 0;
    }
    public Area(long area) {
        this.value = area;
    }

    /**
     * Add area.
     * @param area Value in mm².
     */
    public void add(long area) {
        this.value += area;
    }

    /**
     * Remove area
     * @param area Value in mm².
     */
    public void deduct(long area) {
        this.value -= area;
    }

    @Override
    public String toString() {
        // result = 16800000 / 1000000 will produce integer value 16.0
        // HACK: result = 16800000 / (double)1000000 will produce correct decimal place 16.8
        double result = value / (double)1000000;
        return round(result,2) + Unit.Meter + SQUARE;
    }

    /**
     * Convert area to square feet unit.
     * @return
     */
    public String toSquareFeet() {
        //see http://docs.oracle.com/javase/tutorial/java/data/numberformat.html
        //String.format("%.2f%n", output) + "ft" + SQUARE;
        double rate = 25.4*25.4*12*12;
        double result = value/rate;
        return round(result,2) + Unit.Feet + SQUARE;
    }

    /**
     * Round up value to desired decimal places.
     * See http://stackoverflow.com/questions/2808535/round-a-double-to-2-significant-figures-after-decimal-point
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if(places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
