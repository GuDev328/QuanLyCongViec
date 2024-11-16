package com.example.quanlycongviec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskDAO {
        private SQLiteDatabase db;

        public TaskDAO(Context context) {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }

        // Thêm Task
        public long insertTask(int categoryId, int userId, String date, String time, String title, String description, int status) {
            ContentValues values = new ContentValues();
            values.put("category_id", categoryId);
            values.put("user_id", userId);
            values.put("date", date);
            values.put("time", time);
            values.put("title", title);
            values.put("description", description);
            values.put("status", status);
            return db.insert("Task", null, values);
        }

        // Lấy tất cả Task
        public Cursor getAllTasks() {
            return db.rawQuery("SELECT * FROM Task", null);
        }

        // Cập nhật Task
        public int updateTask(int id, String title, String description, int status) {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);
            values.put("status", status);
            return db.update("Task", values, "id = ?", new String[]{String.valueOf(id)});
        }

        // Xóa Task
        public int deleteTask(int id) {
            return db.delete("Task", "id = ?", new String[]{String.valueOf(id)});
        }
    }

