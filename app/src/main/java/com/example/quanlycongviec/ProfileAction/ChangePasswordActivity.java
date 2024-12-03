package com.example.quanlycongviec.ProfileAction;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.DAO.UserDAO;
import com.example.quanlycongviec.DTO.User_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.ShareStore;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnCancel, btnConfirm;
    EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    ShareStore store;
    UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_change_password);
        store= new ShareStore(this);
        userDAO= new UserDAO(this);
        //Set Width cho Dialog
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }
        btnCancel = findViewById(R.id.btnCancelChangePassword);
        btnConfirm = findViewById(R.id.btnConfirmChangePassword);
        edtOldPassword= findViewById(R.id.editOldPassword);
        edtNewPassword= findViewById(R.id.editNewPassword);
        edtConfirmPassword= findViewById(R.id.edtReNewPassword);

        btnConfirm.setOnClickListener(view -> {
            User_DTO oldUser= userDAO.getById(Integer.parseInt(store.getValue("user_id", null)));
            if(edtOldPassword.getText().toString().equals("")){
                Toast.makeText(this, "Hãy nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                return;
            }
            if(edtNewPassword.getText().toString().equals("")){
                Toast.makeText(this, "Hãy nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                return;
            }
            if(edtConfirmPassword.getText().toString().equals("")){
                Toast.makeText(this, "Hãy nhập lại mật khẩu ", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                Toast.makeText(this, "Mật khẩu mới và mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!edtOldPassword.getText().toString().equals(oldUser.getPassword())){
                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            oldUser.setPassword(edtNewPassword.getText().toString());
            userDAO.update(Integer.parseInt(store.getValue("user_id", null)),oldUser);
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancel.setOnClickListener(view -> {
            finish();
        });

    }
}