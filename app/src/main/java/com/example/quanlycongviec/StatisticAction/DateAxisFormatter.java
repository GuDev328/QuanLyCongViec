package com.example.quanlycongviec.StatisticAction;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class DateAxisFormatter extends ValueFormatter {

    private ArrayList<String> dates;

    public DateAxisFormatter(ArrayList<String> dates) {
        this.dates = dates;
    }

    @Override
    public String getFormattedValue(float value) {
        if (value >= 0 && value < dates.size()) {
            return dates.get((int) value);
        }
        return "";
    }
}
