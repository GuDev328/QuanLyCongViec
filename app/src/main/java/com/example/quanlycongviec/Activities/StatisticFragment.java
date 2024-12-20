package com.example.quanlycongviec.Activities;

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

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DB.DBHelper;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.StatisticAction.DateAxisFormatter;
import com.example.quanlycongviec.StatisticAction.StatisticDAO;
import com.example.quanlycongviec.Utils.ShareStore;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    ImageButton btnShowDatePicker;
    private TextView tvCompletedTasks, tvPendingTasks,txtStart,txtFinish;
    private DBHelper dbHelper;
    private int user_id;
    private ShareStore store ;

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
        store= new ShareStore(getContext());
        user_id =Integer.parseInt(store.getValue("user_id", null));
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
        createBarChart(null,null);
        createPieChart();
        // Fetch task data
        loadStatisticsData(null, null,user_id);

        btnFilter.setOnClickListener(v -> {
            String startDate = txtStart.getText().toString();
            String endDate = txtFinish.getText().toString();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ ngày bắt đầu và ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Lấy dữ liệu theo khoảng ngày
                ArrayList<BarEntry> barEntriesCompleted = statisticDAO.getTaskCompleteCountByDateRange(startDate,endDate);
                ArrayList<BarEntry> barEntriesPending = statisticDAO.getTaskPendingCountByDateRange(startDate,endDate);
                ArrayList<Date> dates = statisticDAO.getTaskDatesByRange(startDate, endDate);

                if (barEntriesCompleted.isEmpty() && barEntriesPending.isEmpty()) {
                    Toast.makeText(getContext(), "Không có dữ liệu trong khoảng ngày đã chọn", Toast.LENGTH_SHORT).show();
                    return;
                }

                BarDataSet barDataSetCompleted = new BarDataSet(barEntriesCompleted, "Nhiệm vụ đã hoàn thành");
                barDataSetCompleted.setColor(Color.BLUE); // Màu cột hoàn thành

                BarDataSet barDataSetPending = new BarDataSet(barEntriesPending, "Nhiệm vụ chưa hoàn thành");
                barDataSetPending.setColor(Color.GREEN); // Màu cột chưa hoàn thành

                // Tạo dữ liệu BarData
                BarData barData = new BarData(barDataSetCompleted, barDataSetPending);
                barData.setBarWidth(0.3f); // Độ rộng mỗi cột

                // Gắn dữ liệu vào BarChart
                barChart.setData(barData);

                // Tùy chỉnh trục X
                XAxis xAxis = barChart.getXAxis();
                xAxis.setAxisMinimum(-0.5f);
//                ArrayList<String> dates = dbHelper.getTaskDates(); // Lấy danh sách ngày
                //      ArrayList<String> dates = dbHelper.getTaskDatesByRange(startDateString,finishDateString); // Lấy danh sách ngày
//                ArrayList<Date> dates = statisticDAO.getTaskDates(); // Lấy danh sách ngày

                xAxis.setValueFormatter(new DateAxisFormatter(dates)); // Đặt formatter cho trục X
                xAxis.setGranularity(1f); // Bước nhảy trục X
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setCenterAxisLabels(false);


                // Nhóm các cột lại theo ngày
                barChart.getXAxis().setAxisMinimum(-0.25f); // Đảm bảo không bị lệch khỏi trục
                float groupSpace = 0.2f; // Khoảng cách giữa các nhóm cột
                float barSpace = 0.05f; // Khoảng cách giữa các cột trong 1 nhóm
                float barWidth = 0.3f; // Độ rộng mỗi cột (phải khớp với barData.setBarWidth)



                // Đảm bảo khoảng cách tổng = 1 (groupSpace + 2 * barWidth + barSpace = 1)
                barData.setBarWidth(barWidth);
                barChart.groupBars(0f, groupSpace, barSpace);

                // Tùy chỉnh trục Y
                barChart.getAxisRight().setEnabled(false); // Vô hiệu hóa trục bên phải
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setGranularity(1f);
                leftAxis.setAxisMinimum(0f);

                // Tùy chỉnh khác
                barChart.getDescription().setEnabled(false); // Tắt phần mô tả góc dưới bên phải (nếu có)
                barChart.setFitBars(true); // Tự động căn chỉnh các cột trong biểu đồ

                // Làm mới biểu đồ
                barChart.invalidate();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }
    private void loadStatisticsData(String startDate, String endDate, int userId) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String completedQuery = "SELECT COUNT(*) FROM Task WHERE status = 1 AND user_id = ?";
            String pendingQuery = "SELECT COUNT(*) FROM Task WHERE status = 0 AND user_id = ?";

            // Thêm điều kiện ngày nếu cần
            if (startDate != null && endDate != null) {
                completedQuery += " AND created_date BETWEEN ? AND ?";
                pendingQuery += " AND created_date BETWEEN ? AND ?";
            }

            String[] completedArgs;
            String[] pendingArgs;

            if (startDate != null && endDate != null) {
                completedArgs = new String[]{String.valueOf(userId), startDate, endDate};
                pendingArgs = new String[]{String.valueOf(userId), startDate, endDate};
            } else {
                completedArgs = new String[]{String.valueOf(userId)};
                pendingArgs = new String[]{String.valueOf(userId)};
            }

            Cursor completedCursor = db.rawQuery(completedQuery, completedArgs);
            completedCursor.moveToFirst();
            int completedCount = completedCursor.getInt(0);
            completedCursor.close();

            Cursor pendingCursor = db.rawQuery(pendingQuery, pendingArgs);
            pendingCursor.moveToFirst();
            int pendingCount = pendingCursor.getInt(0);
            pendingCursor.close();

            tvCompletedTasks.setText("Nhiệm vụ đã hoàn thành\n" + completedCount);
            tvPendingTasks.setText("Nhiệm vụ chưa hoàn thành\n" + pendingCount);
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String startDateString = dateFormat.format(startDateCalendar.getTime());
                    String finishDateString = dateFormat.format(endDateCalendar.getTime());
                    txtStart.setText(startDateString);
                    txtFinish.setText(finishDateString);
                    createBarChart(startDateString,finishDateString);

                } else {
                    Toast.makeText(getContext(),"Invalid period",Toast.LENGTH_LONG).show();
                }

            }, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH));
            endDatePickerDialog.show();

        }, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.show();
    }

    public void createBarChart(String startDateString, String finishDateString) {
//        ArrayList<BarEntry> barEntriesCompleted = statisticDAO.getTaskCompleteCountByDateRange(startDateString,finishDateString);
//        ArrayList<BarEntry> barEntriesPending = statisticDAO.getTaskPendingCountByDateRange(startDateString,finishDateString);
        ArrayList<BarEntry> barEntriesCompleted = statisticDAO.getTaskCompleteCountByDate();
        ArrayList<BarEntry> barEntriesPending = statisticDAO.getTaskPendingCountByDate();

        // Tạo BarDataSet cho dữ liệu nhiệm vụ
        BarDataSet barDataSetCompleted = new BarDataSet(barEntriesCompleted, "Nhiệm vụ đã hoàn thành");
        barDataSetCompleted.setColor(Color.BLUE); // Màu cột hoàn thành

        BarDataSet barDataSetPending = new BarDataSet(barEntriesPending, "Nhiệm vụ chưa hoàn thành");
        barDataSetPending.setColor(Color.GREEN); // Màu cột chưa hoàn thành

        // Tạo dữ liệu BarData
        BarData barData = new BarData(barDataSetCompleted, barDataSetPending);
        barData.setBarWidth(0.3f); // Độ rộng mỗi cột

        // Gắn dữ liệu vào BarChart
        barChart.setData(barData);

        // Tùy chỉnh trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(-0.5f);
        ArrayList<Date> dates = statisticDAO.getTaskDates(); // Lấy danh sách ngày
  //      ArrayList<String> dates = dbHelper.getTaskDatesByRange(startDateString,finishDateString); // Lấy danh sách ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        xAxis.setValueFormatter(new DateAxisFormatter(dates)); // Đặt formatter cho trục X
        xAxis.setGranularity(1f); // Bước nhảy trục X
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);

        int numGroups = dates.size();

        // Nhóm các cột lại theo ngày
        barChart.getXAxis().setAxisMinimum(-0.25f); // Đảm bảo không bị lệch khỏi trục
        float groupSpace = 0.2f; // Khoảng cách giữa các nhóm cột
        float barSpace = 0.05f; // Khoảng cách giữa các cột trong 1 nhóm
        float barWidth = 0.3f; // Độ rộng mỗi cột (phải khớp với barData.setBarWidth)



        // Đảm bảo khoảng cách tổng = 1 (groupSpace + 2 * barWidth + barSpace = 1)
        barData.setBarWidth(barWidth);
        barChart.groupBars(0f, groupSpace, barSpace);

        // Tùy chỉnh trục Y
        barChart.getAxisRight().setEnabled(false); // Vô hiệu hóa trục bên phải
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
//        leftAxis.setTextColor(Color.WHITE);

        // Tùy chỉnh khác
        barChart.getDescription().setEnabled(false); // Tắt phần mô tả góc dưới bên phải (nếu có)
