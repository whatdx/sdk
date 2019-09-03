package com.lib.jsdk.fcm;


import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lib.jsdk.asynctask.CheckTimeAsyncTask;
import com.lib.jsdk.model.AppInstallSdk;
import com.lib.jsdk.sdk.JSdk;
import com.lib.jsdk.utils.LogUtils;
import com.lib.jsdk.utils.MethodUtils;

import java.util.ArrayList;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.d("onMessageReceived");
        boolean foregrounded = MethodUtils.foregrounded();
        LogUtils.d("foregrounded: " + foregrounded);
        if (remoteMessage != null && remoteMessage.getData() != null && !foregrounded) {

            //todo: check xem app của mh có phải là app hiển thị qc hay không
            ArrayList<AppInstallSdk> listAppSdk = MethodUtils.getListAppSdk(this);
            int size = listAppSdk.size();
            LogUtils.d("size: " + size);
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    AppInstallSdk appInstallSdk = listAppSdk.get(i);
                    String pkaCheck = listAppSdk.get(i).getPackageName();
                    //todo: kiểm tra xem app đấy có đc cài không
                    if (MethodUtils.isAppInstall(this, pkaCheck)) {
                        //todo: nếu đc cài thì kiểm tra xem có cùng pka của app ko
                        if (pkaCheck.equals(getPackageName())) {
                            //todo: nếu cùng thì show qc
                            LogUtils.d("onMessageReceived: " + appInstallSdk.getPackageName());
                            getData(remoteMessage);
                        }
                        break;
                    } else {
                        //todo: nếu app k ddc cài (đã bị gỡ) thì remove khỏi list
                        MethodUtils.removeAppSdk(this, appInstallSdk.getPackageName(), appInstallSdk.getFirstOpen());
                    }
                }
            } else {
                getData(remoteMessage);
            }
        }
    }

    private void getData(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String response = data.get("response");
        LogUtils.d(response);
        handlingMessages(response);
    }

    private void handlingMessages(String response) {
//        TinyDB tinyDB = new TinyDB(context);
//        long timeMessagesBefore = tinyDB.getLong(Common.TIME_MESSAGES_BEFORE, 0);
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        if (timeMessagesBefore == 0 || timeMessagesBefore + Common.FIFTEEN_MINUTES < currentTime) {
//            tinyDB.putLong(Common.TIME_MESSAGES_BEFORE, currentTime);
        new CheckTimeAsyncTask(this, response, new CheckTimeAsyncTask.OnCheckTimeListener() {
            @Override
            public void onCheckTime(Context context, String response, boolean isShow) {
                if (isShow) {
                    //todo: check location có trong blacklist k nếu không thì mới show qc
                    LogUtils.d("CheckTime OK");
                    JSdk jSdk = new JSdk();
                    jSdk.handlingMessages(context, response);
                } else {
                    LogUtils.d("Sau 1 thời gian nhất định mới hiện quảng cáo");
                }
            }
        }).execute();
//        }
    }


}
