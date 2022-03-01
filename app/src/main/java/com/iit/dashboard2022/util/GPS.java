package com.iit.dashboard2022.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class GPS {
    public ArrayList getCoordinates(String message) {
        // parse sentence for coordinates & convert to array list of lat and lang
        return coordinates;
    }

    public LatLng coordinateCreation(ArrayList coordinates) {
        LatLng latLang = new LatLng(coordinates.indexOf(0), coordinates.indexOf(1));
        return latLang;
    }
}
