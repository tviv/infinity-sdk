// IInfinityCallback.aidl
package com.foxifinder.infinity;

// Declare any non-default types here with import statements


//todo it will be re-done to more universal thing
interface IInfinityServiceCallback {
    const int EVENT_TYPE_DONE = 1;
    const int EVENT_TYPE_IN_PROCESS = 2;
    const int EVENT_TYPE_ERROR = -1;

    void onCommandEvent(int commandId, int eventType, String description, in Bundle params);
}
