package com.iit.dashboard2022;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iit.dashboard2022.ecu.ECU;
import com.iit.dashboard2022.ecu.ECUUpdater;
import com.iit.dashboard2022.page.CarDashboard;
import com.iit.dashboard2022.page.Commander;
import com.iit.dashboard2022.page.Errors;
import com.iit.dashboard2022.page.LiveData;
import com.iit.dashboard2022.page.Logs;
import com.iit.dashboard2022.page.Map;
import com.iit.dashboard2022.page.Page;
import com.iit.dashboard2022.page.PageManager;
import com.iit.dashboard2022.page.Pager;
import com.iit.dashboard2022.ui.SidePanel;
import com.iit.dashboard2022.ui.widget.SettingsButton;
import com.iit.dashboard2022.ui.widget.WidgetUpdater;
import com.iit.dashboard2022.ui.widget.console.ConsoleWidget;
import com.iit.dashboard2022.util.LogFileIO;
import com.iit.dashboard2022.util.Toaster;

public class MainActivity extends AppCompatActivity {

    SidePanel sidePanel;
    Pager mainPager;

    ECUUpdater ecuUpdater;
    ECU frontECU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        Toaster.setEnabled(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();

        /* INITIALIZE */
        frontECU = new ECU(this);
        mainPager = new Pager(this);
        Toaster.setContext(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        ConsoleWidget console = findViewById(R.id.console);
        SupportMapFragment map;

        /* PAGER */
        CarDashboard cdPage = (CarDashboard) mainPager.getPage(PageManager.DASHBOARD);
        LiveData ldPage = (LiveData) mainPager.getPage(PageManager.LIVEDATA);
        Errors errorsPage = (Errors) mainPager.getPage(PageManager.ERRORS);
        Commander commandPage = (Commander) mainPager.getPage(PageManager.COMMANDER);
        commandPage.setECU(frontECU);
        cdPage.setECU(frontECU);
        Logs logPage = (Logs) mainPager.getPage(PageManager.LOGS);

        /* SIDE PANEL */
        sidePanel = findViewById(R.id.sidePanel);
        sidePanel.attach(this, console, cdPage, ldPage, errorsPage, frontECU);

        /* SETTINGS BUTTON */
        SettingsButton settingsBtn = findViewById(R.id.settingsBtn);

        mainPager.setOnTouchCallback(settingsBtn::performClick);
        settingsBtn.setCallbacks(
                () -> {
                    mainPager.pushMargin(Pager.RIGHT, (int) -sidePanel.sidePanelDrawerAnim.reverse());
                    WidgetUpdater.post();
                },
                () -> {
                    mainPager.pushMargin(Pager.RIGHT, (int) -sidePanel.sidePanelDrawerAnim.start());
                    sidePanel.consoleSwitch.setActionedCheck(false);
                    WidgetUpdater.post();
                },
                locked -> {
                    mainPager.setUserInputEnabled(!locked);
                    sidePanel.consoleSwitch.setActionedCheck(false);
                    WidgetUpdater.post();
                }
        );

        new Handler(Looper.myLooper()).postDelayed(() -> {
            /* FINAL CALLS */
            ecuUpdater = new ECUUpdater(cdPage, ldPage, sidePanel, frontECU);
            frontECU.setJSONLoadListener(() -> logPage.displayFiles(frontECU.getLocalLogs().toArray(new LogFileIO.LogFile[0])));

            if (!frontECU.loadJSONFromSystem()) {
                Toaster.showToast("No JSON is currently loaded", Toaster.WARNING);
            } else {
                frontECU.open();
            }

            cdPage.reset();

            logPage.attachConsole(console, () -> settingsBtn.post(() -> {
                if (settingsBtn.isLocked())
                    settingsBtn.performLongClick();
                settingsBtn.setActionedCheck(true);
                sidePanel.consoleSwitch.setActionedCheck(true);
            }));
            WidgetUpdater.start();
//            Toaster.showToast("Initialized", Toaster.INFO, Toast.LENGTH_SHORT, Gravity.START);
            Toaster.setEnabled(true);
        }, 500);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        if (sidePanel != null)
            sidePanel.onLayoutChange();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideUI();
    }

    private void setupUI() {
        // Account for notches in newer phones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        hideUI();
    }

    private void hideUI() {
        // Hide Action bar and navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}