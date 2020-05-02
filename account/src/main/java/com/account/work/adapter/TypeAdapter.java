package com.account.work.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.account.work.R;
import com.account.work.adapter.base.RecyclerBaseAdapter;
import com.account.work.model.MinorType;
import com.account.work.widght.ColorImageView;

import java.util.List;

/**
 * Add page type icon to display RecyclerView of adapter
 */

public class TypeAdapter extends RecyclerBaseAdapter<MinorType> {
    public TypeAdapter(@LayoutRes int itemLayoutId, @NonNull List<MinorType> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        TextView tvTypeName = holder.getView(R.id.tv_type_name);
        ColorImageView ivTypeIcon = holder.getView(R.id.iv_type_icon);

        MinorType minorType = getData().get(position);

        int tintColor = minorType.getTintColor();
        tvTypeName.setText(minorType.getTypeName());
        ivTypeIcon.setImageResource(getData().get(position).getTypeIconId());

        if (tintColor != -1) {
            tvTypeName.setTextColor(tintColor);
            ivTypeIcon.setColorFilter(Color.WHITE);
            ivTypeIcon.setBgColor((tintColor));
        }
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        holder.getView(R.id.ll_real_type_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, holder);
            }
        });

        holder.getView(R.id.ll_real_type_item).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemLongClick(v, holder);
                return true;
            }
        });
    }
}
