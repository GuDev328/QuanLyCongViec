package com.example.quanlycongviec.StatisticAction;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DateAxisFormatter extends ValueFormatter {

    private ArrayList<Date> dates;
    private SimpleDateFormat dateFormat;

    public DateAxisFormatter(ArrayList<Date> dates) {
        this.dates = dates;
        // Định dạng ngày mong muốn
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value;
        if (index >= 0 && index < dates.size()) {
            Date date = dates.get(index);
            return dateFormat.format(date); // Chuyển đổi Date thành chuỗi định dạng
        }
        return ""; // Trả về chuỗi rỗng nếu giá trị không hợp lệ
    }
}
