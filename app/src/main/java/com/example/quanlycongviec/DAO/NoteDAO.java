package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlycongviec.DTO.Note_DTO;

import java.util.ArrayList;

public class NoteDAO extends CRUD_DAO<Note_DTO> {

    public NoteDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return "Note";
    }

    @Override
    protected Note_DTO fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")); // Sử dụng getLong
        long userId = cursor.getLong(cursor.getColumnIndexOrThrow("user_id")); // Sử dụng getLong
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));

        return new Note_DTO(id, userId, title, content);
    }

    @Override
    protected ContentValues toContentValues(Note_DTO note) {
        ContentValues values = new ContentValues();
        values.put("id", note.getId()); // Thêm nếu cần lưu cả `id`
        values.put("user_id", note.getUserId());
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        return values;
    }

    // Phương thức xóa nhiều bản ghi
    public int deleteMultiple(ArrayList<Note_DTO> notes) {
        // Xử lý xóa nhiều bản ghi
        int i=0;
        for (Note_DTO note : notes) {
            db.delete(getTableName(), "id = ?", new String[]{String.valueOf(note.getId())});
            i++;
        }
        return i;
    }
}
