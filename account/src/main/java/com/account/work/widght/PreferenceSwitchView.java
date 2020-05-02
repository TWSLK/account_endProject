package com.account.work.widght;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.utils.Preferences;

/**
 * A package with text description and switch control type
 * For example: set the timeline style of the control
 */

public class PreferenceSwitchView extends LinearLayout {

    private String title;
    private String key;
    private TextView tvTitle;
    private Switch aSwitch;

    public PreferenceSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_set_switch, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        aSwitch = (Switch) view.findViewById(R.id.a_switch);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PreferenceSwitchView);
        title = array.getString(R.styleable.PreferenceSwitchView_title);
        key = array.getString(R.styleable.PreferenceSwitchView_key);

        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("preference key can't null!");
        }
        array.recycle();

        boolean selected = Preferences.getBoolean(key, false);
        tvTitle.setText(title);
        aSwitch.setChecked(selected);

        initListener();
    }

    private void initListener() {
        aSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = aSwitch.isChecked();
                aSwitch.setChecked(!checked);
                update();
            }
        };
        PreferenceSwitchView.this.setOnClickListener(clickListener);
    }

    private void update() {
        boolean checked = aSwitch.isChecked();
        Preferences.putBoolean(key, checked);
        System.out.println("key-->"+checked);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
