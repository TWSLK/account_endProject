package com.account.work.model.stat;

import com.account.work.app.Cache;
import com.account.work.model.Bill;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JavaBean of statistical graph data on statistical page
 */

public class ChartStat extends Stat {
    /**
     * Less than 2% is classified as other
     */
    private static final String OTHER = "其他";
    /**
     * Less than limit
     */
    private static final float limit = 2f / 100;

    /**
     * Total
     */
    private float totalValue;
    /**
     * Time periods of data statistics
     */
    private String timeQuantum;

    private List<PieEntry> entries = new ArrayList<PieEntry>();
    private List<Integer> colors = new ArrayList<Integer>();


    public String getTimeQuantum() {
        return timeQuantum;
    }

    public ChartStat setTimeQuantum(String timeQuantum) {
        this.timeQuantum = timeQuantum;
        return this;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public ChartStat setTotalValue(float totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public ChartStat setColors(List<Integer> colors) {
        this.colors = colors;
        return this;
    }

    public List<PieEntry> getEntries() {
        return entries;
    }

    public ChartStat setEntries(List<PieEntry> entries) {
        this.entries = entries;
        return this;
    }

    /**
     * Fill in data
     */
    public void fullData(ArrayList<Bill> billList) {
        if (billList == null) {
            return;
        }
        HashMap<String, Float> soleTypes = new HashMap<>();
        ArrayList<String> typeKeys1 = new ArrayList<>();
        ArrayList<String> typeKeys2 = new ArrayList<>();

        // Unique type
        for (Bill bill1 : billList) {
            totalValue += bill1.getMoney();
            Float value = soleTypes.get(bill1.getMinorType());
            if (value == null) {
                soleTypes.put(bill1.getMinorType(), bill1.getMoney());
                typeKeys1.add(bill1.getMinorType());
            } else {
                soleTypes.put(bill1.getMinorType(), bill1.getMoney() + value);
            }
        }

        // Merge <2%
        typeKeys2.addAll(typeKeys1);

        for (String key : typeKeys1) {
            Float value = soleTypes.get(key);
            if (value / totalValue < limit) { // Find less than 2%
                System.out.println("归并" + key);

                if (!typeKeys2.contains(OTHER)) {
                    typeKeys2.add(OTHER);
                }
                if (soleTypes.get(OTHER) == null) {
                    soleTypes.put(OTHER, value);
                } else {
                    soleTypes.put(OTHER, soleTypes.get(OTHER) + value);
                }

                soleTypes.remove(key);
                typeKeys2.remove(key);
            }
        }

        for (String key : typeKeys2) {
            entries.add(new PieEntry(soleTypes.get(key), key));
            colors.add(Cache.getColorByType(key));
        }
    }
}
