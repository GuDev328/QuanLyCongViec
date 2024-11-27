package com.example.quanlycongviec;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quanlycongviec.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tạo bảng nếu chưa tồn tại
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "gender TEXT," +
                "birthday TEXT," +
                "avatar TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "FOREIGN KEY (user_id) REFERENCES User (id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS Task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "category_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "status INTEGER NOT NULL," +
                "FOREIGN KEY (category_id) REFERENCES Category (id)," +
                "FOREIGN KEY (user_id) REFERENCES User (id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS Note (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "title TEXT NOT NULL," +
                "content TEXT," +
                "FOREIGN KEY (user_id) REFERENCES User (id))");
        db.execSQL("INSERT INTO Category (user_id, name, description) " +
                "VALUES (1, 'Không có', 'Chưa chọn danh mục')");
        db.execSQL("INSERT INTO Category (user_id, name, description) " +
                "VALUES (1, 'Cá nhân', 'Cá nhân')");
        db.execSQL("INSERT INTO Category (user_id, name, description) " +
                "VALUES (1, 'Công việc', 'Công việc')");
        insertSampleData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Task");
        db.execSQL("DROP TABLE IF EXISTS Note");
        onCreate(db);
    }
    public void insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Task (date, status) VALUES ('2024-11-17', 1)");
        db.execSQL("INSERT INTO Task (date, status) VALUES ('2024-11-17', 1)");
        db.execSQL("INSERT INTO Task (date, status) VALUES ('2024-11-18', 0)");
        db.execSQL("INSERT INTO Task (date, status) VALUES ('2024-11-18', 1)");
        db.execSQL("INSERT INTO Task (date, status) VALUES ('2024-11-19', 0)");
    }
    public ArrayList<BarEntry> getTaskCompleteCountByDateRange(String startDate, String endDate) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn nhiệm vụ theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 1 AND date BETWEEN ? AND ?" +
                        "GROUP BY date ORDER BY date",
                new String[]{startDate, endDate}
        );

        int index = 0; // Dùng để hiển thị trên trục X
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Ngày
                int count = cursor.getInt(1);     // Số nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }
    public ArrayList<BarEntry> getTaskPendingCountByDateRange(String startDate, String endDate) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn nhiệm vụ theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT date, COUNT(*) as count " +
                        "FROM Task " +
                        "WHERE status = 0 AND date BETWEEN ? AND ?" +
                        "GROUP BY date ORDER BY date",
                new String[]{startDate, endDate}
        );

        int index = 0; // Dùng để hiển thị trên trục X
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Ngày
                int count = cursor.getInt(1);     // Số nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }

    public ArrayList<String> getTaskDatesByRange(String startDate, String endDate) {
        ArrayList<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn danh sách ngày theo khoảng ngày
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT date " +
                        "FROM Task " +
                        "WHERE date BETWEEN ? AND ? " +
                        "ORDER BY date",
                new String[]{startDate, endDate}
        );

        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
    public ArrayList<String> getTaskDates() {
        ArrayList<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date FROM Task ORDER BY date", null);
        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
    public ArrayList<BarEntry> getTaskCompleteCountByDate() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, COUNT(*) as count FROM Task WHERE status = 1 GROUP BY date ORDER BY date", null);

        int index = 0; // Dùng làm trục X (vị trí trên biểu đồ)
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Lấy ngày
                int count = cursor.getInt(1);     // Lấy số lượng nhiệm vụ
                barEntries.add(new BarEntry(index, count));
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries;
    }
    public ArrayList<BarEntry> getTaskPendingCountByDate() {
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, COUNT(*) as count FROM Task WHERE status = 0 GROUP BY date ORDER BY date", null);

        int index1 = 0; // Dùng làm trục X (vị trí trên biểu đồ)
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0); // Lấy ngày
                int count1 = cursor.getInt(1);     // Lấy số lượng nhiệm vụ
                barEntries1.add(new BarEntry(index1, count1));
                index1++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barEntries1;
    }
}
