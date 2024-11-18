package com.example.quanlycongviec;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.StatisticAction.DateAxisFormatter;
import com.example.quanlycongviec.StatisticAction.StatisticDAO;
import com.example.quanlycongviec.StatisticAction.StatisticStatus;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatisticFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    ImageButton btnShowDatePicker;
    private TextView tvCompletedTasks, tvPendingTasks,txtStart,txtFinish;
    private DBHelper dbHelper;

    private StatisticDAO statisticDAO ;
    private TaskDAO taskDAO;

    public StatisticFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        statisticDAO = new StatisticDAO(getContext());
        taskDAO = new TaskDAO(getContext());
        Button btnFilter = view.findViewById(R.id.btnFilter);
        // Initialize the views
        tvCompletedTasks = view.findViewById(R.id.tvCompletedTasks);
        tvPendingTasks = view.findViewById(R.id.tvPendingTasks);
        btnShowDatePicker = view.findViewById(R.id.showDatePicker);
        txtStart = view.findViewById(R.id.txtStart);
        txtFinish = view.findViewById(R.id.txtFinish);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        // Initialize the database helper
        dbHelper = new DBHelper(getContext());
//        createBarChart(null,null);
        createBarChart(null,null);
        createPieChart(null,null);
        // Fetch task data
        loadStatisticsData(null, null);

