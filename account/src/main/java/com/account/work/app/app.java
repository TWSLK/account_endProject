package com.account.work.app;

import android.app.Application;
import android.content.Context;

import com.account.work.utils.Preferences;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.ToastUtils;

/**
 * When the program starts, this class will go first
 * Remember that this class inherits Application and registers in the Androidmanifest.xml (list file)
 */

public class app extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Cache.init();
        initUtils();
    }

    /**
     * Initialize some of the tools that you write
     * Because these tools use context (context)
     */
    private void initUtils() {

        ScreenUtils.init(this);
        ToastUtils.init(this);
        Preferences.init(this);
    }
}
