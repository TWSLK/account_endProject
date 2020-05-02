package com.account.work.helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Wait dialog box tool
 */

public class ProgressDialogHelper {
    ProgressDialog dialog;

    public ProgressDialog show(Context context,String title, String msg, boolean cancelable) {
        if (dialog == null) {
            dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.setCancelable(cancelable);
        dialog.setIndeterminate(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
