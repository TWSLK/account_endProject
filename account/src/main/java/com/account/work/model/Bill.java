package com.account.work.model;

import cn.bmob.v3.BmobObject;

/**
 * JavaBean of the bill
 */

public class Bill extends BmobObject{
    /**
     * Title
     */
    String title;
    /**
     * Amount of money
     */
    Float money;
    /**
     * <p>Main types</p>
     * <P>Income, expenditure</P>
     */
    String mainType;

    /**
     * <P>Second types</P>
     * <P>Dining, entertainment, shopping...</P>
     */
    String minorType;
    /**
     * Date
     */
    Integer date;
    /**
     * Remarks
     */
    String notes;

    /**
     * The unsynchronized sign is 0<br>
     * The synchronization flag is 1
     */
    Integer sync;

    Long addTime;

    public long getAddTime() {
        return addTime;
    }

    public Bill setAddTime(long addTime) {
        this.addTime = addTime;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Bill setTitle(String title) {
        this.title = title;
        return this;
    }

    public float getMoney() {
        return money;
    }

    public Bill setMoney(float money) {
        this.money = money;
        return this;
    }

    public String getMainType() {
        return mainType;
    }

    public Bill setMainType(String mainType) {
        this.mainType = mainType;
        return this;
    }

    public String getMinorType() {
        return minorType;
    }

    public Bill setMinorType(String minorType) {
        this.minorType = minorType;
        return this;
    }

    public int getDate() {
        return date;
    }

    public Bill setDate(int date) {
        this.date = date;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Bill setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public int getSync() {
        return sync;
    }

    public Bill setSync(int sync) {
        this.sync = sync;
        return this;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "title='" + title + '\'' +
                ", money=" + money +
                ", date=" + date +
                '}';
    }
}
