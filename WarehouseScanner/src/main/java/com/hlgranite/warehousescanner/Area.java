package com.hlgranite.warehousescanner;

/**
 * Created by yeang-shing.then on 9/29/13.
 */
public class Area {
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
        return output + "m" + '\u00B2';
    }

    public String toSquareFeet() {
        double inchRatio = 25.4;
        inchRatio = inchRatio*inchRatio;
        double feetRatio = 12;
        feetRatio = feetRatio*feetRatio;

        double output = area/inchRatio/feetRatio;
        return output + "f" + '\u00B2';
    }
}
