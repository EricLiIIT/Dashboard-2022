package com.iit.dashboard2022.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iit.dashboard2022.R;

public class Map extends Fragment implements Page {

    MapView mapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_layout, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }
    // https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    // https://developers.google.com/maps/documentation/android-sdk/location#:~:text=If%20your%20app%20needs%20to,location%20returned%20by%20the%20API.
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

        String TAG = "Logging";
        TextView coordinate_number;
        int numbers = 0; // placeholder for receiving data


        // Start new activity to show map
//        Button map_btn = view.findViewById(R.id.show_map);
//        map_btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent logDetailIntent = new Intent(getContext().getApplicationContext(), PolyActivity.class);
//                startActivity(logDetailIntent);
//                Log.i(TAG, "Displaying Map");
//            }
//        });

        // On receiving data, update textview
//        TextView data = view.findViewById(R.id.coordinate_number);
//        data.setText(numbers);

//        return rootView;


    @NonNull
    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onPageChange(boolean enter) {

    }

//    @Override
//    public void testUI(float percent) {
//
//    }
}