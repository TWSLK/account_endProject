package com.account.work.app;

import android.graphics.Color;
import android.text.TextUtils;

import com.account.work.R;
import com.account.work.utils.ArrayUtils;

import java.util.HashMap;

/**
 * Role: the global cache is used to cache some data that is used by the entire program when it is running
 * For example: icons, bills, types (shopping, alcohol, tobacco, payroll,..., etc.)
 * Benefits: avoid caching each application to regain memory and frequent memory usage after caching
 */

public class Cache {

    /**
     * Expenditure type
     */
    public static String[] monorTypeTitleExpend;
    /**
     * Expenditure type icon
     */
    public static int[] iconsIdExpend;
    /**
     * For coloring
     */
    public static int[] tintColorsExpend;
    /**
     * Income type
     */
    public static String[] monorTypeTitleIncome;
    /**
     * Revenue type icon
     */
    public static int[] iconsIdIncome;
    /**
     * Income coloring
     */
    public static int[] tintColorsIncome;

    private final static HashMap<String, Integer> colorSet = new HashMap<>();
    private final static HashMap<String, Integer> iconSet = new HashMap<>();

    /**
     * Month
     */
    public static String[] monthHan = {
            " ", "一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "十二月"};

    public static void init() {
        monorTypeTitleExpend = ArrayUtils.getStringArray(R.array.minor_type_expend);
        iconsIdExpend = ArrayUtils.getIds(R.array.minor_type_drawable_ids_expend);
        tintColorsExpend = ArrayUtils.getIntArray(R.array.minor_type_tint_expend);
        monorTypeTitleIncome = ArrayUtils.getStringArray(R.array.minor_type_income);
        iconsIdIncome = ArrayUtils.getIds(R.array.minor_type_drawable_ids_income);
        tintColorsIncome = ArrayUtils.getIntArray(R.array.minor_type_tint_income);

        initColorSet();
        initIconSet();
    }

    private static void initIconSet() {
        for (int i = 0; i < monorTypeTitleExpend.length; i++) {
            iconSet.put(monorTypeTitleExpend[i], iconsIdExpend[i]);
        }
        for (int i = 0; i < monorTypeTitleIncome.length; i++) {
            iconSet.put(monorTypeTitleIncome[i], iconsIdIncome[i]);
        }
    }

    private static void initColorSet() {
        for (int i = 0; i < monorTypeTitleExpend.length; i++) {
            colorSet.put(monorTypeTitleExpend[i], tintColorsExpend[i]);
        }
        for (int i = 0; i < monorTypeTitleIncome.length; i++) {
            colorSet.put(monorTypeTitleIncome[i], tintColorsIncome[i]);
        }
    }

    public static int getColorByType(String s) {
        if (s.equals("Other") || TextUtils.isEmpty(s)){
            return Color.GRAY;
        }
        Integer integer = colorSet.get(s);
        if(integer == null){
            return Color.GRAY;
        }
        return integer.intValue();
    }

    public static int getIconIdByType(String s) {
        return iconSet.get(s);
    }
}
