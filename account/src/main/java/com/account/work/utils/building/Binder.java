package com.account.work.utils.building;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * findViewById annotation<br>
 * {@link Activity} onCreate Binder.bind(this);<br>
 */

public class Binder {
    public static void bind(Activity activity) {
        bindContentView(activity);
        bind(activity, activity.getWindow().getDecorView());
    }

    public static void bind(Object target, View view) {
        Class clazz = target.getClass();
        // Get all the member variables of Activity
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Gets the annotation above each member variable
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                try {
                    field.setAccessible(true);
                    field.set(target, view.findViewById(bindView.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void bindContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // Get notes in Activity
        BindLayout contentView = clazz.getAnnotation(BindLayout.class);
        if (contentView != null) {
            // If this activity has this annotation on it,
            // Take out the value value corresponding to this
            // annotation, which is actually the layout file mentioned earlier.
            try {
                Method setViewMethod = clazz.getMethod("setContentView", int.class);
                setViewMethod.invoke(activity, contentView.value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
