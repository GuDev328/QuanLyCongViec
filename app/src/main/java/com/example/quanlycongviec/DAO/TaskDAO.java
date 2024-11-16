package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.quanlycongviec.DTO.Task_DTO;

public class TaskDAO extends CRUD_DAO<Task_DTO> {

    public TaskDAO(Context context) {
        super(context);
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
}
