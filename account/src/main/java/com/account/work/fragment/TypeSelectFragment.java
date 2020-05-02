package com.account.work.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.activity.AddActivity;
import com.account.work.adapter.TypeAdapter;
import com.account.work.adapter.base.RecyclerBaseAdapter;
import com.account.work.app.Contants;
import com.account.work.app.app;
import com.account.work.db.DbHelper;
import com.account.work.fragment.base.BaseFragment;
import com.account.work.helper.EditDialogHelper;
import com.account.work.helper.ListDialogHelper;
import com.account.work.model.Bill;
import com.account.work.model.MinorType;
import com.account.work.utils.ArrayUtils;
import com.account.work.utils.LogUtils;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.ToastUtils;
import com.account.work.utils.building.BindView;
import com.account.work.utils.building.Binder;
import com.account.work.utils.depend.BizierEvaluator2;
import com.account.work.utils.depend.SpacesItemDecoration;
import com.account.work.widght.ColorImageView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Type selection
 * Note: add the page below can slide left and right, can also slide up and down
 * Like the statistics page, his outermost layer is also a viewpager, and then it's loaded with fragment
 * Fragment is equipped with recyclerView to support up and down slide
 */

public class TypeSelectFragment extends BaseFragment implements View.OnTouchListener, RecyclerBaseAdapter.ItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private int spanCount = 4;
    private float startMoveY;
    private boolean isMoving;
    private GridLayoutManager layoutManager;
    private TypeAdapter adapter;
    private ArrayList<MinorType> typeData;

    public TypeSelectFragment initType(String type) {
        this.mainType = type;
        return this;
    }

    private String mainType;

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(app.context).inflate(R.layout.fragment_type_select, null);
        Binder.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        typeData = getTypeData();

        layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(ScreenUtils.dp2px(8));
        recyclerView.addItemDecoration(decoration);
        adapter = new TypeAdapter(R.layout.item_type_select, typeData);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(this);
        adapter.setItemClickListener(this);
    }

    /**
     * Return the type of the bill
     */
    private ArrayList<MinorType> getTypeData() {
        if (mainType.equals(getString(R.string.income))) {
            return initIncomeTypeData();
        } else if (mainType.equals(getString(R.string.expend))) {
            return initExpendTypeData();
        } else {
            LogUtils.e(this, "not find mainType!");
            throw new RuntimeException("not find mainType!");
        }
    }

    @NonNull
    private ArrayList<MinorType> initExpendTypeData() {
        ArrayList<MinorType> minorTypes = fillTypeData(R.array.minor_type_expend,
                R.array.minor_type_drawable_ids_expend, R.array.minor_type_tint_expend);
        return minorTypes;
    }

    @NonNull
    private ArrayList<MinorType> initIncomeTypeData() {
        ArrayList<MinorType> minorTypes = fillTypeData(R.array.minor_type_income,
                R.array.minor_type_drawable_ids_income, R.array.minor_type_tint_income);
        return minorTypes;
    }

    @NonNull
    private ArrayList<MinorType> fillTypeData(@ArrayRes int typeTitles, @ArrayRes int typeDrawables, @ArrayRes int typeTint) {
        ArrayList<MinorType> minorTypes = new ArrayList<>();

        String monorTypeTitle[] = ArrayUtils.getStringArray(typeTitles);
        int iconsId[] = ArrayUtils.getIds(typeDrawables);
        int tintColors[] = ArrayUtils.getIntArray(typeTint);

        for (int i = 0; i < monorTypeTitle.length; i++) {
            minorTypes.add(new MinorType()
                    .setTypeName(monorTypeTitle[i])
                    .setTypeIconId(iconsId[i])
                    .setTintColor(tintColors[i]));
        }
        return minorTypes;
    }

    // recyclerview item click
    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        playAnimation(view);

        String typeName = typeData.get(holder.getAdapterPosition()).getTypeName();
        if (typeName.equals(getString(R.string.custom))) {
            customType(typeName);
            return;
        }
        cacheEntryType(typeName);

    }


    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        MinorType minorType = typeData.get(holder.getAdapterPosition());
        if (minorType.getTypeName().equals(getString(R.string.trip))) {
            showTypeDetailDialog(view, minorType.getTypeName());
            return;
        }
        if (minorType.getTypeName().equals(getString(R.string.dining))) {
            showTypeDetailDialog(view, minorType.getTypeName());
            return;
        }
    }

    /**
     * Determine type popups
     */
    private void showDetermineDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view);
    }

    /**
     * Detailed type popups
     *
     * @param view
     * @param typeName
     */
    private void showTypeDetailDialog(final View view, final String typeName) {
        String[] titles = null;
        int[] iconIds = null;
        if (typeName.equals(getString(R.string.dining))) {
            titles = ArrayUtils.getStringArray(R.array.type_dining);
            iconIds = ArrayUtils.getIds(R.array.type_dining_drawable_ids);
        } else if (typeName.equals(getString(R.string.trip))) {
            titles = ArrayUtils.getStringArray(R.array.type_trip);
            iconIds = ArrayUtils.getIds(R.array.type_trip_drawable_ids);
        }

        ArrayList<ListDialogHelper.Item> dialogItems = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            dialogItems.add(new ListDialogHelper.Item()
                    .setBitmap(BitmapFactory.decodeResource(getResources(), iconIds[i]))
                    .setTitle(titles[i]));
        }

        final String[] finalTitles = titles;
        new ListDialogHelper().showListDialog(getContext(), dialogItems, new ListDialogHelper.ItemClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == finalTitles.length - 1) {
                    customType(typeName);
                } else {
                    ((AddActivity) getActivity()).setSelectTItle(finalTitles[item], Color.BLUE);

                    cacheEntryTitle(finalTitles[item]);
                    cacheEntryType(typeName);
                }
            }
        });
    }

    /**
     * Custom type
     *
     * @param typeName
     */
    private void customType(final String typeName) {

        new EditDialogHelper(getContext()).show(getString(R.string.custom), new EditDialogHelper.ButtonListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPositive(View editViewRoot, String content) {
                if (TextUtils.isEmpty(content)) {
                    Toasty.error(getActivity(), "Can not be empty!").show();

                    return;
                }
                ((AddActivity) getActivity()).setSelectTItle(content, Color.BLUE);
                ((AddActivity) getActivity()).setSelectIcon(getActivity().getDrawable(R.drawable.ic_custom), 0xff707070);

                cacheEntryType(typeName);
                cacheEntryTitle(content);
            }
        });
    }

    /**
     * Add according to type
     */
    private void addBillByType(String type, String title) {
        AddActivity addActivity = (AddActivity) getActivity();
        float money = addActivity.getShowMoney();
        int date = addActivity.getSelectDate();
        String notes = addActivity.getNotes();
        addBill(title, type, money, date, notes);
        addFinishExit();
    }

    /**
     * Add a bill
     */
    private void addBill(String title, String type, float money, int date, String notes) {
        long addTime = System.currentTimeMillis();
        Bill bill = new Bill()
                .setMainType(mainType)
                .setAddTime(addTime)
                .setMinorType(type)
                .setTitle(title)
                .setMoney(money)
                .setNotes(notes)
                .setDate(date);

        DbHelper helpter = new DbHelper();
        helpter.insert(bill);
    }

    /**
     * Types of cache selection
     */
    private void cacheEntryType(String typeName) {
        ((AddActivity) getActivity()).setEntryMainType(mainType);
        ((AddActivity) getActivity()).setEntryMinorType(typeName);
    }

    /**
     * Cache settings for title
     */
    private void cacheEntryTitle(String title) {
        ((AddActivity) getActivity()).setEntryTitle(title);
    }


    /**
     * Cache settings for title
     */
    private boolean checkMoneyValue(View view) {
        AddActivity addActivity = (AddActivity) getActivity();

        // money Input check
        float money = addActivity.getShowMoney();
        if (money == 0f) {
            Toasty.warning(getActivity(), getString(R.string.set_the_money_value)).show();

            return false;
        }
        return true;
    }

    /**
     * Add complete exit
     */
    private void addFinishExit() {
        ToastUtils.show("添加成功");
        Intent intent = new Intent();
        intent.putExtra(Contants.SUCCEED_ADD, true);
        getActivity().setResult(Contants.CODE_RESULT_ADD, intent);
        getActivity().finish();
    }


    //recyclerview onTouch
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isMoving) {
                    isMoving = true;
                    startMoveY = y;
                }
                if (startMoveY - y > 20) {
                    hideNumberInputView();
                } else if (layoutManager.findFirstVisibleItemPosition() == 0 && y - startMoveY > 20) {
                    showNumberInputView();
                }
                break;
            default:
                startMoveY = 0;
                isMoving = false;
                break;
        }
        return false;
    }

    /**
     * Mobile animation
     */
    public void playAnimation(View view) {
        final AddActivity activity = (AddActivity) getActivity();

        LinearLayout itemRootView = (LinearLayout) view;
        final TextView typeTextView = (TextView) itemRootView.findViewById(R.id.tv_type_name);
        final ColorImageView typeView = (ColorImageView) itemRootView.findViewById(R.id.iv_type_icon);
        final String typeName = typeTextView.getText().toString();

        // If it's custom type, it doesn't display animation
        if (typeName.equals(getString(R.string.custom))) {
            return;
        }

        final ColorImageView iv = new ColorImageView(getContext());
        iv.setImageDrawable(typeView.getDrawable());
        iv.setBgColor(typeView.getBgColor());
        iv.setColorFilter(Color.WHITE);

        int position[] = new int[2];
        view.getLocationInWindow(position);
        iv.setX(position[0]);
        iv.setY(position[1]);

        int width = typeView.getWidth();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
        params.topMargin = ScreenUtils.dp2px(5);
        params.leftMargin = ScreenUtils.dp2px(5);

        iv.setLayoutParams(params);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.addView(iv);

         /* The starting position of the animation, that is, the location
         of the object; the position of the end of the animation, that is,
         the position of the title */
        Point startPosition = new Point(position[0], position[1]);
        Point endPosition = activity.getTypeIconPosition();

        int pointX = (startPosition.x + endPosition.x) / 2 - 100;
        int pointY = startPosition.y - 200;
        Point controllPoint = new Point(pointX, pointY);
        /*
        * Attribute animation, rely on TypeEvaluator to achieve animation effect,
        * in which displacement, scaling, gradient, rotation can be used directly
        * Here is the custom TypeEvaluator, we record the trajectory of the movement
        * through point, and then the object moves along the trajectory,
        * Once the trajectory changes, we call the addUpdateListener method, where
        * we constantly acquire new locations and move objects
        * */
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BizierEvaluator2(controllPoint), startPosition, endPosition);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                iv.setX(point.x);
                iv.setY(point.y);
            }
        });

        /**
         * The animation is finished and removed
         */
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
                rootView.removeView(iv);

                activity.setSelectTItle(typeName, typeView.getBgColor());
                activity.setSelectIcon(typeView.getDrawable(), typeView.getBgColor());
            }
        });
    }

    private void hideNumberInputView() {
        ((AddActivity) getActivity()).hideNumberInputView();
    }

    private void showNumberInputView() {
        ((AddActivity) getActivity()).showNumberInputView();
    }


}
