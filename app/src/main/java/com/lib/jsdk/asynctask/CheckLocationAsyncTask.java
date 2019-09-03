package com.lib.jsdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.lib.jsdk.common.Common;
import com.lib.jsdk.utils.LogUtils;
import com.lib.jsdk.utils.TinyDB;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckLocationAsyncTask extends AsyncTask<String, String, String> {
    private String link = "http://ip-api.com/json";
    private Context context;

    public CheckLocationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String jsonString;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(link);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.connect();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            jsonString = response.toString();
            LogUtils.d("doInBackground: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);

            return jsonObject.getString("countryCode");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        LogUtils.d("countryCode: " + s);
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putString(Common.COUNTRY_CODE, s);
        if (!s.equals("")) {
            tinyDB.putBoolean(Common.IS_CHECK_LOCATION, true);
        }
    }
}
