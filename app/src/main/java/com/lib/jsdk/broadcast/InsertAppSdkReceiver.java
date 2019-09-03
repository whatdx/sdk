package com.lib.jsdk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lib.jsdk.common.Common;
import com.lib.jsdk.utils.MethodUtils;
import com.lib.jsdk.utils.TinyDB;

public class InsertAppSdkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //todo: add app sdk đc cài trên máy vào list app sdk
        String action = intent.getAction();
        assert action != null;
        Log.d("datdb", "action: " + action);
        String pkaAppInstall = intent.getStringExtra(Common.PACKAGE_NAME);
        long timeFirstOpen = intent.getLongExtra(Common.TIME_FIRST_OPEN, 0);
        Log.d("datdb", "onReceive: " + pkaAppInstall);
        MethodUtils.addAppSdkToList(context, pkaAppInstall, timeFirstOpen);
        if (action.equals(Common.ACTION_INSERT_NEW_APP_SDK)) {
            if (!pkaAppInstall.equals(context.getPackageName())) {
                MethodUtils.sendBroadcastSdk(context, Common.ACTION_INSERT_OLD_APP_SDK, pkaAppInstall, new TinyDB(context).getLong(Common.TIME_FIRST_OPEN, 0));
            }
        }
    }
}
