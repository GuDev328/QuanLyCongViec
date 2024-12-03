package com.example.quanlycongviec.StatisticAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DB.DBHelper;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;


public class StatisticDAO {
    private SQLiteDatabase db;
    private int user_id;
    private TaskDAO taskDAO;
    private DBHelper dbHelper;
    public StatisticDAO(Context context) {
        dbHelper = new DBHelper(context);
        taskDAO = new TaskDAO(context);
        UserSession userSession = UserSession.getInstance();
        user_id = userSession.getUserId();
    }
    @SuppressLint("Range")
    public List<Task_DTO> getTaskData(String startDateString, String finishDateString){
        return this.taskDAO.getTasks(startDateString,finishDateString);
    }
    public ArrayList<StatisticStatus> getPercentStatus(String tableName, String startDateString, String finishDateString){
        db = dbHelper.getWritableDatabase();
        ArrayList<StatisticStatus> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = {startDateString, finishDateString};
            String query = "SELECT status, COUNT(*) AS count, (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM " + tableName + " WHERE user_id = " + this.user_id + ")) AS percentage " +
                    "FROM " + tableName + " " +
                    "WHERE user_id = " + this.user_id;
            if (startDateString != null && finishDateString != null) {
                query += " AND created_date BETWEEN ? AND ?";
            }
            query += " GROUP BY status";
            if(startDateString!=null && finishDateString!=null){
                cursor = db.rawQuery(query, selectionArgs);
            }
            else{
                cursor = db.rawQuery(query, null);
            }
            // Duyệt qua dữ liệu từ Cursor
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));
                @SuppressLint("Range") float percentage = cursor.getFloat(cursor.getColumnIndex("percentage"));
                StatisticStatus newSS = new StatisticStatus(status,percentage);
                result.add(newSS);
            }
        } catch (Exception e) {
            Log.e("PieChart", e+"");
        } finally {
            // Đảm bảo đóng cursor và SQLiteDatabase
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return result;
    }
    public ArrayList<StatisticStatus> getTaskStatus(String startDateString, String finishDateString){
        return getPercentStatus("Task", startDateString, finishDateString);
    }
    public ArrayList<BarEntry> getTaskCompleteCountByDateRange(String startDate, String endDate) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        // Truy vấn nhiệm vụ theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 1 AND date BETWEEN ? AND ?" +
                        "GROUP BY date ORDER BY date",
                new String[]{startDate, endDate}
        );

        int index = 0; // Dùng để hiển thị trên trục X
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Ngày
                int count = cursor.getInt(1);     // Số nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }
    public ArrayList<BarEntry> getTaskPendingCountByDateRange(String startDate, String endDate) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        // Truy vấn nhiệm vụ theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 0 AND date BETWEEN ? AND ?" +
                        "GROUP BY date ORDER BY date",
                new String[]{startDate, endDate}
        );

        int index = 0; // Dùng để hiển thị trên trục X
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Ngày
                int count = cursor.getInt(1);     // Số nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }

    public ArrayList<String> getTaskDatesByRange(String startDate, String endDate) {
        ArrayList<String> dates = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        // Truy vấn danh sách ngày theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT date " +
                        "FROM Task " +
                        "WHERE date BETWEEN ? AND ? " +
                        "ORDER BY date",
                new String[]{startDate, endDate}
        );

        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
    public ArrayList<String> getTaskDates() {
        ArrayList<String> dates = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date FROM Task ORDER BY date", null);
        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
    public ArrayList<BarEntry> getTaskCompleteCountByDate() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, COUNT(*) as count FROM Task WHERE status = 1 GROUP BY date ORDER BY date", null);

        int index = 0; // Dùng làm trục X (vị trí trên biểu đồ)
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Lấy ngày
                int count = cursor.getInt(1);     // Lấy số lượng nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }
    public ArrayList<BarEntry> getTaskPendingCountByDate() {
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, COUNT(*) as count FROM Task WHERE status = 0 GROUP BY date ORDER BY date", null);

        int index1 = 0; // Dùng làm trục X (vị trí trên biểu đồ)
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Lấy ngày
                int count1 = cursor.getInt(1);     // Lấy số lượng nhiệm vụ
                barEntries1.add(new BarEntry(index1, count1));
                index1++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries1;
    }
}
