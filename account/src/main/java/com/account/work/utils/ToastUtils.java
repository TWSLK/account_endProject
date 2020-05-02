package com.account.work.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast Tool class<br>
 * need init<br>
 */
public class ToastUtils {

    public static Context context;
    private static Toast toast;

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Initialize the Toast tool class<br>
     */
    public static void init(Context context) {
        ToastUtils.context = context.getApplicationContext();
    }

    public static void show(int resId) {
        show(context.getString(resId));
    }

    public static void show(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}
