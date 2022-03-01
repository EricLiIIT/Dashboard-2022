package com.iit.dashboard2022.page;

import static com.iit.dashboard2022.ecu.ECUMsgHandler.STATE.Button;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iit.dashboard2022.R;
import com.iit.dashboard2022.ui.UITester;

import org.w3c.dom.Text;

public class Map extends Page {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String TAG = "Logging";
        TextView coordinate_number;
        int numbers = 0; // placeholder for receiving data

        View view = inflater.inflate(R.layout.map_layout, container, false);

        // Start new activity to show map
        Button map_btn = view.findViewById(R.id.show_map);
        map_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent logDetailIntent = new Intent(getContext().getApplicationContext(), PolyActivity.class);
                startActivity(logDetailIntent);
                Log.i(TAG, "Displaying Map");
            }
        });

        // On receiving data, update textview
        TextView data = view.findViewById(R.id.coordinate_number);
//        data.setText(numbers);

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