package com.qa.perf.emmageeplus;

import com.qa.perf.emmageeplus.email.MailSender;

import junit.framework.Assert;

import org.junit.Test;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTest {
    @Test
    public void mailSenderTest() {
        Assert.assertTrue(MailSender.sendTextMail("EmmageePlus@126.com", "Lwfwind789", "smtp.126.com",
                "Emmagee Performance Test Report", "see attachment",
                null, new String[]{"wind@abc360.com"}));
    }
}