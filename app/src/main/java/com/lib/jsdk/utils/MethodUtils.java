package com.lib.jsdk.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.lib.jsdk.common.Common;
import com.lib.jsdk.model.AppInstallSdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class MethodUtils {
    public static boolean isAppInstall(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = manager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : applicationInfos) {
            if (manager.getLaunchIntentForPackage(app.packageName) != null) {
                if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                } else {
                    if (app.packageName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<String> getAllApp(Context context) {
        ArrayList<String> listPackage = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = manager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : applicationInfos) {
            if (manager.getLaunchIntentForPackage(app.packageName) != null) {
                if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                } else {
                    listPackage.add(app.packageName);
                }
            }
        }
        return listPackage;
    }

    public static void intentGPlay(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void addAppSdkToList(Context context, String pka, long timeFirstOpen) {
        TinyDB tinyDB = new TinyDB(context);

        ArrayList<String> listAppSdk = tinyDB.getListString(Common.LIST_APP_SDK);
        listAppSdk.add(pka);
        tinyDB.putListString(Common.LIST_APP_SDK, listAppSdk);

        ArrayList<Long> listTime = tinyDB.getListLong(Common.LIST_TIME_FIRST_OPEN);
        listTime.add(timeFirstOpen);
        tinyDB.putListLong(Common.LIST_TIME_FIRST_OPEN, listTime);
    }

    public static void removeAppSdk(Context context, String pka, long timeFirstOpen) {
        TinyDB tinyDB = new TinyDB(context);
        try {
            ArrayList<String> listAppSdk = tinyDB.getListString(Common.LIST_APP_SDK);
            listAppSdk.remove(pka);
            tinyDB.putListString(Common.LIST_APP_SDK, listAppSdk);

            ArrayList<Long> listTime = tinyDB.getListLong(Common.LIST_TIME_FIRST_OPEN);
            listTime.remove(timeFirstOpen);
            tinyDB.putListLong(Common.LIST_TIME_FIRST_OPEN, listTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<AppInstallSdk> getListAppSdk(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<AppInstallSdk> appInstallSdks = new ArrayList<>();
        ArrayList<String> listAppSdk = tinyDB.getListString(Common.LIST_APP_SDK);
        ArrayList<Long> listTime = tinyDB.getListLong(Common.LIST_TIME_FIRST_OPEN);

        for (int i = 0; i < listAppSdk.size(); i++) {
            appInstallSdks.add(new AppInstallSdk(listAppSdk.get(i), listTime.get(i)));
        }

        Collections.sort(appInstallSdks);

        return appInstallSdks;
    }

    public static void sendBroadcastSdk(Context context, String action, String pkaAppReceiver, long timeFirstOpen) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.setComponent(new ComponentName(pkaAppReceiver, "com.lib.jsdk.broadcast.InsertAppSdkReceiver"));
            intent.putExtra(Common.PACKAGE_NAME, context.getPackageName());
            intent.putExtra(Common.TIME_FIRST_OPEN, timeFirstOpen);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.sendBroadcast(intent);
            LogUtils.d("okela: " + pkaAppReceiver);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("error: " + pkaAppReceiver);
        }
    }
}
