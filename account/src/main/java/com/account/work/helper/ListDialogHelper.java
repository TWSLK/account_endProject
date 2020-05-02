package com.account.work.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.work.R;

import java.util.ArrayList;

/**
 * Popup ListView type dialog box
 */

public class ListDialogHelper {
    public void showListDialog(Context activityContext, ArrayList<Item> dataList, final ItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);

        builder.setAdapter(new HomeLongClickAdapter(activityContext, dataList, R.layout.item_list_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public interface ItemClickListener {
        void onClick(DialogInterface dialog, int which);
    }

    public class HomeLongClickAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Item> titles;
        private int itemLayoutId;

        public HomeLongClickAdapter(Context context, ArrayList<Item> titles, @LayoutRes int itemLayoutId) {
            this.context = context;
            this.titles = titles;
            this.itemLayoutId = itemLayoutId;

        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parent) {
            ViewHoder hoder;
            if (contentView == null) {
                contentView = View.inflate(context, itemLayoutId, null);
                hoder = new ViewHoder();
                hoder.tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
                hoder.ivIcon = (ImageView) contentView.findViewById(R.id.iv_icon);
                contentView.setTag(hoder);
            } else {
                hoder = (ViewHoder) contentView.getTag();
            }

            hoder.ivIcon.setImageBitmap(titles.get(position).bitmap);
            hoder.tvTitle.setText(titles.get(position).title);

            return contentView;
        }
    }

    public static class ViewHoder {
        public TextView tvTitle;
        public ImageView ivIcon;
    }

    public static class Item {
        private String title;
        private Bitmap bitmap;

        public String getTitle() {
            return title;
        }

        public Item setTitle(String title) {
            this.title = title;
            return this;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public Item setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }
    }
}
