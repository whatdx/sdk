package com.lib.jsdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.lib.jsdk.common.Common;
import com.lib.jsdk.utils.MethodUtils;

import java.util.ArrayList;

public class SendBroadcastToAllAsyncTask extends AsyncTask<String, String, String> {

    private Context context;
    private long timeFirstOpen;

    public SendBroadcastToAllAsyncTask(Context context, long timeFirstOpen) {
        this.context = context;
        this.timeFirstOpen = timeFirstOpen;
    }

    @Override
    protected String doInBackground(String... strings) {
        ArrayList<String> allApps = MethodUtils.getAllApp(context);
        for (int i = 0; i < allApps.size(); i++) {
            String pka = allApps.get(i);
            if (!pka.contains("google")) {
                MethodUtils.sendBroadcastSdk(context, Common.ACTION_INSERT_NEW_APP_SDK, pka, timeFirstOpen);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
