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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qa.perf.emmageeplus.BaseActivity;
import com.qa.perf.emmageeplus.R;
import com.qa.perf.emmageeplus.utils.Settings;
import com.qa.perf.emmageeplus.utils.email.EncryptData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mail Setting Page of Emmagee
 *
 * @author andrewleo
 */
public class MailSettingsActivity extends BaseActivity {

    private static final String LOG_TAG = "Emmagee-" + MailSettingsActivity.class.getSimpleName();
    private static final String BLANK_STRING = "";

    private EditText edtRecipients;
    private EditText edtSender;
    private EditText edtPassword;
    private EditText edtSmtp;
    private String sender;
    private String prePassword, curPassword;
    private String recipients, smtp;
    private String[] receivers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mail_settings);

        final EncryptData des = new EncryptData();

        edtSender = (EditText) findViewById(R.id.sender);
        edtPassword = (EditText) findViewById(R.id.password);
        edtRecipients = (EditText) findViewById(R.id.recipients);
        edtSmtp = (EditText) findViewById(R.id.smtp);
        TextView title = (TextView) findViewById(R.id.nb_title);
        LinearLayout layGoBack = (LinearLayout) findViewById(R.id.lay_go_back);
        LinearLayout layBtnSet = (LinearLayout) findViewById(R.id.lay_btn_set);

        title.setText(R.string.mail_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sender = preferences.getString(Settings.KEY_SENDER, BLANK_STRING);
        prePassword = preferences.getString(Settings.KEY_PASSWORD, BLANK_STRING);
        recipients = preferences.getString(Settings.KEY_RECIPIENTS, BLANK_STRING);
        smtp = preferences.getString(Settings.KEY_SMTP, BLANK_STRING);

        edtRecipients.setText(recipients);
        edtSender.setText(sender);
        edtPassword.setText(prePassword);
        edtSmtp.setText(smtp);

        layGoBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MailSettingsActivity.this.finish();
            }
        });
        layBtnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sender = edtSender.getText().toString().trim();
                if (!BLANK_STRING.equals(sender) && !checkMailFormat(sender)) {
                    Toast.makeText(MailSettingsActivity.this, getString(R.string.sender_mail_toast) + "[" + sender + "]" + getString(R.string.format_incorrect_format),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                recipients = edtRecipients.getText().toString().trim();
                receivers = recipients.split("\\s+");
                for (String receiver : receivers) {
                    if (!BLANK_STRING.equals(receiver) && !checkMailFormat(receiver)) {
                        Toast.makeText(MailSettingsActivity.this,
                                getString(R.string.receiver_mail_toast) + "[" + receiver + "]" + getString(R.string.format_incorrect_format),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                curPassword = edtPassword.getText().toString().trim();
                smtp = edtSmtp.getText().toString().trim();
                if (checkMailConfig(sender, recipients, smtp, curPassword) == -1) {
                    Toast.makeText(MailSettingsActivity.this, getString(R.string.info_incomplete_toast), Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences preferences = Settings.getDefaultSharedPreferences(getApplicationContext());
                Editor editor = preferences.edit();
                editor.putString(Settings.KEY_SENDER, sender);

                try {
                    editor.putString(Settings.KEY_PASSWORD, curPassword.equals(prePassword) ? curPassword : des.encrypt(curPassword));
                } catch (Exception e) {
                    editor.putString(Settings.KEY_PASSWORD, curPassword);
                }
                editor.putString(Settings.KEY_RECIPIENTS, recipients);
                editor.putString(Settings.KEY_SMTP, smtp);
                editor.apply();
                Toast.makeText(MailSettingsActivity.this, getString(R.string.save_success_toast), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_FIRST_USER, intent);
                MailSettingsActivity.this.finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * check if mail configurations are available
     *
     * @return true: valid configurations
     */
    private int checkMailConfig(String sender, String recipients, String smtp, String curPassword) {
        if (!BLANK_STRING.equals(curPassword) && !BLANK_STRING.equals(sender) && !BLANK_STRING.equals(recipients) && !BLANK_STRING.equals(smtp)) {
            return 1;
        } else if (BLANK_STRING.equals(curPassword) && BLANK_STRING.equals(sender) && BLANK_STRING.equals(recipients) && BLANK_STRING.equals(smtp)) {
            return 0;
        } else
            return -1;
    }

    /**
     * check mail format
     *
     * @return true: valid email address
     */
    private boolean checkMailFormat(String mail) {
        String strPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(mail);
        return m.matches();
    }
}
