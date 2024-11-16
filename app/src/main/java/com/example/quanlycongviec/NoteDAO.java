package com.example.quanlycongviec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDAO {
        private SQLiteDatabase db;

        public NoteDAO(Context context) {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }

        // Thêm Note
        public long insertNote(int userId, String title, String content) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("title", title);
            values.put("content", content);
            return db.insert("Note", null, values);
        }

        // Lấy tất cả Note
        public Cursor getAllNotes() {
            return db.rawQuery("SELECT * FROM Note", null);
        }

        // Cập nhật Note
        public int updateNote(int id, String title, String content) {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("content", content);
            return db.update("Note", values, "id = ?", new String[]{String.valueOf(id)});
        }

        // Xóa Note
        public int deleteNote(int id) {
            return db.delete("Note", "id = ?", new String[]{String.valueOf(id)});
        }
    }

