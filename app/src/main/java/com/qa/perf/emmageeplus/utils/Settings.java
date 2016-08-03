package com.qa.perf.emmageeplus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Parameters in Setting Activity
 *
 * @author yrom
 */
public final class Settings {

    /**
     * The constant KEY_SENDER.
     */
    public static final String KEY_SENDER = "sender";
    /**
     * The constant KEY_PASSWORD.
     */
    public static final String KEY_PASSWORD = "password";
    /**
     * The constant KEY_RECIPIENTS.
     */
    public static final String KEY_RECIPIENTS = "recipients";
    /**
     * The constant KEY_SMTP.
     */
    public static final String KEY_SMTP = "smtp";
    /**
     * The constant KEY_ISFLOAT.
     */
    public static final String KEY_ISFLOAT = "isfloat";
    /**
     * The constant KEY_INTERVAL.
     */
    public static final String KEY_INTERVAL = "interval";
    /**
     * The constant KEY_ROOT.
     */
    public static final String KEY_ROOT = "root";
    /**
     * The constant KEY_AUTO_STOP.
     */
    public static final String KEY_AUTO_STOP = "autoStop";

    /**
     * Gets default shared preferences.
     *
     * @param context the context
     * @return the default shared preferences
     */
    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
