package com.account.work.model;

import cn.bmob.v3.BmobObject;

/**
 * Budget put javabean
 */

public class Budget extends BmobObject{
    /**
     * Budget limit
     */
    private Float value;
    /**
     * Budget month
     */
    private Integer yearMonth;

    public Float getValue() {
        return value;
    }

    public Budget setValue(float value) {
        this.value = value;
        return this;
    }

    public int getYearMonth() {
        return yearMonth;
    }

    public Budget setYearMonth(int yearMonth) {
        this.yearMonth = yearMonth;
        return this;
    }
}
