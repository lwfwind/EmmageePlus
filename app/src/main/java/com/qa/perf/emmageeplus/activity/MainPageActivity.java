/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.qa.perf.emmageeplus.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import com.qa.perf.emmageeplus.R;
import com.qa.perf.emmageeplus.adapter.ListViewAdapter;
import com.qa.perf.emmageeplus.receiver.ServiceStatusReceiver;
import com.qa.perf.emmageeplus.service.EmmageeService;
import com.qa.perf.emmageeplus.utils.ContextHelper;

import java.io.IOException;

/**
 * Main Page of Emmagee
 *
 * @author andrewleo
 */
public class MainPageActivity extends Activity {

    private static final String LOG_TAG = "EmmageePlus-" + MainPageActivity.class.getSimpleName();

    private static final int TIMEOUT = 20000;
    private static MainPageActivity instance;
    private ContextHelper contextHelper;
    private Intent monitorService;
    private ListView processListView;
    private Button btnTest;
    private int pid, uid;
    private boolean isServiceStop = false;
    private ServiceStatusReceiver receiver;

    private TextView nbTitle;
    private ImageView ivGoBack;
    private ImageView ivBtnSet;
    private LinearLayout layBtnSet;
    private Long mExitTime = (long) 0;

    public static MainPageActivity getInstance(){
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MainActivity::onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainpage);
        instance = this;
        initTitleLayout();
        contextHelper = new ContextHelper();
        btnTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                monitorService = new Intent();
                monitorService.setClass(MainPageActivity.this, EmmageeService.class);
                if (getString(R.string.start_test).equals(btnTest.getText().toString())) {
                    ListViewAdapter adapter = (ListViewAdapter) processListView.getAdapter();
                    if (adapter.checkedProcess != null) {
                        String packageName = adapter.checkedProcess.getPackageName();
                        String appName = adapter.checkedProcess.getAppName();
                        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                        String startActivity = "";
                        Log.d(LOG_TAG, packageName);
                        // clear logcat
                        try {
                            Runtime.getRuntime().exec("logcat -c");
                        } catch (IOException e) {
                            Log.d(LOG_TAG, e.getMessage());
                        }
                        try {
                            startActivity = intent.resolveActivity(getPackageManager()).getShortClassName();
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(MainPageActivity.this, getString(R.string.can_not_start_app_toast), Toast.LENGTH_LONG).show();
                            return;
                        }
                        waitForAppStart(packageName);
                        monitorService.putExtra("appName", appName);
                        monitorService.putExtra("pid", pid);
                        monitorService.putExtra("uid", uid);
                        monitorService.putExtra("packageName", packageName);
                        monitorService.putExtra("startActivity", startActivity);
                        startService(monitorService);
                        isServiceStop = false;
                        btnTest.setText(getString(R.string.stop_test));
                    } else {
                        Toast.makeText(MainPageActivity.this, getString(R.string.choose_app_toast), Toast.LENGTH_LONG).show();
                    }
                } else {
                    btnTest.setText(getString(R.string.start_test));
                    Toast.makeText(MainPageActivity.this, getString(R.string.test_result_file_toast) + EmmageeService.resultFilePath,
                            Toast.LENGTH_LONG).show();
                    stopService(monitorService);
                }
            }
        });
        processListView.setAdapter(new ListViewAdapter(this));
        processListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RadioButton rdBtn = (RadioButton) ((LinearLayout) view).getChildAt(0);
                rdBtn.setChecked(true);
            }
        });

        nbTitle.setText(getString(R.string.app_name));
        ivGoBack.setVisibility(ImageView.INVISIBLE);
        ivBtnSet.setImageResource(R.drawable.settings_button);
        layBtnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettingsActivity();
            }
        });
        receiver = new ServiceStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(EmmageeService.SERVICE_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initTitleLayout() {
        ivGoBack = (ImageView) findViewById(R.id.go_back);
        nbTitle = (TextView) findViewById(R.id.nb_title);
        ivBtnSet = (ImageView) findViewById(R.id.btn_set);
        processListView = (ListView) findViewById(R.id.processList);
        btnTest = (Button) findViewById(R.id.test);
        layBtnSet = (LinearLayout) findViewById(R.id.lay_btn_set);
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        if (isServiceStop) {
            btnTest.setText(getString(R.string.start_test));
        }
    }

    /**
     * wait for test application started.
     *
     * @param packageName package name of test application
     */
    private void waitForAppStart(String packageName) {
        Log.d(LOG_TAG, "wait for app start");
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + TIMEOUT) {
            pid = contextHelper.getPidByPackageName(getBaseContext(), packageName);
            if (pid != 0) {
                break;
            }
        }
    }

    /**
     * show a dialog when click return key.
     *
     * @return Return true to prevent this event from being propagated further,
     * or false to indicate that you have not handled this event and it
     * should continue to be propagated.
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.quite_alert, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                if (monitorService != null) {
                    Log.d(LOG_TAG, "stop service");
                    stopService(monitorService);
                }
                Log.d(LOG_TAG, "exit Emmagee");
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goToSettingsActivity() {
        Intent intent = new Intent();
        intent.setClass(MainPageActivity.this, SettingsActivity.class);
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void updateBtnTestStatus(){
        btnTest.setText(getString(R.string.start_test));
    }

}
