package com.account.work.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.account.work.widght.BottomDialog;


/**
 * Dialog box tool: pop-up various dialog boxes
 */

public class DialogHelper {

    private ProgressDialog progressDialog;
    private BottomDialog bottomDialog;

    /**
     * Uncertain closed wait dialog box
     */
    public DialogHelper showProgressDialog(Context context, String title, String msg, boolean canceable) {
        progressDialog = ProgressDialog.show(context, title, msg, true, canceable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return this;
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Dialog box displayed at the bottom
     */
    public void showBottomDialog(Activity activity, View contentView) {
        bottomDialog = new BottomDialog(activity);
        bottomDialog.setContentView(contentView);
        bottomDialog.setCancelable(true);

        View parent = (View) contentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomDialog.show();
    }

    public void hideBottomDialog() {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
    }

    /**
     * General dialog box
     */
    public void showNormalDialog(@NonNull AppCompatActivity activity, @NonNull String title,
                                 @NonNull String massage,String posButton, String negButton,
                                 @NonNull final DialogListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(massage)
                .setPositiveButton(posButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPositive(dialog, which);
                            }
                        })
                .setNegativeButton(negButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegative(dialog, which);
                            }
                        })
                .show();
    }

    /**
     * Dialog box list
     */
    public void showListDialog(@NonNull Activity activity, @NonNull String title,
                               @NonNull String[] items, @NonNull final DialogListener listener) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(dialog, which);
                    }
                })
                .show();
    }

}
