package com.example.quanlycongviec.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.DAO.UserDAO;
import com.example.quanlycongviec.MainActivity;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.ShareStore;

public class LoginActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    TextView txtForgotPassword;
    EditText edtEmail, editPassword;
    UserDAO userDAO;
    ShareStore store ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        store = new ShareStore(LoginActivity.this);
        if(store.getValue("user_id", null) != null){
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
        }
        userDAO = new UserDAO(LoginActivity.this);
        edtEmail = findViewById(R.id.edtEmailL);
        btnLogin= findViewById(R.id.btnLogin);
        editPassword= findViewById(R.id.edtPasswordL);
         btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPassword= findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Hãy nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editPassword.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Hãy nhập  mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                long userId= userDAO.login(edtEmail.getText().toString().trim(), editPassword.getText().toString().trim());
                if(userId == -1){
                    Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                }else{

                    store.setValue("user_id",  Long.toString(userId) );

                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });

    }
}