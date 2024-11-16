package com.example.quanlycongviec.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.Common;
import com.example.quanlycongviec.DAO.UserDAO;
import com.example.quanlycongviec.DTO.User_DTO;
import com.example.quanlycongviec.R;

public class RegisterActivity extends AppCompatActivity {
    Button btnBackToLogin, btnBirthDay, btnDangKy;
    EditText edtEmail, editPassword, edtRePassword, edtName;
    RadioGroup groupGender;
    UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        userDAO= new UserDAO(RegisterActivity.this);
        edtEmail = findViewById(R.id.edtEmailR);
        editPassword= findViewById(R.id.edtPasswordR);
        edtRePassword= findViewById(R.id.edtRePassword);
        groupGender= findViewById(R.id.groupGender);
        btnDangKy = findViewById(R.id.btnDangKyR);
        edtName= findViewById(R.id.editName);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnBirthDay = findViewById(R.id.btnBirthDay);
        btnBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnBirthDay.getText().toString().equals("Chọn ngày")) {
                    Common.showDatePicker(RegisterActivity.this, btnBirthDay, -1, -1, -1);
                } else {
                    String ngayDangChon = btnBirthDay.getText().toString();
                    String ngayArr[] = ngayDangChon.split("/");

                    Common.showDatePicker(RegisterActivity.this, btnBirthDay, Integer.parseInt(ngayArr[0]), Integer.parseInt(ngayArr[1]), Integer.parseInt(ngayArr[2]));
                }
            }
        });



        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Common.isValidEmail(edtEmail.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userDAO.checkEmailIsExist(edtEmail.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, "Đã tồn tại Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtName.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập họ tên", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.getSelectedRadioButtonValue(groupGender)==null){
                    Toast.makeText(RegisterActivity.this, "Hãy chọn giới tính", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(btnBirthDay.getText().toString().trim().equals("Chọn ngày")){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập ngày sinh", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editPassword.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtRePassword.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!edtRePassword.getText().toString().trim().equals( editPassword.getText().toString().trim() )){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                User_DTO user = new User_DTO(0,
                        edtEmail.getText().toString().trim(),
                        editPassword.getText().toString().trim(),
                        edtName.getText().toString().trim(),
                        Common.getSelectedRadioButtonValue(groupGender),
                        btnBirthDay.getText().toString().trim(),
                        ""
                );
                long taskId = userDAO.insert(user);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
