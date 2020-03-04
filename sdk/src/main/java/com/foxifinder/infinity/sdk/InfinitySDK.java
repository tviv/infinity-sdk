package com.foxifinder.infinity.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.foxifinder.infinity.IInfinityService;
import com.foxifinder.infinity.IInfinityServiceCallback;
import com.foxifinder.infinity.sdk.utils.AwatingTwoKeysStack;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class InfinitySDK {
    private static final String TAG = "Infinity SDK";

    /**
     * Denotes a successful operation.
     */
    public static final int SUCCESS = 0;
    /**
     * Denotes a generic operation failure.
     */
    public static final int ERROR = -1;

    public final Commands commands;

    private static final String INTENT_ACTION_INFINITY_SERVICE = "foxifinder.intent.action.INFINITY_SERVICE";
    private static final int INIT_TIMEOUT = 10000; //ms

    private final Context mContext;
    private OnInitListener mInitListener;
    private Connection mServiceConnection;
    private CountDownLatch mInitCompletedSignal = new CountDownLatch(1);
    private AwatingTwoKeysStack<CommandCallback> mCallbackStack = new AwatingTwoKeysStack<>();

    private final Object mSetLock = new Object();

    /**
     * Interface definition of a callback to be invoked indicating the completion of the
     * TextToSpeech engine initialization.
     */
    public interface OnInitListener {
        /**
         * Called to signal the completion of the InfinitySDK initialization.
         *
         * @param status {@link InfinitySDK#SUCCESS} or {@link InfinitySDK#ERROR}.
         */
        void onInit(int status);
    }


    /**
     * async constructor
     * @param context
     * @param listener
     */
    public InfinitySDK(Context context, OnInitListener listener) {
        mContext = context;
        mInitListener = listener;
        commands = new Commands(this);
        initSDK();
    }

    /**
     * sync constructor
     * @param context
     */
    public InfinitySDK(Context context) throws Exception {
        this(context.getApplicationContext(), null);

        mInitCompletedSignal.await(INIT_TIMEOUT, TimeUnit.MILLISECONDS);
        if (mServiceConnection == null) {
            throw new Exception("Infinity SDK init error");
        }
    }

    private void initSDK() {
        Connection connection = new Connection();
        Intent intent = new Intent(INTENT_ACTION_INFINITY_SERVICE);
        intent.setPackage("com.foxifinder.infinity.service");
        boolean bound = mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (!bound) {
            Log.e(TAG, "Failed to bind to infinity service");
            dispatchOnInit(ERROR);
        } else {
            Log.i(TAG, "Successfully bound to infinity service");
        }
    }

    private void dispatchOnInit(int result) {
        synchronized (mSetLock) {
            if (mInitListener != null) {
                mInitListener.onInit(result);
                mInitListener = null;
            }
            if (result == ERROR) shutdown();

            if (mInitCompletedSignal.getCount() > 0) mInitCompletedSignal.countDown();
        }
    }

    public boolean isConnected() {return mServiceConnection != null;}

    /**
     * Releases the resources used by the Infinity SDK.
     * It is good practice for instance to call this method in the onDestroy() method of an Activity
     * so the Infinity SDK can be cleanly stopped.
     */
    public void shutdown() {
        // Special case, we are asked to shutdown connection that did finalize its connection.
        synchronized (mSetLock) {
            if (mServiceConnection != null) {
                mContext.unbindService(mServiceConnection);
                mServiceConnection = null;
            }
        }
    }

    private interface Action<R> {
        R run(IInfinityService service) throws RemoteException;
    }

    private <R> R runAction(Action<R> action, R errorResult, String method) {
        synchronized (mSetLock) {
            if (mServiceConnection == null) {
                Log.w(TAG, method + " failed: not bound to Infinity Service");
                initSDK();
                return errorResult;
            }
            try {
                return action.run(mServiceConnection.getService());
            } catch (Exception e) {
                Log.e(TAG, method + " failed", e);
                return errorResult;
            }
        }
    }

    //functions of SDK Service
    int runCommand(final String command, final String params, final CommandCallback cb, final boolean repeated) {
        int result = runAction(new Action<Integer>() {
            @Override
            public Integer run(IInfinityService service) throws RemoteException {
                int id = service.runCommand(command, params);
                if (id > 0 && cb != null) {
                    synchronized (mCallbackStack) {
                        mCallbackStack.push(command, id, cb, repeated);
                        Log.d(TAG, String.format("cb pushed for %d", id));
                    }

                }

                return id;
            }
        }, ERROR, "runCommand");

        if (result == ERROR) {
            if (cb != null) {
                cb.onError("SDK inner error!", null);
            }
        }
        return result;
    }

    public String getServiceVersion() {
        return runAction(new Action<String>() {
            @Override
            public String run(IInfinityService service) throws RemoteException {
                return service.getVersion();
            }
        }, null, "getVersion");
    }

    public interface CommandCallback {
        void onSuccess(String descrtiption, Bundle params);
        void onError(String error, Bundle params);
    }

    private IInfinityServiceCallback mCallback = new IInfinityServiceCallback.Stub() {

        @Override
        public void onCommandEvent(int commandId, int eventType, String description, Bundle params)  {
            Log.d(TAG, String.format("callback for: %d, status; %s, descr: %s", commandId, eventType, description));

            synchronized (mCallbackStack) {
                CommandCallback cb = mCallbackStack.pop(commandId);
                Log.d(TAG, String.format("id = %d and cb is %s", commandId, (cb !=null ? "1" : "0")));
                if (cb != null) {
                    if (eventType == IInfinityServiceCallback.EVENT_TYPE_DONE)
                        cb.onSuccess(description, params);

                    if (eventType == IInfinityServiceCallback.EVENT_TYPE_ERROR)
                        cb.onError(description, params);
                }
            }
        }
    };

    private class Connection implements ServiceConnection {
        private IInfinityService mService;

        IInfinityService getService() { return mService;}

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (mSetLock) {
                Log.i(TAG, "Connected to " + name);

                try {
                    mService = IInfinityService.Stub.asInterface(service);
                    mService.registerCallback(mCallback);
                    mServiceConnection = Connection.this;

                    dispatchOnInit(SUCCESS);
                } catch (RemoteException e) {
                    dispatchOnInit(ERROR);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Asked to disconnect from " + name);
            dispatchOnInit(ERROR);
        }
    }

}