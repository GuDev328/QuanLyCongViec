package com.example.quanlycongviec.StatisticAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DBHelper;
import com.example.quanlycongviec.DTO.Task_DTO;

import java.util.ArrayList;
import java.util.List;


public class StatisticDAO {
    private SQLiteDatabase db;
    private int user_id;
    private TaskDAO taskDAO;
    private DBHelper dbHelper;
    public StatisticDAO(Context context) {
        dbHelper = new DBHelper(context);
        taskDAO = new TaskDAO(context);
        UserSession userSession = UserSession.getInstance();
        user_id = userSession.getUserId();
    }

}
