// IInfinityService.aidl
package com.foxifinder.infinity;

import com.foxifinder.infinity.IInfinityServiceCallback;

// Declare any non-default types here with import statements

interface IInfinityService {
    String getVersion();

    void registerCallback(IInfinityServiceCallback cb);

    void unregisterCallback(IInfinityServiceCallback cb);

    int runCommand(String command, String params);

    int runNetCommand(String command);

}
