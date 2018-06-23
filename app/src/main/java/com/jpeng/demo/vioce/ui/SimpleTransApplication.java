package com.jpeng.demo.vioce.ui;

import android.app.Application;

import org.litepal.LitePalApplication;

/**
 * Created by fujiayi on 2017/10/18.
 */

public class SimpleTransApplication extends LitePalApplication {
    private DigitalDialogInput digitalDialogInput;


    public DigitalDialogInput getDigitalDialogInput() {
        return digitalDialogInput;
    }

    public void setDigitalDialogInput(DigitalDialogInput digitalDialogInput) {
        this.digitalDialogInput = digitalDialogInput;
    }
}
