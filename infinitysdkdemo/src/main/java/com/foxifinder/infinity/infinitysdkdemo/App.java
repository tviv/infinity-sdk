package com.foxifinder.infinity.infinitysdkdemo;

import android.app.Application;
import android.widget.Toast;

import com.foxifinder.infinity.sdk.InfinitySDK;

public class App extends Application {

    private InfinitySDK mSdk;

    @Override
    public void onCreate() {
        super.onCreate();

        requestToConnect();
    }

    public void requestToConnect() {
        mSdk = new InfinitySDK(this, new InfinitySDK.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == InfinitySDK.ERROR) {
                    mSdk = null;
                    Toast.makeText(App.this,
                            "Infinity SDK init error!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public InfinitySDK getSdk() {
        if (mSdk == null || !mSdk.isConnected()) {
            requestToConnect();
        }

        return mSdk;
    }
}
