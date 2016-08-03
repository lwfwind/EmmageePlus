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
package com.qa.perf.emmageeplus.bean;

import android.graphics.drawable.Drawable;

/**
 * details of installed processes ,including
 * icon,packagename,pid,uid,processname
 *
 * @author andrewleo
 */
public class Process implements Comparable<Process> {
    private Drawable icon;
    private String appName;
    private String packageName;
    private int pid;
    private int uid;

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Gets app name.
     *
     * @return the app name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Sets app name.
     *
     * @param appName the app name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets pid.
     *
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * Sets pid.
     *
     * @param pid the pid
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public int compareTo(Process arg0) {
        return (this.getAppName().compareTo(arg0.getAppName()));
    }
}
