package com.iit.dashboard2022.page;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface Page {

    public abstract @NonNull
    String getTitle();

    public void onPageChange(boolean enter);

}
