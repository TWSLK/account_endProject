package com.account.work.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.account.work.R;
import com.account.work.activity.StatActivity;
import com.account.work.activity.base.StatCustomTimeOperation;
import com.account.work.adapter.StatViewAdapter;
import com.account.work.db.DbHelper;
import com.account.work.fragment.base.BaseFragment;
import com.account.work.helper.DialogHelper;
import com.account.work.helper.DialogListener;
import com.account.work.helper.TimeHelper;
import com.account.work.model.Bill;
import com.account.work.model.stat.ChartStat;
import com.account.work.model.stat.Stat;
import com.account.work.utils.TimeUtils;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 *
 * This fragment is the fragment of the statistical page viewpager
 * Our statistics page can slide right and left, and his outside is a viewpager
 * Specifically, each page is a fragment, and this fragment is this
 * Finally, fragment is loaded with recyclerView to support the up and down slide of the statistics page
 *
 * The concrete layer, the outermost layer, is MaterialViewPager-->viewpager-->fragment-->recyclerview
 */
public class RecyclerViewFragment extends BaseFragment implements StatViewAdapter.ItemClickListener {
    RecyclerView mRecyclerView;
    private List<Stat> statDataSet = new ArrayList<>();
    private String type;
    private StatViewAdapter recyclerViewAdapter;
    private StatCustomTimeOperation customOperation;

    private TimeHelper timeHelper = TimeHelper.getInstance();
    private static Handler handler = new Handler(Looper.getMainLooper());
    private ArrayList<Bill> billList;

    public RecyclerViewFragment setType(String type) {
        this.type = type;
        return this;
    }

