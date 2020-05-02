package com.account.work.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.account.work.R;
import com.account.work.utils.LogUtils;
import com.account.work.utils.building.Binder;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();

    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, "onCreate");

        initWindow();
        Binder.bind(this);
        initView();
        initListener();
        initData();
    }

    /**
     * Binder.bind(this);before<br>
     */
    protected void initWindow() {
    }

    /**
     * <p>Initializing view</p>
     * <p>The base class initializes Toolbar to return operations if there is a return</p>
     */
    protected void initView() {
        initBaseToolbar();
    }

    /**
     * <p>Unify the activity settings with toobar and click the home button of toobar for the return operation</p>
     */
    private void initBaseToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        if (toolbar != null) {
            LogUtils.i(this, "have toolbar");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            return;
        }
        LogUtils.i(this, "no have toolbar");
    }


    /**
     * Initialization data
     */
    protected void initData() {
    }

    /**
     * Initializing listener
     */
    protected void initListener() {
    }

    /**
     * ActionBar menu click event
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //toolbar home Button return
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * activity Destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
    }
}
