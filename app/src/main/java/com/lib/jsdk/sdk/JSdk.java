package com.lib.jsdk.sdk;

import android.content.Context;

import com.lib.jsdk.BuildConfig;
import com.lib.jsdk.callback.OnRegisterListner;

public class JSdk {
    public static boolean DEBUG = BuildConfig.DEBUG;

    public void register(Context context, boolean debug, String mailDeveloper, String appName, String linkFirstOpen, OnRegisterListner onRegisterListner) {
        DEBUG = debug;
        SdkMethod.getInstance().firstOpen(context, mailDeveloper, appName, linkFirstOpen, onRegisterListner);
    }

    public void handlingMessages(Context context, String response) {
        SdkMethod.getInstance().handlingMessages(context, response);
    }
}
