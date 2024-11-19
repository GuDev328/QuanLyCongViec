package com.example.quanlycongviec.CategoryAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.R;

public class CategoryEdit extends AppCompatActivity {
    EditText edtCategoryName, edtCategoryDescription;
    Button btnSaveCategory, btnCancel;
    CategoryDAO categoryDAO;
    Category_DTO category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_edit);

        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }

        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDescription = findViewById(R.id.edtCategoryDescription);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        btnCancel = findViewById(R.id.btnCancel);

        categoryDAO = new CategoryDAO(this);

        // Lấy ID danh mục được chọn từ Intent
        long categoryId = getIntent().getLongExtra("selectedCategoryId", -1);
        if (categoryId != -1) {
            category = categoryDAO.getById((int) categoryId);
            if (category != null) {
                edtCategoryName.setText(category.getName());
                edtCategoryDescription.setText(category.getDescription());
            }
        }

        btnSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtCategoryName.getText().toString().trim();
                String description = edtCategoryDescription.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(CategoryEdit.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                category.setName(name);
                category.setDescription(description);

                long result = categoryDAO.update(category.getId(), category);
                if (result != -1) {
                    Toast.makeText(CategoryEdit.this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Trả về kết quả OK
                    finish();
                } else {
                    Toast.makeText(CategoryEdit.this, "Cập nhật danh mục thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
