package com.example.quanlycongviec;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;

    private TextView tvCompletedTasks, tvPendingTasks;
    private DBHelper dbHelper;


    public StatisticFragment() {
        // Required empty public constructor
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

        // Initialize the views
        tvCompletedTasks = view.findViewById(R.id.tvCompletedTasks);
        tvPendingTasks = view.findViewById(R.id.tvPendingTasks);

        // Initialize the database helper
        dbHelper = new DBHelper(requireContext());
        // Fetch task data
        updateTaskStatistics();
        // Ánh xạ view
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        // Cấu hình Bar Chart
        setupBarChart();

        // Cấu hình Pie Chart
        setupPieChart();

        return view;
    }
    private void updateTaskStatistics() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Fetch completed tasks count
        Cursor cursorCompleted = db.rawQuery(
                "SELECT COUNT(*) FROM Task WHERE status = 1", null);
        if (cursorCompleted != null && cursorCompleted.moveToFirst()) {
            int completedCount = cursorCompleted.getInt(0);
            tvCompletedTasks.setText("Nhiệm vụ đã hoàn thành: " + completedCount);
            cursorCompleted.close();
        }

        // Fetch pending tasks count
        Cursor cursorPending = db.rawQuery(
                "SELECT COUNT(*) FROM Task WHERE status = 0", null);
        if (cursorPending != null && cursorPending.moveToFirst()) {
            int pendingCount = cursorPending.getInt(0);
            tvPendingTasks.setText("Nhiệm vụ chưa hoàn thành: " + pendingCount);
            cursorPending.close();
        }
    }
    private void setupBarChart() {
//        ArrayList<BarEntry> entries = dbHelper.getTaskData();
        List<BarEntry> entries = new ArrayList<>();
//        if (entries.isEmpty()) {
//            barChart.setNoDataText("Không có dữ liệu để hiển thị");
//            barChart.invalidate();
//            return;
//        }
        entries.add(new BarEntry(0, 5));  // Ngày đầu tiên hoàn thành 5 nhiệm vụ
        entries.add(new BarEntry(1, 3));  // Ngày thứ hai hoàn thành 3 nhiệm vụ
        entries.add(new BarEntry(2, 0));  // Ngày thứ ba không có nhiệm vụ
        BarDataSet dataSet = new BarDataSet(entries, "Hoàn thành nhiệm vụ hàng ngày");
        dataSet.setColor(Color.BLUE);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f); // Đặt độ rộng cho cột

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();


    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(5, "Đã hoàn thành"));
        entries.add(new PieEntry(1, "Chưa hoàn thành"));

        PieDataSet dataSet = new PieDataSet(entries, "Phân loại nhiệm vụ");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh biểu đồ
    }
}