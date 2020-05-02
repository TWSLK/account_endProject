package com.account.work.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.activity.base.StatCustomTimeOperation;
import com.account.work.adapter.FragmentAdapter;
import com.account.work.app.Contants;
import com.account.work.fragment.RecyclerViewFragment;
import com.account.work.helper.TimeHelper;
import com.account.work.utils.LogUtils;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.building.BindLayout;
import com.account.work.utils.building.BindView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Statistics page
 */

@BindLayout(R.layout.activity_stat)
public class StatActivity extends BaseActivity implements MaterialViewPager.Listener,
        DatePickerDialog.OnDateSetListener, StatCustomTimeOperation {

    @BindView(R.id.mvp_view_pager)
    private MaterialViewPager mViewPager;

    private String[] pageTitles = new String[]{
            "今天", "昨天", "本周", "上周", "本月", "上月", "今年", "去年"};
    private int startDate;
    private int endDate;
    private DatePickerDialog timeDialog;
    private boolean shouldStartDate = true;
    private FragmentAdapter fragmentAdapter;
    private RecyclerViewFragment customFragment;
    private int customYear;
    private int customMonth;
    private int customDay;
    private Calendar customCalendar;

    private boolean isChanged;

    public StatActivity setChanged(boolean changed) {
        LogUtils.d(this, "setChanged="+changed);
        isChanged = changed;
        return this;
    }

    @Override
    protected void initWindow() {
        ScreenUtils.transparentSystemBar(this);
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        toolbar = mViewPager.getToolbar();
        super.initView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    protected void initListener() {
        mViewPager.setMaterialViewPagerListener(this);
        setTitleClickListener();
    }

    private void setTitleClickListener() {
        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    customTime(shouldStartDate);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Select the time periods that need to be counted
     */
    private void customTime(boolean shouldStart) {

        if (customCalendar == null) {
            customCalendar = Calendar.getInstance();
            customYear = customCalendar.get(Calendar.YEAR);
            customMonth = customCalendar.get(Calendar.MONTH);
            customDay = customCalendar.get(Calendar.DAY_OF_MONTH);
        }

        timeDialog = new DatePickerDialog(this, this, customYear, customMonth, customDay);
        timeDialog.show();
        if (shouldStart) {
            Toasty.info(StatActivity.this, "Selection start time").show();
        } else {
            Toasty.info(StatActivity.this, "Select end time").show();
        }
    }

    @Override
    protected void initData() {
        initViewPager();
    }

    private void initViewPager() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setTitles(pageTitles);
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < pageTitles.length; i++) {
            RecyclerViewFragment fragment = RecyclerViewFragment.newInstance(pageTitles[i]);
            if (i == pageTitles.length - 1) {
                customFragment = fragment;
            }
            fragments.add(fragment);
        }
        fragmentAdapter.setFragments(fragments);
        mViewPager.getViewPager().setAdapter(fragmentAdapter);
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());//must
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    public HeaderDesign getHeaderDesign(int i) {
        switch (i) {
            case 0:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.blue,
                        "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
            case 1:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.green,
                        "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
            case 2:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.red,
                        "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
            case 3:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.cyan,
                        "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
        }
        return null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;// The date will be one day, so add one
        int date = TimeHelper.getInstance().formatDate(year, month, dayOfMonth);
        if (shouldStartDate) {
            startDate = date;
            endDate = 0;
            customTime(false);
        } else {
            endDate = date;
            notifyCustomPage();
        }

        shouldStartDate = !shouldStartDate;
    }

    private void notifyCustomPage() {
        System.out.println("Start refreshing custom page");
        customFragment.notifyCustomData();
    }


    @Override
    public int getStartDate() {
        return startDate;
    }

    @Override
    public int getEndDate() {
        return endDate;
    }

    @Override
    public void clearSelectionDate() {
        startDate = endDate = 0;
    }

    @Override
    public void moveToCustom() {
        int pageLength = mViewPager.getViewPager().getChildCount();
        mViewPager.getViewPager().setCurrentItem(pageLength - 1);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Contants.CHANGED_STAT,isChanged);
        setResult(Contants.CODE_RESULT_STAT, intent);
        super.onBackPressed();
    }
}