//        btnFilter.setOnClickListener(v -> {
//            String startDate = txtStart.getText().toString();
//            String endDate = txtFinish.getText().toString();
//
//            if (startDate.isEmpty() || endDate.isEmpty()) {
//                Toast.makeText(getContext(), "Vui lòng nhập đủ ngày bắt đầu và ngày kết thúc", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            try {
//                // Lấy dữ liệu theo khoảng ngày
//                ArrayList<BarEntry> barEntries = dbHelper.getTaskCountByDateRange(startDate, endDate);
//                ArrayList<String> dates = dbHelper.getTaskDatesByRange(startDate, endDate);
//
//                if (barEntries.isEmpty()) {
//                    Toast.makeText(getContext(), "Không có dữ liệu trong khoảng ngày đã chọn", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Hiển thị dữ liệu lên BarChart
//                BarDataSet barDataSet = new BarDataSet(barEntries, "Số lượng nhiệm vụ");
//                barDataSet.setColor(Color.BLUE);
//                BarData barData = new BarData(barDataSet);
//                barChart.setData(barData);
//
//                // Hiển thị ngày trên trục X
//                XAxis xAxis = barChart.getXAxis();
//                xAxis.setValueFormatter(new DateAxisFormatter(dates));
//                xAxis.setGranularity(1f);
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                // Làm mới biểu đồ
//                barChart.invalidate();
//            } catch (Exception e) {
//                Toast.makeText(getContext(), "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
//            }
//        });

        btnShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }
    private void loadStatisticsData(String startDate, String endDate) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String completedQuery = "SELECT COUNT(*) FROM Task WHERE status = 1";
            String pendingQuery = "SELECT COUNT(*) FROM Task WHERE status = 0";

            // Thêm điều kiện ngày nếu cần
            if (startDate != null && endDate != null) {
                completedQuery += " AND created_date BETWEEN ? AND ?";
                pendingQuery += " AND created_date BETWEEN ? AND ?";
            }

            Cursor completedCursor = db.rawQuery(completedQuery, startDate != null ? new String[]{startDate, endDate} : null);
            completedCursor.moveToFirst();
            int completedCount = completedCursor.getInt(0);
            completedCursor.close();

            Cursor pendingCursor = db.rawQuery(pendingQuery, startDate != null ? new String[]{startDate, endDate} : null);
            pendingCursor.moveToFirst();
            int pendingCount = pendingCursor.getInt(0);
            pendingCursor.close();

            tvCompletedTasks.setText("Nhiệm vụ đã hoàn thành\n" + String.valueOf(completedCount));
            tvPendingTasks.setText("Nhiệm vụ chưa hoàn thành\n" + String.valueOf(pendingCount));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }


    public void showDatePicker(){
        Calendar startDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            startDateCalendar.set(year, month, dayOfMonth);
            DatePickerDialog endDatePickerDialog = new DatePickerDialog(getContext(), (view2, year2, month2, dayOfMonth2) -> {
                endDateCalendar.set(year2, month2, dayOfMonth2);

                if (endDateCalendar.getTimeInMillis() >= startDateCalendar.getTimeInMillis()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String startDateString = dateFormat.format(startDateCalendar.getTime());
                    String finishDateString = dateFormat.format(endDateCalendar.getTime());
                    txtStart.setText(startDateString);
                    txtFinish.setText(finishDateString);
                    createBarChart(startDateString,finishDateString);
//                    createPieChart(startDateString,finishDateString);

                } else {
                    Toast.makeText(getContext(),"Invalid period",Toast.LENGTH_LONG).show();
                }

            }, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH));
            endDatePickerDialog.show();

        }, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.show();
    }

    public void createBarChart(String startDateString,String finishDateString) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<BarEntry> barEntries = dbHelper.getTaskCountByDate();
        // Tạo dữ liệu biểu đồ
        BarDataSet barDataSet = new BarDataSet(barEntries, "Số lượng nhiệm vụ");
        barDataSet.setColor(Color.BLUE); // Màu cột
        BarData barData = new BarData(barDataSet);
        barDataSet.setValueTextColor(Color.WHITE);
        // Gắn dữ liệu vào BarChart
        barChart.setData(barData);


        // Tùy chỉnh trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        ArrayList<String> dates = dbHelper.getTaskDates();
        xAxis.setValueFormatter(new DateAxisFormatter(dates));

        barChart.getAxisRight().setEnabled(false); // Vô hiệu hóa trục bên phải
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(barChart.getYMax());

        barChart.getDescription().setEnabled(false); // Tắt phần mô tả góc dưới bên phải (nếu có)
        barChart.getLegend().setTextColor(Color.WHITE);
        // Làm mới biểu đồ
        barChart.invalidate();
    }

    private void createPieChart(String startDateString,String finishDateString) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String completedQuery = "SELECT COUNT(*) FROM Task WHERE status = 1";
        String pendingQuery = "SELECT COUNT(*) FROM Task WHERE status = 0";

        // Thêm điều kiện ngày nếu cần
        if (startDateString != null && finishDateString != null) {
            completedQuery += " AND created_date BETWEEN ? AND ?";
            pendingQuery += " AND created_date BETWEEN ? AND ?";
        }

        Cursor completedCursor = db.rawQuery(completedQuery, startDateString != null ? new String[]{startDateString, finishDateString} : null);
        completedCursor.moveToFirst();
        int completedCount = completedCursor.getInt(0);
        completedCursor.close();

        Cursor pendingCursor = db.rawQuery(pendingQuery, startDateString != null ? new String[]{startDateString, finishDateString} : null);
        pendingCursor.moveToFirst();
        int pendingCount = pendingCursor.getInt(0);
        pendingCursor.close();


        List<PieEntry> entries = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(entries, "");
        entries.add(new PieEntry(completedCount, "Đã hoàn thành\t"));
        entries.add(new PieEntry(pendingCount, "Chưa hoàn thành"));

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setHoleRadius(40f); // Bán kính lỗ bên trong (40% kích thước biểu đồ)
        pieChart.setTransparentCircleRadius(45f); // Bán kính vòng tròn trong suốt bên ngoài lỗ
        pieChart.setDrawHoleEnabled(true); // Bật chế độ hiển thị lỗ
        pieChart.setEntryLabelColor(Color.WHITE); // Đổi màu nhãn (labels)
        pieChart.setEntryLabelTextSize(12f); // Đổi kích thước chữ nhãn

        pieChart.getDescription().setEnabled(false); // Tắt phần mô tả góc dưới bên phải (nếu có)
        pieChart.getLegend().setTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh biểu đồ
    }
    public static String[] convertDates(List<String> dates) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM");

        List<String> formattedDates = new ArrayList<>();

        for (String date : dates) {
            try {
                Date parsedDate = inputFormat.parse(date);
                String formattedDate = outputFormat.format(parsedDate);
                formattedDates.add(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return formattedDates.toArray(new String[0]);
    }
}