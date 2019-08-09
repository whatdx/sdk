package com.lib.jsdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.lib.jsdk.utils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateInfoAppAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String appName;
    private String linkIcon;

    public UpdateInfoAppAsyncTask(Context context, String appName, String linkIcon) {
        this.context = context;
        this.appName = appName;
        this.linkIcon = linkIcon;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String jsonString;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL("https://gc652ktbul.execute-api.us-east-2.amazonaws.com/demo-sdk/update-application");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(10000);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            String params = "{\"package_name\": \"" + context.getPackageName() + "\",\"app_name\": \"" + appName + "\",\"icon_app\": \"" + linkIcon + "\" }";
//            String params = "{\"package_name\": \"com.facebook.katana\",\"app_name\": \"" + appName + "\",\"icon_app\": \"" + linkIcon + "\" }";
            writer.write(params);

            writer.flush();
            writer.close();
            os.close();

            con.connect();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonString = response.toString();
        LogUtils.d("update: " + jsonString);
        return null;
    }
}
