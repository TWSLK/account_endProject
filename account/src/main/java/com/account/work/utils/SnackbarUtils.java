package com.account.work.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Snackbar display tool
 */

public class SnackbarUtils {

    /**
     * Display general notification
     */
    public static Snackbar notice(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, " ( ゜- ゜)つロ" + msg,
                Snackbar.LENGTH_LONG).setAction("Get it", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.show();
        return snackbar;
    }

    /**
     * Display notifications that can be operated
     */
    public static void noticeAction(View view, String msg, String actionName, final View.OnClickListener clickListener) {
        Snackbar.make(view, msg,
                Snackbar.LENGTH_LONG).setAction(actionName, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        }).show();
    }
}
