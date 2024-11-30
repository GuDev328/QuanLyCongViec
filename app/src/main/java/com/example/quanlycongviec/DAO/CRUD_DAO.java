package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlycongviec.DBHelper;
import com.example.quanlycongviec.ShareStore;

import java.util.ArrayList;
import java.util.List;

public abstract class CRUD_DAO<T> {
    protected SQLiteDatabase db;
    protected ShareStore store;

    public CRUD_DAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        store = new ShareStore(context);
    }

    // Lấy tên bảng (phải được implement trong subclass)
    protected abstract String getTableName();
    protected  SQLiteDatabase getDBInstance(){
        return  this.db;
    };
    // Chuyển từ Cursor -> Object (phải được implement trong subclass)
    protected abstract T fromCursor(Cursor cursor);

    // Chuyển từ Object -> ContentValues (phải được implement trong subclass)
    protected abstract ContentValues toContentValues(T object);

    // Thêm một đối tượng vào database
    public long insert(T object) {
        ContentValues values = toContentValues(object);
        values.remove("id");
        return db.insert(getTableName(), null, values);
    }

    // Lấy tất cả các bản ghi
    public ArrayList<T> getAll() {
        ArrayList<T> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + getTableName()+ " WHERE user_id = ?", new String[]{store.getValue("user_id", null)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // Cập nhật một bản ghi (yêu cầu ID để xác định)
    public int update(long id, T object) {
        ContentValues values = toContentValues(object);
        return db.update(getTableName(), values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Xóa một bản ghi dựa trên ID
    public int delete(long id) {
        return db.delete(getTableName(), "id = ?", new String[]{String.valueOf(id)});
    }

    // Lấy bản ghi theo ID
    public T getById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + getTableName() + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            T object = fromCursor(cursor);
            cursor.close();
            return object;
        }
        return null;
    }
}
