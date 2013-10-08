package com.hlgranite.warehousescanner;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
    private static final double EIGHTH = 0.125;
    private static final char EIGHTH_CHAR = '\u215B';
    private static final char EIGHTH2_CHAR = '\u00BC';
    private static final char EIGHTH3_CHAR = '\u215C';
    private static final char EIGHTH4_CHAR = '\u00BD';
    private static final char EIGHTH5_CHAR = '\u215D';
    private static final char EIGHTH6_CHAR = '\u00BE';
    private static final char EIGHTH7_CHAR = '\u215E';

    public static String LENGTH_FORMAT = "%04d";
    public static SimpleDateFormat DateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Convert mm to feet conversion.
     * @param value
     * @return
     */
    public static String toFeetLabel(double value) {
        String result = Double.toString(value) + Unit.Inch;
        int floor = (int)Math.floor(value/12d);
        double remainder = Area.round(value - floor*12d, 3);

        // Convert remainder to fraction number
        if(floor > 0) {
            int inches = (int)remainder;
            double digits = remainder - inches;
            result = floor + "' " + inches + fraction(digits) + Unit.Inch;
        } else {
            int inches = (int)value;
            double digits = value - inches;
            result = inches + fraction(digits) + Unit.Inch;
        }

        return result;
    }

    /**
     * Return nearer fraction value.
     * @param value Double value less than 1.
     * @return
     */
    public static char fraction(double value) {
        char result = '\0';
        char[] eights = new char[]{EIGHTH_CHAR, EIGHTH2_CHAR, EIGHTH3_CHAR, EIGHTH4_CHAR, EIGHTH5_CHAR, EIGHTH6_CHAR, EIGHTH7_CHAR, '\0'};

        for(int i=eights.length;i>=0;i--) {
            if(value > i*EIGHTH) {
                if(value - i*EIGHTH > (i+1)*EIGHTH - value) {
                    //Log.i("INFO", "Round " + value + " to " + eights[i + 1]);
                    return eights[i];
                } else {
                    //Log.i("INFO", "Round " + value + " to " + eights[i]);
                    return eights[i+1];
                }
            }
        }

        return result;
    }

}
