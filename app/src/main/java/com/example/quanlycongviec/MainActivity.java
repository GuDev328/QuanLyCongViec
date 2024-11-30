package com.example.quanlycongviec;

import android.content.Context;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.databinding.ActivityMainBinding;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

//import com.example.quanlycongviec.databinding.ActivityMainBinding;
//import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); // 100MB
            } catch (Exception e) {
                e.printStackTrace();
            }
//            //Insert tạm 2 bản ghi
//            TaskDAO taskDAOVIP = new TaskDAO(this);
//            for(int i=0; i<2; i++){
//                Task_DTO newTask = new Task_DTO(0, 1, 1, "2024-11-15", "10:00", "Học lập trình", "Hoàn thành bài tập", 0);
//                long taskId = taskDAOVIP.insert(newTask);
//            }
//
//            //Get ALL
//            String a="";
//            ArrayList<Task_DTO> tasks = taskDAOVIP.getAll();
//            for (Task_DTO task : tasks) {
//                a+=("Task: "+ "ID: " + task.getId() + ", Title: " + task.getTitle())+"\n";
//            }
//            Common.showAlertDialog(MainActivity.this, "Demo", a);
//
//            //Update
//            taskDAOVIP.update(1, new Task_DTO( 1, 1, 1, "2024-11-15", "10:30", "Học SQLite", "Thực hành CRUD", 1));
//
//            //Delete
//            taskDAOVIP.delete(2);



        }
        catch (Exception e){
            System.out.println("database"+e.getMessage());
        }

        // Inflate the binding layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the default fragment
        replaceFragment(new HomeFragment());

        // Set up the bottom navigation listener
        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.statistics) {
                selectedFragment = new StatisticFragment();
            } else if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.notes) {
                selectedFragment = new NoteFragment();
            } else if (item.getItemId() == R.id.lists) {
                selectedFragment = new ListFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });
    }

    // Method to replace the fragment in the frame layout
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
