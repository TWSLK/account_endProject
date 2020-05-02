package com.account.work.helper;

import com.account.work.utils.TimeUtils;

/**
 * <p>Check date</p>
 * <P>You can't choose the future date</P>
 * Explanation: check for adding pages to select dates
 * 1.------You can't choose the future date
 * 2.------If it is today or return to Chinese characters: today, yesterday
 * 3.------If it's the other day, return the exact date
 */

public class DateCheckHelper {
    private Listener listener;
    int year, month, day;

    public interface Listener {
        void succeed(int date, int year, int month, int dayOfMonth, String name);

        void fail();
    }

    public DateCheckHelper(int year, int month, int day, Listener listener) {
        this.listener = listener;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateCheckHelper start() {
        String month = String.valueOf(this.month);
        String day = String.valueOf(this.day);

        month = month.length() == 1 ? "0" + month : month;
        day = day.length() == 1 ? "0" + day : day;

        int selectTime = Integer.decode(year + month + day);
        String nowTimeText = TimeUtils.stampToDate(System.currentTimeMillis(), "yyyyMMdd");
        int nowTime = Integer.decode(nowTimeText);

        if (selectTime > nowTime) {
            listener.fail();
        } else {
            String name = "";
            if (nowTime == selectTime) {
                name = "今天";
            } else if (nowTime - selectTime == 1) {
                name = "昨天";
            }

            listener.succeed(selectTime, this.year, this.month, this.day, name);
        }
        return this;
    }

}
