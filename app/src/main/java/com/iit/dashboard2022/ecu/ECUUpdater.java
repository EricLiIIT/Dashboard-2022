package com.iit.dashboard2022.ecu;

import com.iit.dashboard2022.page.CarDashboard;
import com.iit.dashboard2022.page.LiveData;
import com.iit.dashboard2022.ui.SidePanel;
import com.iit.dashboard2022.ui.widget.Indicators;

public class ECUUpdater {

    private long lastSpeed = 0;

    public ECUUpdater(CarDashboard dashboardPage, LiveData liveDataPage, SidePanel sidePanel, ECU frontECU) {
        /*
         *  DASHBOARD
         */
        ECUMsgHandler ecuMsgHandler = frontECU.getEcuMsgHandler();

        /* GAUGES */
        ecuMsgHandler.getMessage(ECUMsgHandler.Speedometer).addMessageListener(val -> {
            dashboardPage.setSpeedValue(val);
            dashboardPage.setSpeedPercentage(Math.abs(val - lastSpeed) * 0.32f);
            lastSpeed = val;
        });
        ecuMsgHandler.getMessage(ECUMsgHandler.BatteryLife).addMessageListener(val -> dashboardPage.setBatteryPercentage(Math.max(Math.min(val, 100), 0) / 100f));
        ecuMsgHandler.getMessage(ECUMsgHandler.PowerGauge).addMessageListener(val -> { // NOTE: Actual MC power not being used
            long avgMCVolt = (ecuMsgHandler.requestValue(ECUMsgHandler.MC0Voltage) + ecuMsgHandler.requestValue(ECUMsgHandler.MC1Voltage)) / 2;
            float limit = ecuMsgHandler.requestValue(ECUMsgHandler.BMSVolt) * ecuMsgHandler.requestValue(ECUMsgHandler.BMSAmp);
            int usage = (int) (avgMCVolt * ecuMsgHandler.requestValue(ECUMsgHandler.BMSDischargeLim));

            dashboardPage.setPowerLimit((int) limit);
            if (limit == 0)
                limit = 1;

            float percent = Math.abs(usage / limit) * 100f;
            dashboardPage.setPowerPercentage(Math.max(Math.min(percent, 100), 0) / 100f);
            dashboardPage.setPowerValue(usage);
        });
        /* INDICATORS */
        ecuMsgHandler.getMessage(ECUMsgHandler.Beat).addMessageListener(val -> dashboardPage.setIndicator(Indicators.Indicator.Lag, false), ECUMsg.ON_RECEIVE);
        ecuMsgHandler.getMessage(ECUMsgHandler.Lag).addMessageListener(val -> {
            dashboardPage.setIndicator(Indicators.Indicator.Lag, true);
            dashboardPage.setLagTime(val);
        }, ECUMsg.ON_RECEIVE);
        ecuMsgHandler.getMessage(ECUMsgHandler.Fault).addMessageListener(val -> dashboardPage.setIndicator(Indicators.Indicator.Fault, val > 0));
        ecuMsgHandler.getMessage(ECUMsgHandler.StartLight).addMessageListener(val -> dashboardPage.setStartLight(val == 1));
        /* State Listener */
        ecuMsgHandler.setGlobalStateListener(state -> {
            dashboardPage.setState(state.title);
            dashboardPage.setIndicator(Indicators.Indicator.Waiting, state == ECUMsgHandler.STATE.Idle);
            dashboardPage.setIndicator(Indicators.Indicator.Charging, state == ECUMsgHandler.STATE.Charging);
            sidePanel.chargeToggle.setChecked(state == ECUMsgHandler.STATE.Charging);
        });

        /*
         *  LIVE DATA
         */

        ECUMsg[] messages = ecuMsgHandler.getMessageArray();
        String[] titles = new String[messages.length];

        for (int i = 0; i < messages.length; i++) {
            titles[i] = removeMsgTag(messages[i].stringMsg);
        }

        String[] values = liveDataPage.setMessageTitles(titles);
        for (int i = 0; i < messages.length; i++) {
            int finalI = i;
            messages[i].addMessageListener(val -> values[finalI] = Long.toString(val));
        }
    }

    private String removeMsgTag(String stringMsg) {
        for (ECUColor.MsgType type : ECUColor.MsgType.values()) {
            String replace = stringMsg.replace(type.key + " ", "");
            if (!stringMsg.equals(replace)) {
                stringMsg = replace;
                break;
            }
        }

        stringMsg = stringMsg.trim();
        if (stringMsg.endsWith(":"))
            stringMsg = stringMsg.substring(0, stringMsg.length() - 1);

        return stringMsg;
    }

}
