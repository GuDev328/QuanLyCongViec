package com.example.quanlycongviec.StatisticAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DBHelper;
import com.example.quanlycongviec.DTO.Task_DTO;

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
}
