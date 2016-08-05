package com.qa.perf.emmageeplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static Activity curActivity;
    protected boolean destroyed;
    private String name = this.getClass().getSimpleName() + "";

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showMessage(int msgId) {
        Toast.makeText(getApplicationContext(), msgId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, name + "->onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, name + "->onCreate savedInstanceState=" + savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, name + "->onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, name + "->onStart");
        super.onStart();
        curActivity = this;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, name + "->onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, name + "->onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, name + "->onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, name + "->onDestroy");
        super.onDestroy();
        destroyed = true;
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d(TAG, name + "->onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, name + "->onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, name + "->onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, name + "->onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, name + "->onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }
}