    public static RecyclerViewFragment newInstance(String type) {
        return new RecyclerViewFragment().setType(type);
    }


    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customOperation = (StatCustomTimeOperation) getActivity();
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDataByType(type);
                notifyView();
            }
        }).start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // setup materialviewpager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        // Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerViewAdapter = new StatViewAdapter(statDataSet);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setItemClickListener(this);
    }

    /**
     * Loading data based on the current type
     */
    private void loadDataByType(String type) {
        int date = 0, startDate = 0, endDate = 0;
        billList = null;

        int todayDate = timeHelper.getIntTodayDate();

        switch (type) {
            case "今天":
                date = todayDate;
                billList = new DbHelper().queryByDate(date);
                break;
            case "昨天":
                date = timeHelper.getIntYesterDayDate();
                billList = new DbHelper().queryByDate(date);
                break;
            case "本周":
                startDate = timeHelper.getThisWeekFirstDayDate();
                endDate = todayDate;
                billList = getBillsByRange(startDate, endDate);
                break;
            case "上周":
                startDate = timeHelper.getLastWeekFirstDayDate();
                endDate = timeHelper.getLastWeekLastDayDate();
                billList = getBillsByRange(startDate, endDate);
                break;
            case "本月":
                startDate = timeHelper.getThisMonthFirstDayDate();
                endDate = todayDate;
                billList = getBillsByRange(startDate, endDate);
                break;
            case "上月":
                startDate = timeHelper.getLastMonthFirstDayDate();
                endDate = timeHelper.getLastMonthLastDayDate();
                billList = getBillsByRange(startDate, endDate);
                break;
            case "今年":
                startDate = timeHelper.getThisYearFirstDayDate();
                endDate = todayDate;
                billList = getBillsByRange(startDate, endDate);
                break;
            case "去年":
                startDate = timeHelper.getLastYearFirstDayDate();
                endDate = timeHelper.getLastYearLastDayDate();
                billList = getBillsByRange(startDate, endDate);
                break;
            default:
        }
        convertData();
    }

    /**
     * Order within enquiry range
     */
    private ArrayList<Bill> getBillsByRange(int startDate, int endDate) {
        ArrayList<Bill> billList;
        billList = new DbHelper().queryByRange(startDate, endDate);
        return billList;
    }

    private ArrayList<Bill> loadCustomData() {
        DbHelper dbHelper = new DbHelper();

        int startDate = customOperation.getStartDate();
        int endDate = customOperation.getEndDate();

        if (startDate == 0 && endDate == 0) {
            showNoSelectionToast();
            return null;
        }
        // If there is no end date,
        // Manually give a larger value
        if (endDate == 0) {
            endDate = 20501232;
        }
        return dbHelper.queryByRange(startDate, endDate);
    }

    private void showNoSelectionToast() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toasty.error(getContext(), "No statistical time interval was selected").show();
            }
        });
    }

    /**
     * Bill conversion
     */
    private void convertData() {
        ChartStat chartStat = new ChartStat();
        fullChartStat(chartStat, billList);
        fullChartStatTimeQuantum(chartStat);

        statDataSet.clear();// You may add data repeatedly when you customize your time period
        statDataSet.add(chartStat);// Each page has a default graph
        if (billList == null || billList.isEmpty()) {

        } else {
            for (Bill bill : billList) {
                Stat stat = new Stat();
                stat.bill = bill;
                statDataSet.add(stat);
            }
        }
    }

    /**
     * Fill in the statistical chart data
     */
    private void fullChartStat(ChartStat chartStat, ArrayList<Bill> billList) {
        chartStat.fullData(billList);
    }

    /**
     * Fill in the statistical chart "time"
     */
    private void fullChartStatTimeQuantum(ChartStat chartStat) {
        TimeHelper timeHelper = TimeHelper.getInstance();
        if (type.equals("今天")) {
            chartStat.setTimeQuantum(TimeUtils.stampToDate(System.currentTimeMillis(), "M-d yyyy"));

        } else if (type.equals("昨天")) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            chartStat.setTimeQuantum(TimeUtils.stampToDate(calendar.getTimeInMillis(), "M-d yyyy"));

        } else if (type.equals("本周")) {
            int startDate = timeHelper.getThisWeekFirstDayDate();
            int endDate = timeHelper.getIntTodayDate();
            formatTimeQuantum(chartStat, startDate, endDate);

        } else if (type.equals("上周")) {
            int startDate = TimeHelper.getInstance().getLastWeekFirstDayDate();
            int endDate = TimeHelper.getInstance().getLastWeekLastDayDate();
            formatTimeQuantum(chartStat, startDate, endDate);
        } else if (type.equals("本月")) {
            int startDate = TimeHelper.getInstance().getThisMonthFirstDayDate();
            int endDate = TimeHelper.getInstance().getIntTodayDate();
            formatTimeQuantum(chartStat, startDate, endDate);

        } else if (type.equals("上月")) {
            int startDate = TimeHelper.getInstance().getLastMonthFirstDayDate();
            int endDate = TimeHelper.getInstance().getLastMonthLastDayDate();
            formatTimeQuantum(chartStat, startDate, endDate);

        } else if (type.equals("今年")) {
            int startDate = TimeHelper.getInstance().getThisYearFirstDayDate();
            int endDate = TimeHelper.getInstance().getIntTodayDate();
            formatTimeQuantum(chartStat, startDate, endDate);

        } else if (type.equals("去年")) {
            int startDate = TimeHelper.getInstance().getLastYearFirstDayDate();
            int endDate = TimeHelper.getInstance().getLastYearLastDayDate();
            formatTimeQuantum(chartStat, startDate, endDate);
        }
    }

    private void formatTimeQuantum(ChartStat chartStat, int startDate, int endDate) {
        TimeHelper timeHelper = TimeHelper.getInstance();
        chartStat.setTimeQuantum(timeHelper.formatStatChartDate(startDate) + " - " +
                timeHelper.formatStatChartDate(endDate));
    }

    /**
     * Custom page data refresh
     */
    public void notifyCustomData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                moveToCustomPage();
                billList = loadCustomData();
                convertData();
                notifyView();
            }
        }).start();
    }

    /**
     * Move to custom time section page
     */
    private void moveToCustomPage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                customOperation.moveToCustom();
            }
        });
    }

    /**
     * Refresh interface
     */
    private void notifyView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    // recyclerView Click event callback
    @Override
    public void onItemClick(StatViewAdapter.StatViewHolder holder, Stat stat) {

    }

    @Override
    public void onItemLongClick(StatViewAdapter.StatViewHolder holder, final Stat stat) {
        final Bill bill = stat.bill;
        if (bill == null) {
            return;
        }
        String massage = bill.getTitle() + "  (" + bill.getMainType() + bill.getMoney() + "pounds)";
        new DialogHelper().showNormalDialog((AppCompatActivity) getActivity(),
                "Are you sure you want to delete?", massage, "YES", "NO", new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        deleteItem(stat, bill);
                    }
                });
    }

    /**
     * Delete operation
     */
    private void deleteItem(Stat stat, Bill bill) {
        removeOnCache(stat);
        updateChartData();
        notifyDataChanged();
        deleteInDb(bill);

        StatActivity statActivity = (StatActivity) getActivity();
        statActivity.setChanged(true);
    }

    /**
     * Remove from cache
     */
    private void removeOnCache(Stat stat) {
        billList.remove(stat.bill);
        recyclerViewAdapter.getDatas().remove(stat);
    }

    /**
     * Reload statistical chart data
     */
    private void updateChartData() {
        ChartStat chartStat = new ChartStat();
        chartStat.fullData(billList);
        ChartStat oldStat = (ChartStat) recyclerViewAdapter.getDatas().get(0);
        chartStat.setTimeQuantum(oldStat.getTimeQuantum());
        recyclerViewAdapter.getDatas().set(0, chartStat);
    }

    /**
     * Refresh recyclerView
     */
    private void notifyDataChanged() {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Delete in database
     *
     * @param bill
     */
    private void deleteInDb(Bill bill) {
        new DbHelper().delete(bill);
    }

}