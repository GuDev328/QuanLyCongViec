package com.example.quanlycongviec.CategoryAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.Utils.ShareStore;

public class CategoryAdd extends AppCompatActivity {
    EditText edtCategoryName, edtCategoryDescription;
    Button btnCancel, btnConfirm;
    CategoryDAO categoryDAO;
    ShareStore store;
    long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add);

        store = new ShareStore(CategoryAdd.this);
        if (store.getValue("user_id", null) == null) {
            Intent it = new Intent(CategoryAdd.this, LoginActivity.class);
            startActivity(it);
        }
        user_id = Long.parseLong(store.getValue("user_id", null));
        categoryDAO = new CategoryDAO(CategoryAdd.this);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDescription = findViewById(R.id.edtCategoryDescription);
        btnCancel = findViewById(R.id.btnCancelAddCategory);
        btnConfirm = findViewById(R.id.btnConfirmAddCategory);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Đóng activity
            }
        });

        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = edtCategoryName.getText().toString().trim();
                String categoryDescription = edtCategoryDescription.getText().toString().trim();

                if (categoryName.isEmpty()) {
                    Toast.makeText(CategoryAdd.this, "Nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo mới đối tượng Category_DTO với id = 0 (tạo mới)
                Category_DTO newCategory = new Category_DTO(0, user_id, categoryName, categoryDescription);
                long categoryId = categoryDAO.insert(newCategory);
                if (categoryId != -1) {
                    Toast.makeText(CategoryAdd.this, "Tạo danh mục thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity trước
                    finish();
                } else {
                    Toast.makeText(CategoryAdd.this, "Tạo danh mục thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
