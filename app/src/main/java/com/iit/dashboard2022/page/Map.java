package com.iit.dashboard2022.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iit.dashboard2022.R;
import com.iit.dashboard2022.ui.UITester;

import org.w3c.dom.Text;

public class Map extends Page {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView coordinate_number;
        int numbers = 0; // placeholder for receiving data

        View view = (ViewGroup) inflater.inflate(R.layout.map_layout, container, false);
        // On receiving data, update textview
        TextView data = (TextView) getView().findViewById(R.id.coordinate_number);
        data.setText(numbers);

        return view;
    }

    @NonNull
    @Override
    public String getTitle() {
        return null;
    }

//    @Override
//    public void testUI(float percent) {
//
//    }
}