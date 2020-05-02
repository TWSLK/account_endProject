package com.account.work.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.adapter.base.RecyclerBaseAdapter;
import com.account.work.app.Cache;
import com.account.work.app.app;
import com.account.work.model.Bill;
import com.account.work.utils.MatcherUtils;

import java.util.List;
import java.util.Random;

/**
 * Adapter for time line RecyclerView
 */

public class TimeLineAdapter extends RecyclerBaseAdapter<Bill> {
    public TimeLineAdapter(@LayoutRes int itemLayoutId, @NonNull List<Bill> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        setTimeLime(holder, position);
        Bill bill = getData().get(position);
        int date = bill.getDate();
        String day = (date + "").substring(6, 8);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvTypeName = holder.getView(R.id.tv_type_name);

        float money = bill.getMoney();
        java.text.NumberFormat nf = MatcherUtils.getMoneyFormat();

        holder.setText(R.id.tv_time_line_title, bill.getTitle());
        holder.setText(R.id.tv_day, day);

        setTypeName(bill,tvTypeName);
        setMoney(bill, tvMoney);
        setIcon(holder, position);
        setYearMonth(holder, position, bill);
    }

    /**
     * Setting type
     */
    private void setTypeName(Bill bill, TextView tvTypeName) {
        int color = bill.getMainType().equals(app.context.getString(R.string.income)) ?
                app.context.getResources().getColor(R.color.colorPrimary) :
                app.context.getResources().getColor(R.color.colorAccent);
        tvTypeName.setTextColor(color);
        tvTypeName.setText(bill.getMainType());
    }

