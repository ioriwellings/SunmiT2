package com.sunmi.sunmit2demo.utils;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;

/**
 * Created by highsixty on 2018/3/7.
 * mail  gaolulin@sunmi.com
 */

public class ScreenManager {

    private final String TAG = ScreenManager.class.getSimpleName();
    public static ScreenManager manager = null;
    private Display[] displays = null;//屏幕数组

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (null == manager) {
            synchronized (ScreenManager.class) {
                if (null == manager) {
                    manager = new ScreenManager();
                }
            }
        }
        return manager;
    }

    public void init(Context context) {
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        Log.d(TAG, "init: ----------->" + displays.length);
    }

    public Display[] getDisplays() {
        return displays;
    }
}