//        barChart.getLegend().setTextColor(Color.WHITE); // Màu chữ của chú thích (legend)
        barChart.setFitBars(true); // Tự động căn chỉnh các cột trong biểu đồ

        // Làm mới biểu đồ
        barChart.invalidate();
    }


    private void createPieChart() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<PieEntry> entries = statisticDAO.getData();

        // Tạo PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "Danh mục nhiệm vụ");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Đặt màu sắc cho biểu đồ
        dataSet.setValueTextSize(14f); // Kích thước văn bản
        dataSet.setValueTextColor(Color.WHITE); // Màu sắc văn bản

        // Tạo PieData từ PieDataSet
        PieData data = new PieData(dataSet);

        // Cấu hình PieChart
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true); // Hiển thị lỗ ở giữa biểu đồ
        pieChart.setHoleRadius(40f); // Bán kính lỗ
        pieChart.setTransparentCircleRadius(45f); // Bán kính vòng tròn trong suốt
        pieChart.setEntryLabelColor(Color.WHITE); // Màu nhãn các phần
        pieChart.setEntryLabelTextSize(12f); // Kích thước nhãn
        pieChart.getDescription().setEnabled(false); // Tắt mô tả biểu đồ
//        pieChart.getLegend().setTextColor(Color.WHITE); // Đặt màu sắc cho chú thích
        pieChart.animateY(1000); // Hiệu ứng hiển thị

        // Làm mới biểu đồ
        pieChart.invalidate();
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