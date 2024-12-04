package com.example.quanlycongviec;

import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlycongviec.Activities.HomeFragment;
import com.example.quanlycongviec.Activities.ListFragment;
import com.example.quanlycongviec.Activities.NoteFragment;
import com.example.quanlycongviec.Activities.ProfileFragment;
import com.example.quanlycongviec.Activities.StatisticFragment;
import com.example.quanlycongviec.DB.DBHelper;
import com.example.quanlycongviec.databinding.ActivityMainBinding;

import java.lang.reflect.Field;

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
