package com.iit.dashboard2022.util;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GPS {
    public static String message (byte[] sentence) {

    }

    public static float[] getCoordinates(String message) {
        // parse sentence for coordinates & convert to array list of lat and lang
        // NMEA sentence structure:
        // https://www.rfwireless-world.com/Terminology/GPS-sentences-or-NMEA-sentences.htmlv

        String[] msg = message.split(","); // Divides string message into components
        float latitude = Float.parseFloat(msg[3]);
        float longitude = Float.parseFloat(msg[5]);
        float[] coordinates;
        coordinates = new float[2];
        coordinates[0] = latitude;
        coordinates[1] = longitude;

        return coordinates;
    }

    public static LatLng coordinateCreation(float[] coordinates) {
        return new LatLng(coordinates[0], coordinates[1]);
    }
}
