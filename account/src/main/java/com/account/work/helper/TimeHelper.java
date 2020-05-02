package com.account.work.helper;

import java.util.Calendar;

/**
 * Time helper for this application
 */

public class TimeHelper {
    private static class Holder {
        public static final TimeHelper instance = new TimeHelper();
    }

    public static TimeHelper getInstance() {
        return Holder.instance;
    }

    /**
     * Get today's day and month int
     *
     * @return 20170923
     */
    public int getIntTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return getIntDate(calendar);
    }

    /**
     * Get the date of yesterday int
     *
     * @return 20170923
     */
    public int getIntYesterDayDate() {
        Calendar calendar = Calendar.getInstance();
        // Add the date back one day. The integer pushes forward and the negative moves forward
        calendar.add(Calendar.DATE, -1);
        return getIntDate(calendar);
    }

    /**
     * Get the date of the first day of the week
     * calendar.getFirstDayOfWeek()
     *
     * @return 20171029
     */
    public int getThisWeekFirstDayDate() {
        Calendar calendar = Calendar.getInstance();
        int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -todayOfWeek + 1);

        int date = getIntDate(calendar);
        return date;
    }

    /**
     * Get the date of the last day of last week
     *
     * @return 20171029
     */
    public int getLastWeekLastDayDate() {
        Calendar calendar = Calendar.getInstance();
        int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -todayOfWeek + 1 - 1);
        int date = getIntDate(calendar);
        System.out.println(date);
        return date;
    }

    /**
     * Get the date of the first day of last week
     *
     */
    public int getLastWeekFirstDayDate() {
        Calendar calendar = Calendar.getInstance();
        int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -todayOfWeek + 1 - 7);
        int date = getIntDate(calendar);
        System.out.println(date);
        return date;
    }

    public int getThisYearFirstDayDate(){
        return Calendar.getInstance().get(Calendar.YEAR) * 10000 + 100 + 1;
    }

    public int getLastYearFirstDayDate(){
        return (Calendar.getInstance().get(Calendar.YEAR) - 1) * 10000 + 100 + 1;
    }

    public int getLastYearLastDayDate() {
        return (Calendar.getInstance().get(Calendar.YEAR) - 1) * 10000 + 1231;
    }


    /**
     * Get the date of the first day of the month
     *
     * @return
     */
    public int getThisMonthFirstDayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year * 10000 + month * 100 + 1;
    }

    public int getLastMonthFirstDayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year * 10000 + month * 100 + 1;
    }

    public int getLastMonthLastDayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);

        int MaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year * 10000 + month * 100 + MaxDay;
    }

    /**
     * Statistical page cell date formatting
     *
     * @return
     */
    public String formatStatItemDate(int yearMonthDay) {
        String month = String.valueOf(yearMonthDay % 10000 / 100);
        String day = String.valueOf(yearMonthDay % 10000 % 100);
        return month + "-" + day;
    }

    /**
     * Statistical page, statistical chart, date conversion
     *
     * @return
     */
    public String formatStatChartDate(int yearMonthDay) {
        String year = String.valueOf(yearMonthDay / 10000);
        String month = String.valueOf(yearMonthDay % 10000 / 100);
        String day = String.valueOf(yearMonthDay % 10000 % 100);
        return month + "-" + day + " " + year;
    }

    /**
     * Format to int type date
     */
    public int formatDate(int year, int month, int dayOfMonth) {
        return year*10000+month*100+dayOfMonth;
    }

    private int getIntDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year * 10000 + month * 100 + day;
    }
}
