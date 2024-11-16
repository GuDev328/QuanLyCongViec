package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.quanlycongviec.DTO.User_DTO;

public class UserDAO extends CRUD_DAO<User_DTO> {

    public UserDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return "User";
    }

    @Override
    protected User_DTO fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")); // Sử dụng getLong
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
        String birthday = cursor.getString(cursor.getColumnIndexOrThrow("birthday"));
        String avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"));

        return new User_DTO(id, email, password, name, gender, birthday, avatar);
    }

    @Override
    protected ContentValues toContentValues(User_DTO user) {
        ContentValues values = new ContentValues();
        values.put("id", user.getId()); // Thêm nếu cần lưu cả `id`
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("avatar", user.getAvatar());
        return values;
    }
}
