package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.quanlycongviec.DTO.Category_DTO;

public class CategoryDAO extends CRUD_DAO<Category_DTO> {

    public CategoryDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return "Category";
    }

    @Override
    protected Category_DTO fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")); // Sử dụng getLong
        long userId = cursor.getLong(cursor.getColumnIndexOrThrow("user_id")); // Sử dụng getLong
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        return new Category_DTO(id, userId, name, description);
    }

    @Override
    protected ContentValues toContentValues(Category_DTO category) {
        ContentValues values = new ContentValues();
        values.put("id", category.getId()); // Thêm nếu cần lưu cả `id`
        values.put("user_id", category.getUserId());
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        return values;
    }
}
