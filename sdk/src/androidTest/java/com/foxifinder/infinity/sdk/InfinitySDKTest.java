package com.foxifinder.infinity.sdk;

import android.content.Context;
import android.os.Bundle;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class InfinitySDKTest {

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private InfinitySDK sdk = new InfinitySDK(appContext);

    public InfinitySDKTest() throws Exception {
    }

    @Test
    public void simpleCommandWithCallback() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        System.out.println(sdk.runCommand("DUMMY", "1", new InfinitySDK.CommandCallback() {
            @Override
            public void onSuccess(String descrtiption, Bundle params) {
                assertEquals("dummy success!", descrtiption);
                signal.countDown();
            }

            @Override
            public void onError(String error, Bundle params) {

            }
        }, false));

        System.out.println(sdk.runCommand("DUMMY", "1", null, false));
        System.out.println(sdk.runCommand("DUMMY", "1", null, false));

        signal.await(5, TimeUnit.SECONDS);
        assertEquals(0, signal.getCount());
    }

    @Test
    public void getServiceVersion() {
        assertEquals("1.0.0", sdk.getServiceVersion());
    }

    @Test
    public void setAirplaneMode() {
        //sdk.commands.setAirplaneMode(true, null);
    }


}