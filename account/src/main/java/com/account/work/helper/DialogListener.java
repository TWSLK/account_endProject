package com.account.work.helper;

import android.content.DialogInterface;

/**
 * Dialog box callback
 */
public abstract class DialogListener {
    public void onPositive(DialogInterface dialog, int which) {
    }

    public void onNegative(DialogInterface dialog, int which) {
    }

    public void onItemClick(DialogInterface dialog, int which) {
    }
}