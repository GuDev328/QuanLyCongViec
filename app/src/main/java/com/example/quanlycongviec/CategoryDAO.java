package com.example.quanlycongviec;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDAO {
        private SQLiteDatabase db;

        public CategoryDAO(Context context) {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }

        // Thêm Category
        public long insertCategory(int userId, String name, String description) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("name", name);
            values.put("description", description);
            return db.insert("Category", null, values);
        }

        // Lấy tất cả Category
        public Cursor getAllCategories() {
            return db.rawQuery("SELECT * FROM Category", null);
        }

        // Cập nhật Category
        public int updateCategory(int id, String name, String description) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("description", description);
            return db.update("Category", values, "id = ?", new String[]{String.valueOf(id)});
        }

        // Xóa Category
        public int deleteCategory(int id) {
            return db.delete("Category", "id = ?", new String[]{String.valueOf(id)});
        }
    }

