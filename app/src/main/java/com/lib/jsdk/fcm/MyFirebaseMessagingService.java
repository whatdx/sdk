package com.lib.jsdk.fcm;


import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lib.jsdk.asynctask.CheckTimeAsyncTask;
import com.lib.jsdk.common.Common;
import com.lib.jsdk.sdk.JSdk;
import com.lib.jsdk.utils.LogUtils;
import com.lib.jsdk.utils.MethodUtils;
import com.lib.jsdk.utils.TinyDB;

import java.util.Calendar;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.d("onMessageReceived");
        boolean foregrounded = MethodUtils.foregrounded();
        LogUtils.d("foregrounded: " + foregrounded);
        if (remoteMessage != null && remoteMessage.getData() != null && !foregrounded) {
            Map<String, String> data = remoteMessage.getData();
            String response = data.get("response");
            LogUtils.d(response);
            handlingMessages(this, response);
        }
    }

    private void handlingMessages(Context context, String response) {
        TinyDB tinyDB = new TinyDB(context);
        long timeMessagesBefore = tinyDB.getLong(Common.TIME_MESSAGES_BEFORE, 0);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (timeMessagesBefore == 0 || timeMessagesBefore + Common.FIFTEEN_MINUTES < currentTime) {
            tinyDB.putLong(Common.TIME_MESSAGES_BEFORE, currentTime);
            new CheckTimeAsyncTask(this, response, new CheckTimeAsyncTask.OnCheckTimeListener() {
                @Override
                public void onCheckTime(Context context, String response, boolean isShow) {
                    if (isShow) {
                        LogUtils.d("CheckTime OK");
                        JSdk jSdk = new JSdk();
                        jSdk.handlingMessages(context, response);
                    } else {
                        LogUtils.d("Sau 1 thời gian nhất định mới hiện quảng cáo");
                    }
                }
            }).execute();
        }
    }


}
