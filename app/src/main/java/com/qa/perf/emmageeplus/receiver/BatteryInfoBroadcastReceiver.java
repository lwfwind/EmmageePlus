package com.qa.perf.emmageeplus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.qa.perf.emmageeplus.service.EmmageeService;

/**
 * Created by kcgw001 on 2016/8/4.
 */
public class BatteryInfoBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            String totalBatt = String.valueOf(level * 100 / scale);
            String voltage = String.valueOf(intent.getIntExtra(
                    BatteryManager.EXTRA_VOLTAGE, -1) * 1.0 / 1000);
            String temperature = String.valueOf(intent.getIntExtra(
                    BatteryManager.EXTRA_TEMPERATURE, -1) * 1.0 / 10);
            EmmageeService.getInstance().updateBatteryInfo(totalBatt, voltage, temperature);
        }
    }
}
