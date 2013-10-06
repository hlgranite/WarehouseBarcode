package com.hlgranite.warehousescanner;

/**
 * Unit class act like enum.
 * Created by yeang-shing.then on 9/30/13.
 */
public class Unit {

    public static final String Meter = "m";
    public static final String Feet = "ft";

    public static final String Mm = "mm";
    public static final String Inch = "\"";
    public static final String Piece = "pcs";

    public static final double InchRatio = 25.4;

    public static String toFeetLabel(double value) {
        //Log.i("INFO", "Convert "+value);
        String result = Double.toString(value) + Unit.Inch;
        int floor = (int)Math.floor(value/12d);
        //Log.i("INFO", "Ceiling: " + floor);
        double remainder = value - floor*12d;

        // TODO: Convert remainder to fraction number
        if(floor > 0) {
            result = floor + "' " + remainder + Unit.Inch;
        } else {
            result = value + Unit.Inch;
        }

        return result;
    }

    /**
     * TODO: Return nearer fraction value.
     * @param value
     * @return
     */
    public static String fraction(double value) {
        return "";
    }
}
