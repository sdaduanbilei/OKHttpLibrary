package com.sda.library.Util;

/**
 * Created by scorpio on 15/10/16.
 * Log 工具类
 */

public class Logs {

    public static String Tag = "Debug";
    public static boolean isDebug = true;

    public static String getTag() {
        return Tag;
    }

    public static void setTag(String tag) {
        Tag = tag;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        Logs.isDebug = isDebug;
    }

    public static void Debug(String str) {
        if (isDebug) {
            android.util.Log.d(Tag, str);
        }
    }
}
