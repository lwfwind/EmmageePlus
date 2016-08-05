package com.qa.perf.emmageeplus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qa.perf.emmageeplus.activity.MainPageActivity;
import com.qa.perf.emmageeplus.service.EmmageeService;

/**
 * Created by kcgw001 on 2016/8/4.
 */
public class ServiceStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(EmmageeService.SERVICE_ACTION)) {
            boolean isServiceStop = intent.getExtras().getBoolean("isServiceStop");
            if (isServiceStop) {
                MainPageActivity.getInstance().updateBtnTestStatus();
            }
        }
    }
}
