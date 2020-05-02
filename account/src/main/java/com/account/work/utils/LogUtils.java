package com.account.work.utils;

import android.util.Log;

/**
 * Log printing tool
 */

public class LogUtils {

    public static boolean debug = true;
    public static final String START = "START----->";
    public static final String END = "END----->";

    // Information level
    public static void i(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.i(newTag, msg);
    }

    // Debug level
    public static void d(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.d(newTag, msg);
    }

    // Warning level
    public static void w(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.w(newTag, msg);
    }

    // Detail
    public static void v(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.v(newTag, msg);
    }

    // Error level
    public static void e(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.e(newTag, msg);
    }

    private static String getNewTag(Object tag) {
        String newTag = "";

        if (tag instanceof String) {
            newTag = (String) tag;
        } else if (tag instanceof Class) {
            newTag = ((Class) tag).getSimpleName();
        } else {
            newTag = tag.getClass().getSimpleName();
        }
        return START +newTag+ END;
    }
}
