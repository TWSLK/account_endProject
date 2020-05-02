package com.account.work.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Time processing tool
 */

public class TimeUtils {

    /**
     * Convert time into time stamp
     */
    public static long dateToStamp(String time, String format) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return ts;
    }

    /**
     * Convert timestamp to time
     */
    public static String stampToDate(long time, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * Convert timestamp to time
     */
    public static String stampToDate(int y, int m, int d, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(y, m, d);
        res = simpleDateFormat.format(date);
        return res;
    }

}
