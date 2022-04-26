package com.iit.dashboard2022.page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iit.dashboard2022.R;
import com.iit.dashboard2022.util.GPS;

public class Map extends Page implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment map;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_layout, container, false);
        map = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        return rootView;
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Map";
    }

    @Override
    public void onPageChange(boolean enter) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-29.501, 119.700)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-29.501, 119.700),
                        new LatLng(-27.456, 119.672),
                        new LatLng(-28.081, 126.555),
                        new LatLng(-28.848, 124.229),
                        new LatLng(-28.215, 123.938),
                        new LatLng(-28.332, 122.938),
                        new LatLng(-29.501, 119.700)
                        )
                );
    }
}
