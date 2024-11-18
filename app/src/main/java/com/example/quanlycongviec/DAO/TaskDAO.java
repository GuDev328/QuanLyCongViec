package com.example.quanlycongviec.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlycongviec.DBHelper;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.StatisticAction.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO extends CRUD_DAO<Task_DTO> {

    private SQLiteDatabase db;
    private int user_id;
    private DBHelper dbHelper;

    public TaskDAO(Context context) {
        super(context);
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        UserSession userSession = UserSession.getInstance();
        user_id = userSession.getUserId();
    }


    @Override
    protected String getTableName() {
        return "Task";
    }

    @Override
    protected Task_DTO fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")); // Sử dụng getLong
        long categoryId = cursor.getLong(cursor.getColumnIndexOrThrow("category_id")); // Sử dụng getLong
        long userId = cursor.getLong(cursor.getColumnIndexOrThrow("user_id")); // Sử dụng getLong
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));

        return new Task_DTO(id, categoryId, userId, date, time, title, description, status);
    }

    @Override
    protected ContentValues toContentValues(Task_DTO task) {
        ContentValues values = new ContentValues();
        values.put("id", task.getId()); // Thêm nếu cần lưu cả `id`
        values.put("category_id", task.getCategoryId());
        values.put("user_id", task.getUserId());
        values.put("date", task.getDate());
        values.put("time", task.getTime());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("status", task.getStatus());
        System.out.println(values);
        return values;
    }

    public ArrayList<Task_DTO> getListNotDone(String date){
        SQLiteDatabase db = this.getDBInstance();
        ArrayList<Task_DTO> list = new ArrayList<>();
        // Cập nhật câu truy vấn để lọc theo date và status = 0
        String query = "SELECT * FROM " + getTableName() +
                " WHERE date = ? AND status = 0";

        // Chạy truy vấn với tham số là ngày
        Cursor cursor = db.rawQuery(query, new String[]{date});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;

    }
    public ArrayList<Task_DTO> getListDone(String date){
        SQLiteDatabase db = this.getDBInstance();
        ArrayList<Task_DTO> list = new ArrayList<>();
        // Cập nhật câu truy vấn để lọc theo date và status = 0
        String query = "SELECT * FROM " + getTableName() +
                " WHERE date = ? AND status = 1";

        // Chạy truy vấn với tham số là ngày
        Cursor cursor = db.rawQuery(query, new String[]{date});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;

    }
    @SuppressLint("Range")
    public List<Task_DTO> getTasks(String startDateString, String finishDateString) {
        List<Task_DTO> list = new ArrayList<>();
        Cursor cursor = null;
        String query = "SELECT * FROM Task WHERE user_id = ?";
        String[] selectionArgs;

        // Lọc theo ngày tháng nếu có
        if (startDateString != null && finishDateString != null) {
            query += " AND date BETWEEN ? AND ?";
            selectionArgs = new String[]{String.valueOf(this.user_id), startDateString, finishDateString};
        } else {
            selectionArgs = new String[]{String.valueOf(this.user_id)};
        }

        cursor = db.rawQuery(query, selectionArgs);

        // Duyệt qua kết quả trả về và thêm vào danh sách
        while (cursor.moveToNext()) {
            Task_DTO task = new Task_DTO();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
            task.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            task.setDate(cursor.getString(cursor.getColumnIndex("date")));
            task.setTime(cursor.getString(cursor.getColumnIndex("time")));
            task.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            list.add(task);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<Task_DTO> getAllTasks() {
        return getTasks(null, null);
    }
}
