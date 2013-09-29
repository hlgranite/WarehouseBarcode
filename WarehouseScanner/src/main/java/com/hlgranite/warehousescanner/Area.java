package com.hlgranite.warehousescanner;

/**
 * Created by yeang-shing.then on 9/29/13.
 */
public class Area {

    /**
     * Square character.
     */
    private final char SQUARE = '\u00B2';

    private long area;
    public long getArea() {
        return this.area;
    }

    public Area() {
        area = 0;
    }

    /**
     * Add area.
     * @param area Value in mm²
     */
    public void add(long area) {
        this.area += area;
    }
    public void deduct(long area) {
        this.area -= area;
    }

    @Override
    public String toString() {
        double output = 0;
        output = area/1000000;
        return output + "m" + SQUARE;
    }

    public String toSquareFeet() {
//        double inchRatio = 25.4;
//        inchRatio = inchRatio*inchRatio;
//        double feetRatio = 12;
//        feetRatio = feetRatio*feetRatio;

        double rate = 1000/25.4;//inchRatio*feetRatio;
        rate = rate*rate;
        rate = rate/144;

        double output = area*rate/1000000;
        return String.format("%.1f%n", output) + "f" + SQUARE;
    }
}
