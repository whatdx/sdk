package com.lib.jsdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.lib.jsdk.callback.OnParseAppInfoListener;
import com.lib.jsdk.utils.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseAppInfoAsyncTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private String pka;
    private String appName = "";
    private String linkIcon = "";

    private OnParseAppInfoListener onParseAppInfoListener;

    public ParseAppInfoAsyncTask(Context context, String pka, OnParseAppInfoListener onParseAppInfoListener) {
        this.context = context;
        this.pka = pka;
        this.onParseAppInfoListener = onParseAppInfoListener;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + pka + "&hl=en").get();

            appName = document.select("meta[property='og:title']").attr("content");

            Elements elementsImage = document.select("img");
            for (int i = 0; i < elementsImage.size(); i++) {
                if (elementsImage.get(i).attr("abs:alt").contains("Cover art")) {
                    linkIcon = elementsImage.get(i).attr("abs:src");
                    break;
                }
            }
            LogUtils.d("image: " + linkIcon + "\nappName: " + appName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (onParseAppInfoListener != null && !appName.equals("") && !linkIcon.equals("")) {
            onParseAppInfoListener.onParseSuccess(context, linkIcon, appName);
        }
    }
}
