package com.lib.jsdk.callback;

import android.content.Context;

public interface OnParseAppInfoListener {
    void onParseSuccess(Context context, String icon, String appName);
}
