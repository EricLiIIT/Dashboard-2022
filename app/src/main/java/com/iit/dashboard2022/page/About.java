package com.iit.dashboard2022.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iit.dashboard2022.R;

public class About extends Fragment implements Page {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_about_layout, container, false);
    }

    @NonNull
    @Override
    public String getTitle() {
        return "About";
    }

    @Override
    public void onPageChange(boolean enter) {

    }
}
