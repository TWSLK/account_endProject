package com.account.work.helper;

import android.graphics.Color;

import com.account.work.R;
import com.account.work.app.app;
import com.account.work.utils.depend.MyValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

/**
 * Data graph settings data modification style tool
 * Explanation: for the statistical page service, modifying the style of the statistical chart can be operated here
 */

public class PChartHelper {
    PieChart pieChart;
    List<PieEntry> entries;
    List<Integer> colors;

    public PChartHelper(PieChart pieChart, List<PieEntry> entries, List<Integer> colors) {
        this.pieChart = pieChart;
        this.entries = entries;
        this.colors = colors;

        style();
        init();
    }

    private void style() {
        pieChart.setNoDataText("没有数据");// No data display
        pieChart.setNoDataTextColor(app.context.getResources().getColor(R.color.brown));// No data display

        pieChart.setTouchEnabled(false);// Do not accept click events
        Description desc = new Description(); // Setting description lable
        desc.setText(""); // Setting description lable
        pieChart.setDescription(desc); // Setting description lable

        pieChart.setUsePercentValues(true);// Set percentage form display
        pieChart.setEntryLabelTextSize(12);// Setting item lable display style
        pieChart.setEntryLabelColor(Color.TRANSPARENT);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawCenterText(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationAngle(0);
        // Enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.animateY(1000, Easing.EasingOption.EaseOutCubic);

        setLegend();
    }

    private void setLegend() {
        Legend legend = pieChart.getLegend();// Legend text
        legend.setForm(Legend.LegendForm.SQUARE);// Shape
        legend.setWordWrapEnabled(true);// Newline
    }

    private PChartHelper init() {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueTextColor(Color.GRAY);// Data value display style
        dataSet.setValueTextSize(14);// Data value display style
        dataSet.setColors(colors);// Item background color
        dataSet.setSliceSpace(0f);// Set the distance between pie graphs

        dataSet.setSelectionShift(10f);// The length of a part of the area selected
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(Color.GRAY);
        dataSet.setValueLineWidth(1);

        setData(dataSet);
        return this;
    }

    private void setData(PieDataSet dataSet) {
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new MyValueFormatter());// Formatted data display
        pieChart.setData(pieData);
    }

    public void invalidate() {
        pieChart.invalidate();
    }
}
