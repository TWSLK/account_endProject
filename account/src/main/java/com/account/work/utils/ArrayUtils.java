package com.account.work.utils;

import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;

import com.account.work.app.app;

/**
 * Tools to obtain arrays from array.xml files
 */

public class ArrayUtils {
    /**
     * Data acquisition ids
     *
     * @return
     */
    @NonNull
    public static int[] getIds(@ArrayRes int arrayResId) {
        TypedArray minorTypeArray = app.context.getResources().obtainTypedArray(arrayResId);
        int length = minorTypeArray.length();
        int[] monorTypeIconResIds = new int[length];
        for (int i = 0; i < length; i++) {
            monorTypeIconResIds[i] = minorTypeArray.getResourceId(i, 0);
        }
        minorTypeArray.recycle();

        return monorTypeIconResIds;
    }

    public static String[] getStringArray(@ArrayRes int arrayResId) {
        return app.context.getResources().getStringArray(arrayResId);
    }

    public static int[] getIntArray(@ArrayRes int arrayResId) {
        return app.context.getResources().getIntArray(arrayResId);
    }
}