    /**
     * Set amount
     */
    private void setMoney(Bill bill, TextView money) {
        String billMoney = String.valueOf(MatcherUtils.getMoneyFormat().format(bill.getMoney()));
        SpannableString styledText = new SpannableString("￥" + billMoney);
        styledText.setSpan(new TextAppearanceSpan(app.context, R.style.style0), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(app.context, R.style.style1), 1, billMoney.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        money.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        holder.getView(R.id.ll_item_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder);
                }
            }
        });
        holder.getView(R.id.ll_item_root).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(v, holder);
                }
                return true;
            }
        });
    }

    private void setYearMonth(ViewHolder holder, int position, Bill bill) {
        LinearLayout llYearMonth = holder.getView(R.id.ll_time_line_year_month);
        TextView textView = holder.getView(R.id.tv_time_line_date);
        String year = (bill.getDate() + "").substring(0, 4);//20171212
        String month = (bill.getDate() + "").substring(4, 6);//20171212


        if (position == 0) {
            llYearMonth.setVisibility(View.VISIBLE);
            setYearMonth(textView, year, month);
        } else {
            String lostYearMonth = (getData().get(position - 1).getDate() + "").substring(0, 6);
            if ((year + month).equals(lostYearMonth)) {
                llYearMonth.setVisibility(View.GONE);
            } else {
                llYearMonth.setVisibility(View.VISIBLE);
                setYearMonth(textView, year, month);
            }
        }
    }

    private void setYearMonth(TextView textView, String year, String month) {
        int monthInt = Integer.parseInt(month);
        if (monthInt < 1 || monthInt > 12) {
            throw new RuntimeException("adapter setYearMonth month error");
        }
        textView.setText(year + "  " + Cache.monthHan[monthInt]);
    }

    private void setIcon(ViewHolder holder, int position) {
        ImageView ivImage = holder.getView(R.id.iv_time_line_image);
        Bill bill = getData().get(position);
        String mainType = bill.getMainType();
        String minorType = bill.getMinorType();

        if (mainType.equals(app.context.getString(R.string.income))) {
            switchIncome(ivImage, minorType);
        } else {
            switchExpend(ivImage, minorType);
        }
    }

    private void switchExpend(ImageView imageView, String minor) {
        int resId;
        int resColor;
        switch (minor) {
            case "Shopping":
                resId = R.drawable.ic_shoping;
                break;
            case "Restaurant":
                resId = R.drawable.ic_eat;
                break;
            case "Travel":
                resId = R.drawable.ic_car;
                break;
            case "Custom":
                resId = R.drawable.ic_custom;
                break;
            case "Entertainment":
                resId = R.drawable.ic_game;
                break;
            case "Snacks":
                resId = R.drawable.ic_snacks;
                break;
            case "Alcohol and tobacco":
                resId = R.drawable.ic_tob_wine;
                break;
            case "Lend":
                resId = R.drawable.ic_lend;
                break;
            case "Housing":
                resId = R.drawable.ic_home;
                break;
            case "Tourism":
                resId = R.drawable.ic_travel;
                break;
            case "Communication":
                resId = R.drawable.ic_comm;
                break;
            case "Medical":
                resId = R.drawable.ic_medical;
                break;
            case "Cosmetology":
                resId = R.drawable.ic_beauty;
                break;
            case "Daily Necessities":
                resId = R.drawable.ic_daily;
                break;
            case "Study":
                resId = R.drawable.ic_study;
                break;
            case "Favor":
                resId = R.drawable.ic_feelings;
                break;
            case "Debt":
                resId = R.drawable.ic_debit;
                break;
            case "Digital":
                resId = R.drawable.ic_digital;
                break;
            case "Home Furnishing":
                resId = R.drawable.ic_jiaju;
                break;
            case "Breakfast":
                resId = R.drawable.ic_breakfast;
                break;
            case "Lunch":
            case "Dinner":
                resId = R.drawable.ic_dining_normal;
                break;
            case "Night snack":
                resId = R.drawable.ic_night_snack;
                break;
            case "Ferry":
                resId = R.drawable.ic_ferry;
                break;
            case "Flight":
                resId = R.drawable.ic_flight;
                break;
            case "Transit":
                resId = R.drawable.ic_bus;
                break;
            case "Taxi":
                resId = R.drawable.ic_car;
                break;
            case "Gas":
                resId = R.drawable.ic_refuel;
                break;
            default:
                resId = R.drawable.ic_custom;
                break;
        }
        imageView.setImageResource(resId);

        int rand = new Random().nextInt(10);
        imageView.setColorFilter(Cache.tintColorsExpend[rand]);
        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
        drawable.setStroke(3, Cache.tintColorsExpend[rand]);
    }

    private void switchIncome(ImageView imageView, String minor) {
        int resId;
        switch (minor) {
            case "Salary":
                resId = R.drawable.ic_salary;
                break;
            case "RedPackage":
                resId = R.drawable.ic_red_packet;
                break;
            case "Bonus":
                resId = R.drawable.ic_bonus;
                break;
            case "Part-time job":
                resId = R.drawable.ic_part_time_jab;
                break;
            case "Borrow":
                resId = R.drawable.ic_borrow;
                break;
            case "Custom":
            default:
                resId = R.drawable.ic_custom;
                break;
        }

        imageView.setImageResource(resId);

        int rand = new Random().nextInt(10);
        imageView.setColorFilter(Cache.tintColorsExpend[rand]);
        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
        drawable.setStroke(3, Cache.tintColorsExpend[rand]);
    }

    private void setTimeLime(ViewHolder holder, int position) {
        if (getItemCount() == 1) {
            hideLineFooter(holder);
            goneTimeItem(holder);
        } else {
            if (position == getItemCount() - 1) {
                hideLineFooter(holder);
            } else {
                showLimeFooter(holder);
            }
        }
    }

    private void showLimeFooter(ViewHolder holder) {
        holder.getView(R.id.v_time_line_footer).setVisibility(View.VISIBLE);
    }

    private void showLineHeader(ViewHolder holder) {
        holder.getView(R.id.v_time_line_header).setVisibility(View.VISIBLE);
    }

    private void hideLineFooter(ViewHolder holder) {
        holder.getView(R.id.v_time_line_footer).setVisibility(View.INVISIBLE);
    }

    private void hideLineHeader(ViewHolder holder) {
        holder.getView(R.id.v_time_line_header).setVisibility(View.INVISIBLE);
    }

    private void showTimeItem(ViewHolder holder) {
        holder.getView(R.id.ll_time_line_year_month).setVisibility(View.VISIBLE);
    }

    private void goneTimeItem(ViewHolder holder) {
        holder.getView(R.id.ll_time_line_year_month).setVisibility(View.GONE);
    }
}
