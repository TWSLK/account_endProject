package com.account.work.model;

/**
 * Bill display basic type
 */

public class MinorType {
    /**
     * 类型名称：购物、烟酒、、、
     */
    private String typeName;
    /**
     * 类型的图标
     */
    private int typeIconId;
    /**
     * 圆形背景颜色
     */
    private int tintColor = -1;

    public int getTintColor() {
        return tintColor;
    }

    public MinorType setTintColor(int tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public MinorType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public int getTypeIconId() {
        return typeIconId;
    }

    public MinorType setTypeIconId(int typeIconId) {
        this.typeIconId = typeIconId;
        return this;
    }
}
