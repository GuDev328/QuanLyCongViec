package com.example.quanlycongviec.Profile;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.R;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnCancel, btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_change_password);
        //Set Width cho Dialog
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }

        btnCancel = findViewById(R.id.btnCancelChangePassword);
        btnCancel.setOnClickListener(view -> {
            finish();
        });
        btnConfirm = findViewById(R.id.btnConfirmChangePassword);
        btnConfirm.setOnClickListener(view -> {
            finish();
        });

    }
}