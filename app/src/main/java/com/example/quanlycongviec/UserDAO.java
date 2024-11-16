package com.example.quanlycongviec;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
        private SQLiteDatabase db;

        public UserDAO(Context context) {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }

        // Thêm User
        public long insertUser(String email, String password, String name, String gender, String birthday, String avatar) {
            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("password", password);
            values.put("name", name);
            values.put("gender", gender);
            values.put("birthday", birthday);
            values.put("avatar", avatar);
            return db.insert("User", null, values);
        }

        // Lấy danh sách User
        public Cursor getAllUsers() {
            return db.rawQuery("SELECT * FROM User", null);
        }

        // Cập nhật User
        public int updateUser(int id, String name, String gender, String birthday, String avatar) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("gender", gender);
            values.put("birthday", birthday);
            values.put("avatar", avatar);
            return db.update("User", values, "id = ?", new String[]{String.valueOf(id)});
        }

        // Xóa User
        public int deleteUser(int id) {
            return db.delete("User", "id = ?", new String[]{String.valueOf(id)});
        }
    }


