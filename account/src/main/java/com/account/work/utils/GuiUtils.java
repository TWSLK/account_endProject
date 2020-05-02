package com.account.work.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * A tool class that displays animation
 */

public class GuiUtils {
    public interface OnRevealAnimationListener {
        void onRevealHide();

        void onRevealShow();
    }

    // Circle explosion effect display
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(
            final Context context, final View view,
            final int startRadius, @ColorRes final int color,
            final OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        animateRevealShow(context, view, cx, cy, startRadius, color, listener);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(
            final Context context, final View view,
            final int startRadius,
            final OnRevealAnimationListener listener) {
        animateRevealShow(context, view, startRadius, -1, listener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(
            final Context context,
            final View view, int cx, int cy,
            final int startRadius,
            @ColorRes final int color,
            final OnRevealAnimationListener listener) {
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
        // Set round display animation
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.VISIBLE);
                listener.onRevealShow();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (color != -1) {
                    view.setBackgroundColor(ContextCompat.getColor(context, color));
                }
            }
        });

        anim.start();

    }

    // Circle condensation effect
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealHide(
            final Context context, final View view,
            final int finalRadius, @ColorRes final int color,
            final OnRevealAnimationListener listener
    ) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        animateRevealHide(context, view, cx,cy,finalRadius, color,listener);
    }

    public static void animateRevealHide(
            final Context context, final View view, int cx, int cy,
            final int finalRadius, @ColorRes final int color,
            final OnRevealAnimationListener listener) {

        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ContextCompat.getColor(context, color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onRevealHide();
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
}
