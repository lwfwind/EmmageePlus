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
package com.qa.perf.emmageeplus.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.qa.perf.emmageeplus.bean.Process;

/**
 * get information of processes
 *
 * @author andrewleo
 */
public class ContextHelper {

    private static final String LOG_TAG = "EmmageePlus-"
            + ContextHelper.class.getSimpleName();

    private static final String PACKAGE_NAME = "com.qa.perf.emmageeplus";
    private static final int ANDROID_M = 22;

    /**
     * get top activity name
     *
     * @param context context of activity
     * @return top activity name
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // Note: getRunningTasks is deprecated in API 21(Official)
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null)
            return (runningTaskInfos.get(0).topActivity).toString();
        else
            return null;
    }

    /**
     * get information of all running processes,including package name ,process
     * name ,icon ,pid and uid.
     *
     * @param context context of activity
     * @return running processes list
     */
    public List<Process> getRunningProcess(Context context) {
        Log.i(LOG_TAG, "get running processes");
        List<Process> progressList = new ArrayList<Process>();
        PackageManager pm = context.getPackageManager();

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ApplicationInfo appinfo : getPackagesInfo(context)) {
            Process process = new Process();
            if (((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                    || ((appinfo.processName != null) && (appinfo.processName
                    .equals(PACKAGE_NAME)))) {
                continue;
            }
            for (RunningAppProcessInfo runningProcess : run) {
                if ((runningProcess.processName != null)
                        && runningProcess.processName
                        .equals(appinfo.processName)) {
                    process.setPid(runningProcess.pid);
                    process.setUid(runningProcess.uid);
                    break;
                }
            }
            process.setPackageName(appinfo.processName);
            process.setAppName(appinfo.loadLabel(pm).toString());
            process.setIcon(appinfo.loadIcon(pm));
            progressList.add(process);
        }
        Collections.sort(progressList);
        return progressList;
    }

    /**
     * get pid by package name
     *
     * @param context     context of activity
     * @param packageName the package name
     * @return pid pid by package name
     */
    public int getPidByPackageName(Context context, String packageName) {
        Log.i(LOG_TAG, "start getLaunchedPid");
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // Note: getRunningAppProcesses return itself in API 22
        if (Build.VERSION.SDK_INT < ANDROID_M) {
            List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
            for (RunningAppProcessInfo runningProcess : run) {
                if ((runningProcess.processName != null)
                        && runningProcess.processName.equals(packageName)) {
                    return runningProcess.pid;
                }
            }
        } else {
            Log.i(LOG_TAG, "use top command to get pid");
            try {
                java.lang.Process p = Runtime.getRuntime().exec("top -m 100 -n 1");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(packageName)) {
                        line = line.trim();
                        String[] splitLine = line.split("\\s+");
                        if (packageName.equals(splitLine[splitLine.length - 1])) {
                            return Integer.parseInt(splitLine[0]);
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * get information of all installed packages
     *
     * @param context context of activity
     * @return all installed packages
     */
    public List<Process> getAllPackages(Context context) {
        Log.i(LOG_TAG, "getAllPackages");
        List<Process> progressList = new ArrayList<Process>();
        PackageManager pm = context.getPackageManager();
        for (ApplicationInfo appinfo : getPackagesInfo(context)) {
            Process process = new Process();
            if (((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                    || ((appinfo.processName != null) && (appinfo.processName
                    .equals(PACKAGE_NAME)))) {
                continue;
            }
            process.setPackageName(appinfo.packageName);
            process.setAppName(appinfo.loadLabel(pm).toString());
            process.setIcon(appinfo.loadIcon(pm));
            progressList.add(process);
        }
        Collections.sort(progressList);
        return progressList;
    }

    /**
     * get information of all applications.
     *
     * @param context context of activity
     * @return packages information of all applications
     */
    private List<ApplicationInfo> getPackagesInfo(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    /**
     * get pid by package name
     *
     * @param context     context of activity
     * @param packageName package name of monitoring app
     * @return pid process by package name
     */
    public Process getProcessByPackageName(Context context, String packageName) {
        if (Build.VERSION.SDK_INT < ANDROID_M) {
            List<Process> processList = getRunningProcess(context);
            for (Process process : processList) {
                if ((process.getPackageName() != null)
                        && (process.getPackageName().equals(packageName))) {
                    return process;
                }
            }
        } else {
            Process process = new Process();
            int pid = getPidByPackageName(context, packageName);
            process.setPid(pid);
            process.setUid(0);
            return process;
        }
        return null;
    }
}
