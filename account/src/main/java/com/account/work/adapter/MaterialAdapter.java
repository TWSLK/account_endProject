package com.account.work.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.adapter.base.RecyclerBaseAdapter;
import com.account.work.app.Cache;
import com.account.work.helper.TimeHelper;
import com.account.work.model.Bill;
import com.account.work.utils.TimeUtils;
import com.account.work.widght.ColorImageView;

import java.util.Calendar;
import java.util.List;

/**
 * The main interface displays the adapter of the month's bill
 */

public class MaterialAdapter extends RecyclerBaseAdapter<Bill> {

    private int date;
    private String year;
    private String month;
    private String day;

    public MaterialAdapter(@LayoutRes int itemLayoutId, @NonNull List<Bill> data) {
        super(itemLayoutId, data);
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(getData().get(position).getNotes())) {
            return 8;
        } else {
            if (getData().get(position).getMainType().equalsIgnoreCase("Income")) {
                return 6;
            }
            if (getData().get(position).getMainType().equalsIgnoreCase("expenditure")) {
                return 7;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getLayoutId(int type) {
        switch (type){
            case 6:
                return R.layout.item_material_in;
            case 7:
                return R.layout.item_material_out;
            case 8:
                return R.layout.item_material_customer;
        }
        return super.getLayoutId(type);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        Bill bill = getData().get(position);
        date = bill.getDate();

        String strDate = String.valueOf(date);
        year = strDate.substring(0, 4);
        month = strDate.substring(4, 6);
        day = strDate.substring(6, 8);

        float money = bill.getMoney();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(1);

        holder.setText(R.id.tv_title, bill.getTitle());
        holder.setText(R.id.tv_money, nf.format(money) + " pounds");
        TextView textView1 = (TextView) holder.itemView.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) holder.itemView.findViewById(R.id.tv_money);

        if (getItemViewType(position) == 6) {
            textView1.setTextColor(0xaa16b55b);
            textView2.setTextColor(0xaa16b55b);
        }else if(getItemViewType(position) == 7) {
            textView1.setTextColor(0xaab52e16);
            textView2.setTextColor(0xaab52e16);
        } else if(getItemViewType(position) == 8){
            holder.setText(R.id.tv_note, bill.getNotes());
            if(getData().get(position).getMainType().equalsIgnoreCase("Income")){
                textView1.setTextColor(0xaa16b55b);
                textView2.setTextColor(0xaa16b55b);
            }else{
                textView1.setTextColor(0xaab52e16);
                textView2.setTextColor(0xaab52e16);
            }
        }

        // Setting mainType
        holder.setText(R.id.tv_main_type, bill.getMainType().equals("expenditure") ? "expenditure" : "income");
        // If it is today, it will show the hours
        if (date == TimeHelper.getInstance().getIntTodayDate()) {
            String time = TimeUtils.stampToDate(bill.getAddTime(), "HH:mm");
            holder.setText(R.id.tv_date, time);
        } else if (date == TimeHelper.getInstance().getIntYesterDayDate()) {
            holder.setText(R.id.tv_date, "Yesterday");
        } else if (year.equals(Calendar.getInstance().get(Calendar.YEAR) + "")) {
            holder.setText(R.id.tv_date, month + "-" + day + "-");
        } else {
            holder.setText(R.id.tv_date, year + "-" + month + "-" + day + "-");
        }
        setIcon(holder, bill, position);
        holder.getView(R.id.iv_notes).setVisibility(
                TextUtils.isEmpty(bill.getNotes()) ? View.INVISIBLE : View.VISIBLE);
    }


    private void setIcon(ViewHolder holder, Bill bill, int position) {
        ColorImageView ivIcon = holder.getView(R.id.iv_type_icon);
        ivIcon.setImageResource(Cache.getIconIdByType(bill.getMinorType()));
        ivIcon.setBgColor(Cache.getColorByType(bill.getMinorType()));

    }
}
