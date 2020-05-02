package com.account.work.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.adapter.FragmentAdapter;
import com.account.work.app.Contants;
import com.account.work.db.DbHelper;
import com.account.work.fragment.TypeSelectFragment;
import com.account.work.helper.DateCheckHelper;
import com.account.work.helper.EditDialogHelper;
import com.account.work.helper.MoneyEditHelper;
import com.account.work.helper.TimeHelper;
import com.account.work.model.Bill;
import com.account.work.utils.GuiUtils;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.SnackbarUtils;
import com.account.work.utils.ToastUtils;
import com.account.work.utils.building.BindLayout;
import com.account.work.utils.building.BindView;
import com.account.work.widght.ColorImageView;
import com.mnnyang.numberkeyboard.NumberInputView;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Add page
 */
@BindLayout(R.layout.activity_add)
public class AddActivity extends BaseActivity implements View.OnClickListener,
        NumberInputView.KeyboardListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.ll_header)
    LinearLayout viewHeader;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.et_input)
    EditText etMoneyInput;
    @BindView(R.id.ll_input_view)
    LinearLayout llInputView;

    @BindView(R.id.ll_input_root)
    LinearLayout llInputViewRoot;
    @BindView(R.id.niv_number_input)
    NumberInputView numberInputView;

    @BindView(R.id.ll_add_root)
    LinearLayout llRoot;

    @BindView(R.id.iv_toolbar_icon)
    ImageView ivToolbarIcon;
    @BindView(R.id.iv_toolbar_done)
    ImageView ivToolbarDone;
    @BindView(R.id.iv_toolbar_notes)
    ImageView ivToolbarNotes;

    @BindView(R.id.tv_select_type_name)
    TextView tvSelectTypeName;
    @BindView(R.id.iv_selectype_icon)
    ColorImageView civSelectTypeIcon;
    @BindView(R.id.tv_selsect_time)
    TextView tvSelectTime;

    private boolean isNumberInputViewHide = false;
    // Amount input test assistant
    private MoneyEditHelper moneyHelper;
    private DatePickerDialog mDatePickerDialog;

    // Added data cache
    private String entryMainType;
    private String entryMinorType;
    private String entryNotes;
    private String entryTitle;
    private int entryYear;
    private int entryMonth;
    private int entryDay;
    private int entryDate;


    @Override
    protected void initWindow() {
        super.initWindow();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setReturnTransition(new Fade().setDuration(500));
            getWindow().setEnterTransition(new Slide().setDuration(500));
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        ScreenUtils.transparentSystemBar(this);
    }

    public Point getTypeIconPosition() {
        Point point = new Point();
        int position[] = new int[2];
        civSelectTypeIcon.getLocationInWindow(position);
        point.x = position[0];
        point.y = position[1];
        return point;
    }

    public void setSelectTItle(String text, int color) {
        tvSelectTypeName.setText(text);
        tvSelectTypeName.setTextColor(color);
    }

    public void setSelectIcon(Drawable drawable, int bgColor) {
        drawable = drawable.getConstantState().newDrawable();

        civSelectTypeIcon.setImageDrawable(drawable);
        civSelectTypeIcon.setColorFilter(Color.WHITE);
        civSelectTypeIcon.setBgColor(bgColor);
    }

    @Override
    protected void initView() {
        super.initView();
        initMarginStatusBar();
        initHeightInputView();
        initViewPager();
        initHelper();
        initDatePicker();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
        } else {
            viewHeader.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            initViews();
        }

    }

    private void initHeightInputView() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llInputViewRoot.getLayoutParams();
        layoutParams.height = ScreenUtils.getSHeight()/2;
        llInputViewRoot.setLayoutParams(layoutParams);
    }

    private void initMarginStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
            if (statusBarHeight > 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llToolbar.getLayoutParams();
                layoutParams.topMargin = statusBarHeight;
                llToolbar.setLayoutParams(layoutParams);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                viewHeader.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    // Animation display
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        GuiUtils.animateRevealShow(this, viewHeader,
                (int) ivToolbarIcon.getX(), (int) ivToolbarIcon.getY(),
                20, R.color.colorAccent, new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        initViews();
                    }
                });
    }

    // Initialization view
    private void initViews() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(AddActivity.this, android.R.anim.fade_in);
                animation.setDuration(300);
                ivToolbarDone.setAnimation(animation);
                ivToolbarNotes.setAnimation(animation);
                tabLayout.setAnimation(animation);
                llInputView.setAnimation(animation);

                ivToolbarDone.setVisibility(View.VISIBLE);
                ivToolbarNotes.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                llInputView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        entryYear = calendar.get(Calendar.YEAR);
        entryMonth = calendar.get(Calendar.MONTH)+1;
        entryDay = calendar.get(Calendar.DAY_OF_MONTH);
        entryDate = TimeHelper.getInstance().getIntTodayDate();

        mDatePickerDialog = new DatePickerDialog(this, this, entryYear, entryMonth, entryDay);
    }

    private void initHelper() {
        moneyHelper = new MoneyEditHelper(etMoneyInput);
    }

    private void initViewPager() {
        String income = getString(R.string.income);
        String expend = getString(R.string.expend);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new TypeSelectFragment().initType(expend));
        fragmentAdapter.addFragment(new TypeSelectFragment().initType(income));
        fragmentAdapter.setTitles(new String[]{expend, income});
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initListener() {
        super.initListener();
        numberInputView.setKeyboardListener(this);
        llInputViewRoot.setOnClickListener(this);
        ivToolbarNotes.setOnClickListener(this);
        ivToolbarDone.setOnClickListener(this);
        ivToolbarIcon.setOnClickListener(this);
        tvSelectTime.setOnClickListener(this);
        etMoneyInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_toolbar_icon:
                exit();
                break;
            case R.id.iv_toolbar_notes:
                showNotesDialog();
                break;
            case R.id.iv_toolbar_done:
                submit();
                break;
            case R.id.et_input:
                showNumberInputView();
                break;
            case R.id.tv_selsect_time:
                selectTime();
                break;
            case R.id.ll_input_root:
                showNumberInputView();
                break;
            default:
                break;
        }
    }

    /**
     * Confirm add
     */
    private void submit() {
        boolean addSucceed = addBill();
        if (addSucceed) {
            addFinishExit();
        }
    }

    /**
     * Add a bill
     */
    private boolean addBill() {
        // check selected type
        if (TextUtils.isEmpty(entryMainType)
                || TextUtils.isEmpty(entryMinorType)) {
            Toasty.warning(this, getString(R.string.text_set_the_type)).show();
            return false;
        }
        // check money value
        float money = getShowMoney();
        if (money <= 0f) {
            Toasty.warning(this, getString(R.string.set_the_money_value)).show();

            return false;
        }

        // check title
        if (TextUtils.isEmpty(entryTitle)) {
            entryTitle = entryMinorType;
        }

        long addTime = System.currentTimeMillis();
        Bill bill = new Bill()
                .setMainType(entryMainType)
                .setMinorType(entryMinorType)
                .setTitle(entryTitle)
                .setAddTime(addTime)
                .setMoney(money)
                .setNotes(entryNotes)
                .setSync(0)
                .setDate(entryDate);

        // insert to db
        DbHelper helpter = new DbHelper();
        helpter.insert(bill);

        return true;
    }


    /**
     * Add complete exit
     */
    private void addFinishExit() {
        Toasty.success(this, "添加成功").show();
        Intent intent = new Intent();
        intent.putExtra(Contants.SUCCEED_ADD, true);
        setResult(Contants.CODE_RESULT_ADD, intent);
        onBackPressed();
    }

    /**
     * Quit application
     */
    private void exit() {
        onBackPressed();
    }

    // Exit event
    @Override
    public void onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            defaultBackPressed();
            return;
        }
        GuiUtils.animateRevealHide(this, llRoot,
                (int) ivToolbarIcon.getX(), (int) ivToolbarIcon.getY(),
                20, R.color.colorAccent, new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        defaultBackPressed();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    // Default rollback
    private void defaultBackPressed() {
        super.onBackPressed();
    }

    /**
     * Add remarks dialog box
     */
    private void showNotesDialog() {
        new EditDialogHelper(this).show(getString(R.string.notes), new EditDialogHelper.ButtonListener() {
            @Override
            public void onPositive(View editViewRoot, String content) {
                entryNotes = content;
                if (!TextUtils.isEmpty(entryNotes)) {
                    Toasty.success(AddActivity.this, getString(R.string.notes_set_succeed)).show();
                }
            }
        });
    }

    /**
     * Select date
     */
    private void selectTime() {
        mDatePickerDialog.show();
    }

    /**
     * Date control callback
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;// The date will be one day, so add one
        new DateCheckHelper(year, month, dayOfMonth, new DateCheckHelper.Listener() {
            @Override
            public void succeed(int date, int year, int month, int dayOfMonth, String name) {
                selectTimeSucceed(date, year, month, dayOfMonth, name);
            }

            @Override
            public void fail() {
                selectTimeFail();
            }
        }).start();
    }

    /**
     * Choose the correct date
     */
    private void selectTimeSucceed(int date, int year, int month, int dayOfMonth, String name) {
        entryYear = year;
        entryMonth = month;
        entryDay = dayOfMonth;
        entryDate = date;

        if (!TextUtils.isEmpty(name)) {
            tvSelectTime.setText(name);
            return;
        }
        tvSelectTime.setText(year + "-" + month + "-" + dayOfMonth);
    }

    /**
     * Wrong date selection
     */
    private void selectTimeFail() {
        Toasty.error(this, "You can't choose the future!").show();
    }

    // Input amount callback
    @Override
    public void onNumber(String number) {
        String value = moneyHelper.onAddNumber(number);
    }


    @Override
    public void onNumberDelete() {
        String value = moneyHelper.onDelete();
    }

    /**
     * Display numeric input keyboard
     */
    public void showNumberInputView() {
        if (!isNumberInputViewHide) {
            return;
        }
        llInputViewRoot.animate().translationY(0).start();
        isNumberInputViewHide = false;
    }

    /**
     * Hide digital input keyboard
     */
    public void hideNumberInputView() {
        if (isNumberInputViewHide) {
            return;
        }
        llInputViewRoot.animate().translationY(llInputViewRoot.getHeight()).start();
        isNumberInputViewHide = true;
    }

    /**
     * Display notification
     *
     * @param msg
     */
    public void notice(String msg) {
        SnackbarUtils.notice(tvSelectTime, msg);
        ToastUtils.show(msg);
    }

    /**
     * Get the amount of input
     */
    public float getShowMoney() {
        String string = etMoneyInput.getText().toString();
        if (TextUtils.isEmpty(string)) {
            return 0f;
        }
        return Float.parseFloat(string);
    }

    public LinearLayout getLlRoot() {
        return llRoot;
    }

    /**
     * Get the selected date
     */
    public int getSelectDate() {
        return entryDate;
    }

    /**
     * Gets the set remarks
     */
    public String getNotes() {
        return entryNotes;
    }

    /**
     * Set main types
     */
    public AddActivity setEntryMainType(String entryMainType) {
        this.entryMainType = entryMainType;
        return this;
    }

    /**
     * Setting minor types
     */
    public AddActivity setEntryMinorType(String entryMinorType) {
        this.entryMinorType = entryMinorType;
        return this;
    }

    /**
     * Setting title
     */
    public AddActivity setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
        return this;
    }

}
