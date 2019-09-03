package com.lib.jsdk.model;

public class AppInstallSdk implements Comparable<AppInstallSdk> {
    private String packageName;
    private long firstOpen;

    public AppInstallSdk() {
    }

    public AppInstallSdk(String packageName, long firstOpen) {
        this.packageName = packageName;
        this.firstOpen = firstOpen;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getFirstOpen() {
        return firstOpen;
    }

    public void setFirstOpen(long firstOpen) {
        this.firstOpen = firstOpen;
    }

    @Override
    public String toString() {
        return "AppInstallSdk{" +
                "packageName='" + packageName + '\'' +
                ", firstOpen=" + firstOpen +
                '}';
    }

    @Override
    public int compareTo(AppInstallSdk o) {
        if (firstOpen == o.getFirstOpen()) {
            return 0;
        } else if (firstOpen > o.getFirstOpen()) {
            return -1;
        } else {
            return 1;
        }
    }
}
