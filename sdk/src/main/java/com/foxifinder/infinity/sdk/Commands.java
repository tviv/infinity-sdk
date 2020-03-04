package com.foxifinder.infinity.sdk;

import android.os.Bundle;

public class Commands {
    private InfinitySDK sdk;
    Commands(InfinitySDK sdk) {
        this.sdk = sdk;
    }

    public void setAirplaneMode(boolean value, InfinitySDK.CommandCallback cb) {
        sdk.runCommand("AIRPL_MODE", (value ? "1" : "0"), cb, false);
    }

    public void setMobileDataEnabled(boolean value, InfinitySDK.CommandCallback cb) {
        sdk.runCommand("MOBILE_DATA", (value ? "1" : "0"), cb, false);
    }

}
