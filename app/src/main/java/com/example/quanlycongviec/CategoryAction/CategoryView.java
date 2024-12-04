package com.example.quanlycongviec.CategoryAction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.Utils.Common;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.R;

public class CategoryView extends AppCompatActivity {
    TextView txtCategoryName, txtCategoryDescription;
    Button btnCloseCategoryView;
    ImageView btnDeleteCategory, btnSuaCategory;
    CategoryDAO categoryDAO;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Đánh dấu để trả về Activity trước để chạy hàm định sẵn
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                        finish();

                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_view);

        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }

        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtCategoryDescription = findViewById(R.id.txtCategoryDescription);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        btnCloseCategoryView = findViewById(R.id.btnCloseCategoryView);
        btnSuaCategory = findViewById(R.id.btnSuaCategory);
        categoryDAO = new CategoryDAO(getApplication());

        long categoryId = getIntent().getLongExtra("selectedCategoryId", -1);

        if (categoryId != -1) {
            Category_DTO category = categoryDAO.getById((int) categoryId);
            txtCategoryName.setText(category.getName());
            txtCategoryDescription.setText(category.getDescription());

            btnCloseCategoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btnSuaCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sử dụng activityResultLauncher để mở CategoryEdit
                    Intent intent = new Intent(CategoryView.this, CategoryEdit.class);
                    intent.putExtra("selectedCategoryId", categoryId);  // Truyền ID của danh mục hiện tại
                    activityResultLauncher.launch(intent); // Mở CategoryEdit và đợi kết quả
                }
            });

            btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskDAO taskDAO = new TaskDAO(getApplicationContext());
                    if (taskDAO.hasTasksInCategory(categoryId)) {
                        Toast.makeText(CategoryView.this, "Không thể xoá. Danh mục này vẫn còn công việc!", Toast.LENGTH_SHORT).show();
                    } else {
                        Common.showConfirmDialog(CategoryView.this, "Xoá danh mục",
                                "Bạn có chắc chắn muốn xoá danh mục này?", new Runnable() {
                                    @Override
                                    public void run() {
                                        categoryDAO.delete((int) categoryId);
                                        Toast.makeText(CategoryView.this, "Đã xoá danh mục", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                    }
                }
            });


        } else {
            Toast.makeText(this, "Không tìm thấy thông tin danh mục!", Toast.LENGTH_SHORT).show();
        }
    }
}
