package com.account.work.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.app.Cache;
import com.account.work.helper.PChartHelper;
import com.account.work.helper.TimeHelper;
import com.account.work.model.Bill;
import com.account.work.model.stat.ChartStat;
import com.account.work.model.stat.Stat;
import com.account.work.utils.LogUtils;
import com.account.work.utils.MatcherUtils;
import com.account.work.widght.ColorImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

/**
 * Statistical pages display statistical charts and bills recyclerView的adapter
 */
public class StatViewAdapter extends RecyclerView.Adapter<StatViewAdapter.StatViewHolder> {

    List<Stat> datas;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private List<PieEntry> entries;
    private List<Integer> colors;

    public List<Stat> getDatas() {
        return datas;
    }

    public StatViewAdapter(List<Stat> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public StatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false);
                break;

            case TYPE_CELL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_small, parent, false);
                break;
        }

        final StatViewHolder holder = new StatViewHolder(viewType, view);

        setupEvent(view, holder);

        return holder;
    }

    private void setupEvent(View view, final StatViewHolder holder) {
        if (itemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder, datas.get(holder.getAdapterPosition()));
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onItemLongClick(holder, datas.get(holder.getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public static class StatViewHolder extends RecyclerView.ViewHolder {
        PieChart pieChart;
        TextView tvTotalValue;
        TextView tvTimeQuantum;

        TextView tvTitle;
        TextView tvMoney;
        TextView tvDate;
        ColorImageView ivIcon;

        public StatViewHolder(int viewType, View itemView) {
            super(itemView);
            switch (viewType) {
                case TYPE_HEADER:
                    pieChart = (PieChart) itemView.findViewById(R.id.pie_chart);
                    tvTotalValue = (TextView) itemView.findViewById(R.id.tv_total);
                    tvTimeQuantum = (TextView) itemView.findViewById(R.id.tv_time_quantum);
                    break;
                case TYPE_CELL:
                    tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
                    tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                    tvDate = (TextView) itemView.findViewById(R.id.tv_date);
                    ivIcon = (ColorImageView) itemView.findViewById(R.id.iv_type_icon);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(StatViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                bindChartItem(holder);
                break;
            case TYPE_CELL:
                bindCellItem(holder, position);
                break;
        }
    }

    private void bindCellItem(StatViewHolder holder, int position) {
        Stat stat = datas.get(position);
        Bill bill = stat.bill;
        holder.ivIcon.setImageResource(R.drawable.ic_game);
        holder.tvMoney.setText(bill.getMoney() + "");
        holder.tvTitle.setText(bill.getTitle());
        holder.tvDate.setText(TimeHelper.getInstance().formatStatItemDate(bill.getDate()));

        holder.ivIcon.setImageResource(Cache.getIconIdByType(bill.getMinorType()));
        holder.ivIcon.setBgColor(Cache.getColorByType(bill.getMinorType()));
    }

    private void bindChartItem(StatViewHolder holder) {
        System.out.println("bindChartItem  ");
        ChartStat stat = (ChartStat) datas.get(0);
        entries = ((ChartStat) datas.get(0)).getEntries();
        colors = ((ChartStat) datas.get(0)).getColors();

        bindRim(holder, stat);
        if (entries == null || entries.isEmpty()) {
            LogUtils.w(this, "The statistics are empty");
            new PChartHelper(holder.pieChart, entries, colors).invalidate();
            holder.pieChart.clear();
        } else {
            new PChartHelper(holder.pieChart, entries, colors).invalidate();
        }
    }

    /**
     * Setting the perimeter of the statistical chart
     */
    private void bindRim(StatViewHolder holder, ChartStat stat) {
        holder.tvTotalValue.setText(MatcherUtils.getMoneyFormat().format(stat.getTotalValue()));
        holder.tvTimeQuantum.setText(stat.getTimeQuantum());

    }

    protected ItemClickListener itemClickListener;

    public StatViewAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onItemClick(StatViewHolder holder, Stat stat);

        void onItemLongClick(StatViewHolder holder, Stat stat);
    }
}