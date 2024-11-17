package com.example.quanlycongviec.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.R;


public class ForgotPassword extends AppCompatActivity {

    TextView txtPhone, txtCode, txtPassword, txtRePassword;
    EditText edtPhone, edtCode, edtPassword, edtRePassword;
    Button btnBackToLogin, btnConfirm, btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_forgot_password);
        txtPhone = findViewById(R.id.txtPhone);
        txtCode = findViewById(R.id.txtCode);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtCode = findViewById(R.id.edtCode);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnSendCode = findViewById(R.id.btnSendCode);

        txtCode.setVisibility(View.GONE);
        txtPassword.setVisibility(View.GONE);
        txtRePassword.setVisibility(View.GONE);
        edtCode.setVisibility(View.GONE);
        edtPassword.setVisibility(View.GONE);
        edtRePassword.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ForgotPassword.this, "Đã gửi mã xác nhận", Toast.LENGTH_SHORT).show();
                txtCode.setVisibility(View.VISIBLE);
                txtPassword.setVisibility(View.VISIBLE);
                txtRePassword.setVisibility(View.VISIBLE);
                edtCode.setVisibility(View.VISIBLE);
                edtPassword.setVisibility(View.VISIBLE);
                edtRePassword.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnSendCode.setVisibility(View.GONE);
            }
        });


        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}