package com.example.quanlycongviec.StatisticAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DB.DBHelper;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.Utils.ShareStore;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StatisticDAO {
    private SQLiteDatabase db;
    private int user_id;
    private TaskDAO taskDAO;
    private ShareStore store ;

    private DBHelper dbHelper;
    public StatisticDAO(Context context) {
        dbHelper = new DBHelper(context);
        taskDAO = new TaskDAO(context);
        store= new ShareStore(context);
        user_id =Integer.parseInt(store.getValue("user_id", null));
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
    public  ArrayList<PieEntry> getData() {
        // Truy vấn để lấy số nhiệm vụ theo danh mục
        String query = "SELECT c.name AS category_name, COUNT(t.id) AS task_count " +
                "FROM Category c " +
                "LEFT JOIN Task t ON c.id = t.category_id " +
                "WHERE t.user_id = ?" +
                "GROUP BY c.id " +
                "ORDER BY c.name";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(user_id)});

        // Danh sách các phần của biểu đồ
        ArrayList<PieEntry> entries = new ArrayList<>();

        while (cursor.moveToNext()) {
            String categoryName = cursor.getString(0); // Tên danh mục
            int taskCount = cursor.getInt(1);         // Số nhiệm vụ
            entries.add(new PieEntry(taskCount, categoryName)); // Thêm mục vào biểu đồ
        }
        cursor.close();
        return entries;
    }
    public ArrayList<BarEntry> getTaskCompleteCountByDateRange(String startDate, String endDate) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) AS count " +
                        "FROM Task " +
                        "WHERE status = 1 AND user_id = ? AND date BETWEEN ? AND ? " +
                        "GROUP BY date ORDER BY date",
                new String[]{String.valueOf(user_id), startDate, endDate}
        );

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int count = cursor.getInt(1);
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

        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 0 AND user_id = ? AND date BETWEEN ? AND ? " +
                        "GROUP BY date ORDER BY date",
                new String[]{String.valueOf(user_id), startDate, endDate}
        );

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int count = cursor.getInt(1);
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }

    public ArrayList<Date> getTaskDatesByRange(String startDate, String endDate) {
        ArrayList<Date> dates = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT date " +
                        "FROM Task " +
                        "WHERE date BETWEEN ? AND ? " +
                        "ORDER BY date",
                new String[]{ startDate, endDate}
        );
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (cursor.moveToFirst()) {
            do {
                try {
                    String dateString = cursor.getString(0); // Lấy giá trị ngày dưới dạng chuỗi
                    Date date = dateFormat.parse(dateString); // Chuyển đổi chuỗi thành đối tượng Date
                    if (date != null) {
                        dates.add(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    public ArrayList<BarEntry> getTaskCompleteCountByDate() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 1 AND user_id = ? " +
                        "GROUP BY date ORDER BY date",
                new String[]{String.valueOf(user_id)}
        );

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int count = cursor.getInt(1);
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

        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 0 AND user_id = ? " +
                        "GROUP BY date ORDER BY date",
                new String[]{String.valueOf(user_id)}
        );

        int index1 = 0;
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int count1 = cursor.getInt(1);
                barEntries1.add(new BarEntry(index1, count1));
                index1++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries1;
    }
    public ArrayList<Date> getTaskDates() {
        ArrayList<Date> dates = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date FROM Task WHERE user_id = ?  ORDER BY date", new String[]{String.valueOf(user_id)});
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (cursor.moveToFirst()) {
            do {
                try {
                    String dateString = cursor.getString(0); // Lấy giá trị ngày dưới dạng chuỗi
                    Date date = dateFormat.parse(dateString); // Chuyển đổi chuỗi thành đối tượng Date
                    if (date != null) {
                        dates.add(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
}