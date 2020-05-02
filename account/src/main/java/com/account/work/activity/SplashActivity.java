package com.account.work.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.transition.Fade;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.app.app;
import com.account.work.db.BillDatabaseHelper;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.building.BindLayout;
import com.account.work.utils.building.BindView;

/**
 * SplashActivity The program starts the activity on the first page
 */
@BindLayout(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.iv_launcher)
    ImageView ivLauncher;

    private final static int TIME_DELATED = 1500;
    protected Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void initWindow() {
        super.initWindow();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(500));
        }
        ScreenUtils.transparentSystemBar(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a database
        SQLiteDatabase database = new BillDatabaseHelper().getWritableDatabase();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            rippleAnim();
        } else {
            llRoot.setBackgroundColor(ContextCompat.getColor(app.context, R.color.colorPrimary));
            delayedGoto(TIME_DELATED);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void rippleAnim() {
        llRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //layout方法执行结束之后(位置确定之后)
            @Override
            public void onGlobalLayout() {
                llRoot.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);

                final int color = R.color.colorPrimary;
                Animator anim = null;
                int[] position = new int[2];
                ivLauncher.getLocationInWindow(position);
                anim = ViewAnimationUtils.createCircularReveal(llRoot,
                        position[0] + ivLauncher.getWidth() / 2,
                        position[1] + ivLauncher.getHeight() / 2,
                        0,
                        (int) Math.hypot(llRoot.getWidth(), llRoot.getHeight()));

                anim.setDuration(3000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        delayedGoto(300);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        if (color != -1) {
                            llRoot.setBackgroundColor(ContextCompat.getColor(app.context, color));
                        }
                    }
                });

                anim.setStartDelay(100);
                anim.start();
            }
        });
    }

    /**
     * Delayed entry
     */
    private void delayedGoto(int timeDelated) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle();
                    ContextCompat.startActivity(SplashActivity.this, intent, options);
                } else {
                    startActivity(intent);
                }
                finish();
            }
        }, timeDelated);
    }


    @Override
    public void onBackPressed() {
        /**
         * The return key is not allowed
         */
    }
}
