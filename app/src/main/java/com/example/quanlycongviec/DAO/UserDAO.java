package com.example.quanlycongviec.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlycongviec.DTO.User_DTO;
import com.example.quanlycongviec.MainActivity;
import com.example.quanlycongviec.ShareStore;

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

    public boolean checkEmailIsExist(String email){
        SQLiteDatabase db = this.getDBInstance();
        // Câu lệnh SQL để kiểm tra xem có user nào có email này hay không
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";

        // Thực thi câu lệnh truy vấn với email làm tham số
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Kiểm tra xem kết quả có tồn tại và trả về true hoặc false
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);  // Trả về số lượng kết quả tìm thấy
            cursor.close();
            return count > 0;  // Nếu có ít nhất 1 kết quả, trả về true (email đã tồn tại)
        }

        return false;
    }

    public long login(String email, String password) {
        SQLiteDatabase db = this.getDBInstance();

        // Câu lệnh SQL để tìm người dùng với email và password
        String query = "SELECT id FROM User WHERE email = ? AND password = ?";

        // Thực thi câu lệnh truy vấn với email và password làm tham số
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        // Kiểm tra xem kết quả có tồn tại
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy index của cột "id"
            int columnIndex = cursor.getColumnIndex("id");

            // Kiểm tra nếu cột "id" tồn tại
            if (columnIndex != -1) {
                long userId = cursor.getLong(columnIndex);
                cursor.close();
                return userId; // Trả về id của người dùng
            } else {
                // Cột "id" không tồn tại
                cursor.close();
                return -1; // Trả về -1 nếu cột không tồn tại
            }
        }

        // Nếu không có kết quả (đăng nhập thất bại), trả về -1
        return -1;
    }


}
