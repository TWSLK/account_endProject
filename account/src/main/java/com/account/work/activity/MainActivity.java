package com.account.work.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.adapter.MaterialAdapter;
import com.account.work.adapter.TimeLineAdapter;
import com.account.work.adapter.base.RecyclerBaseAdapter;
import com.account.work.app.Contants;
import com.account.work.db.DbHelper;
import com.account.work.helper.TimeHelper;
import com.account.work.model.Bill;
import com.account.work.model.Budget;
import com.account.work.utils.LogUtils;
import com.account.work.utils.MatcherUtils;
import com.account.work.utils.Preferences;
import com.account.work.utils.ScreenUtils;
import com.account.work.utils.SnackbarUtils;
import com.account.work.utils.building.BindLayout;
import com.account.work.utils.building.BindView;
import com.account.work.utils.depend.AppBarStateChangeListener;
import com.chaychan.viewlib.NumberRunningTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Setting interface
 */
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_money)
    NumberRunningTextView tvExpendMoney;
    @BindView(R.id.tv_income)
    TextView tvShowIncome;
    @BindView(R.id.tv_budget)
    TextView tvShowBudget;
    @BindView(R.id.tv_overspend)
    TextView tvOverSpend;

    @BindView(R.id.nsv_scroll_view)
    NestedScrollView nsvScrollView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_status_nothing)
    LinearLayout llStatusNothing;

    protected Handler handler = new Handler(Looper.getMainLooper());
    private ArrayList<Bill> billsList = new ArrayList<>();
    private RecyclerBaseAdapter itemAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isTimeLineTheme;
    private Bill deleteBill;
    private int deleteItem;
    private int yearMonth;
    private float budgetValue;

    @Override
    protected void initWindow() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Page Jump animation
            getWindow().setEnterTransition(new Slide().setDuration(500));
            getWindow().setReenterTransition(new Fade().setDuration(300));
            getWindow().setExitTransition(new Fade().setDuration(300));
            // Use element sharing animations to add
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
    }

    @Override
    protected void initView() {
        initToolbar();
        initRecyclerView();
        updateHeader();

    }

    private void updateHeader() {
        initYearMonth();
        updateBudget(yearMonth);
    }

    private void updateBudget(final int yearMonth) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Budget queryBudget = new DbHelper().queryBudget(yearMonth);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (queryBudget == null) {
                            tvShowBudget.setText("预算");
                            return;
                        }
                        budgetValue = queryBudget.getValue();
                        tvShowBudget.setText(budgetValue + "");
                    }
                });
            }
        }).start();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            actionBar.setTitle(month + " 月");
        }
    }

    /**
     * Initialize to today's date
     */
    private void initYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        yearMonth = year * 100 + month;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        selectAdapter();
    }


    private void selectAdapter() {
        isTimeLineTheme = getIsTimeLime();
        if (isTimeLineTheme) {
            itemAdapter = new TimeLineAdapter(R.layout.item_time_line, billsList);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, 0);
                }
            });

        } else {
            itemAdapter = new MaterialAdapter(R.layout.item_material, billsList);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, ScreenUtils.dp2px(2), 0, 0);
                }

            });
        }

        setItemClickListener(itemAdapter);
        notifyView();
        recyclerView.setAdapter(itemAdapter);
    }

    /**
     * Judge whether it is timeline style
     */
    private boolean getIsTimeLime() {
        boolean result = Preferences.getBoolean(Contants.KEY_TIME_LINE_THEME, false);
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * When you return to the page, check to see if the settings change
         */
        applyChanged();
    }


    private void applyChanged() {
        boolean isTimeLimeTheme = getIsTimeLime();
        if (isTimeLimeTheme != this.isTimeLineTheme) {
            recreate();
        }
        updateHeader();
    }

    @Override
    protected void initListener() {
        tvShowBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    fab.show();
                } else if (state == State.COLLAPSED) {
                    fab.hide();
                }
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (billsList.isEmpty()) {
                    return;
                }
                int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                Bill firstBill = billsList.get(firstPosition);
                int date = firstBill.getDate();
                int month = date % 10000 / 100;
                getSupportActionBar().setTitle(month + " 月");
            }
        });
    }

    private void setItemClickListener(RecyclerBaseAdapter adapter) {
        adapter.setItemClickListener(new RecyclerBaseAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                itemClick(holder.getAdapterPosition());
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                itemLongClick(holder.getAdapterPosition());
            }
        });
    }

    private void itemLongClick(final int position) {
        Bill bill = billsList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否确认删除数据?");
        builder.setMessage(bill.getTitle() + "  (" + bill.getMainType() + " ￥" + bill.getMoney() + ")");
        builder.setPositiveButton(getString(R.string.affirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemDelete(position);
            }
        });
        builder.setNegativeButton(getString(R.string.canccel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    private void itemClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("备注");
        String remark = billsList.get(position).getNotes();
        builder.setMessage(TextUtils.isEmpty(remark) ? "没有备注信息" : remark);
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }

    private void itemDelete(int position) {
        deleteItem = position;
        deleteBill = billsList.get(position);

        billsList.remove(position);
        itemAdapter.notifyItemRemoved(position);
        updateNothingState();
        if (itemAdapter instanceof TimeLineAdapter) {
            notifyViewDelayed();
        }
        showUndoSnakebar();
        executeDeleteForDatabase();
    }

    /**
     * Delete operation after 2000
     */
    private void executeDeleteForDatabase() {
        handler.postDelayed(deleteRunnable, 2000);
        updateExpend(billsList);
    }

    /**
     * Display undo delete prompt
     */
    private void showUndoSnakebar() {
        SnackbarUtils.noticeAction(fab, "数据删除成功", "撤回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(deleteRunnable);
                billsList.add(deleteItem, deleteBill);
                itemAdapter.notifyItemInserted(deleteItem);
                updateNothingState();
                if (itemAdapter instanceof TimeLineAdapter) {
                    notifyViewDelayed();
                }
                updateExpend(billsList);
            }
        });
    }

    @Override
    protected void initData() {
        loadBillsData();
    }

    private void loadBillsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int startDate = TimeHelper.getInstance().getThisMonthFirstDayDate();
                int endDate = TimeHelper.getInstance().getIntTodayDate();
                ArrayList<Bill> bills = new DbHelper().queryByRange(startDate, endDate);

                billsList.clear();
                billsList.addAll(bills);
                notifyView();
            }
        }).start();
    }

    private void notifyView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                itemAdapter.notifyDataSetChanged();
                updateNothingState();
                updateExpend(billsList);
                nsvScrollView.fullScroll(NestedScrollView.FOCUS_UP);
            }
        });
    }

    /**
     * Delayed refresh
     */
    private void notifyViewDelayed() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNothingState();
                itemAdapter.notifyDataSetChanged();
                updateExpend(billsList);
                updateNothingState();
            }
        }, 500);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateExpend(billsList);
            }
        }, 650);
    }

    /**
     * When there is no data
     * Display blank prompt
     */
    private void updateNothingState() {
        if (billsList.isEmpty()) {
            llStatusNothing.setVisibility(View.VISIBLE);
        } else {
            llStatusNothing.setVisibility(View.GONE);
        }
    }

    /**
     * Update expenditure
     */
    private void updateExpend(ArrayList<Bill> billsList) {
        boolean haveExpend = false;
        float expendSum = 0;
        float incomeSum = 0;
        // Calculate income and expenditure
        for (Bill bill : billsList) {
            if (bill.getMainType().equals(getString(R.string.expend))) {
                expendSum += bill.getMoney();
                    haveExpend = true;
            } else if (bill.getMainType().equals(getString(R.string.income))) {
                incomeSum += bill.getMoney();

            }
        }

        NumberFormat format = MatcherUtils.getMoneyFormat();

        // Update income display
        tvShowIncome.setText(format.format(incomeSum));

        // Update spending and overspending display
        if (!haveExpend) {
            tvExpendMoney.setContent(0 + "");
            updateOverExpend(0);
        } else {
            tvExpendMoney.setContent(format.format(expendSum) + "");

            if (expendSum > budgetValue){
                float overspend = expendSum - budgetValue;
                if (overspend > 0) {
                    updateOverExpend(overspend);
                }
            }else{
                updateOverExpend(0);
            }
        }
    }


    /**
     * Update overruns
     */
    private void updateOverExpend(float i) {
        NumberFormat format = MatcherUtils.getMoneyFormat();
        tvOverSpend.setText(format.format(i) + "");
    }


    /**
     * When activity returns
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Determine what is returned from that page
        switch (requestCode) {
            case Contants.CODE_REQUEST_ADD:
                onAddResult(data);
                break;
            case Contants.CODE_REQUEST_STAT:
                onStatResult(data);
            default:
                break;
        }
    }

    private void onStatResult(Intent intent) {
        System.out.println("osr");
        if (intent != null) {
            boolean changed = intent.getBooleanExtra(Contants.CHANGED_STAT, false);
            System.out.println("c=" + changed);
            if (changed) {
                loadBillsData();
                LogUtils.d(this, "change succeed & notify !");
            }
        }
    }

    private void onAddResult(Intent intent) {
        if (intent != null) {
            boolean succeedAdd = intent.getBooleanExtra(Contants.SUCCEED_ADD, false);
            if (succeedAdd) {
                loadBillsData();
                LogUtils.d(this, "insert succeed & notify !");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_personal:
                personalClick();
                break;
            case R.id.action_stat:
                statClick();
                break;
            case R.id.book:
                startActivity(new Intent(this, com.account.work.note.activity.MainActivity.class));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void statClick() {
        Intent intent = new Intent(this, StatActivity.class);
        startActivityForResult(intent, Contants.CODE_REQUEST_STAT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                fabClick();
                break;
            case R.id.tv_budget:
                budgetClick();
                break;
            default:
                break;
        }
    }

    /**
     * Revise budget
     */
    private void budgetClick() {

    }


    private void fabClick() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    fab, getString(R.string.translatin_name_add));
            ActivityCompat.startActivityForResult(this, intent, Contants.CODE_REQUEST_ADD, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void personalClick() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    // Execute deleted runnable
    Runnable deleteRunnable = new Runnable() {
        @Override
        public void run() {
            new DbHelper().delete(deleteBill);
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}
