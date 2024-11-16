package com.example.quanlycongviec;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareStore {
    private static final String PREFS_NAME = "MyAppPrefs"; // Tên file SharedPreferences
    private SharedPreferences sharedPreferences;

    // Constructor
    public ShareStore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Hàm set dữ liệu (key-value)
    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply(); // Lưu thay đổi
    }

    // Hàm get dữ liệu (key)
    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Hàm xóa dữ liệu (theo key)
    public void removeValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // Hàm xóa tất cả dữ liệu
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}