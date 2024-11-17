package com.example.quanlycongviec.TaskAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.Common;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.MainActivity;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.ShareStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskActionAdd extends AppCompatActivity {
    Button btnPickDate, btnPickTime, btnCancel, btnConfirm;
    EditText edtTitle, edtDescription;
    TaskDAO taskDAO;
    CategoryDAO categoryDAO;
    Spinner spinnerCate;
    ShareStore store;
    long user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.task_action_add);
        store= new ShareStore(TaskActionAdd.this);
        if(store.getValue("user_id", null) == null){
            Intent it = new Intent(TaskActionAdd.this, LoginActivity.class);
            startActivity(it);
        }
        user_id = Long.parseLong(store.getValue("user_id", null));
        taskDAO = new TaskDAO(TaskActionAdd.this);
        categoryDAO= new CategoryDAO(TaskActionAdd.this);
        spinnerCate= findViewById(R.id.spinnerCate);
        edtTitle= findViewById(R.id.edtTitle);
        edtDescription= findViewById(R.id.edtDescription);
        ArrayList<Category_DTO> cates = categoryDAO.getAll();
        ArrayList<String> spinnerData = new ArrayList<>();
        for (Category_DTO cate :
                cates) {
            spinnerData.add(cate.getName());
        }
        Common.setAdapterSpinner(TaskActionAdd.this, spinnerCate, spinnerData);
        

        //Set Width cho Dialog
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);

        //Set ngày mặc dinh
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(calendar.getTime());
        btnPickDate.setText(today);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPickDate.getText().toString().equals("Chọn ngày")){
                    Common.showDatePicker(TaskActionAdd.this, btnPickDate, -1,-1,-1, null);
                }else{
                    String ngayDangChon = btnPickDate.getText().toString();
                    String ngayArr[] = ngayDangChon.split("/");
                    Common.showDatePicker(TaskActionAdd.this, btnPickDate,Integer.parseInt(ngayArr[0]),Integer.parseInt(ngayArr[1]),Integer.parseInt(ngayArr[2]), null);
                }

            }
        });

        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPickTime.getText().toString().equals("__:__")){
                    Common.showTimePickerDialog(TaskActionAdd.this, btnPickTime,-1,-1);
                }else{
                    String gioDangChon = btnPickTime.getText().toString();
                    String gioArr[] = gioDangChon.split(":");
                    Common.showTimePickerDialog(TaskActionAdd.this, btnPickTime,Integer.parseInt(gioArr[0]),Integer.parseInt(gioArr[1]));
                }
            }
        });

        btnCancel = findViewById(R.id.btnCancelAddTask);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirm = findViewById(R.id.btnConfirmAddTask);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTitle.getText().toString().trim().equals("")){
                    Toast.makeText(TaskActionAdd.this, "Nhập tên công việc", Toast.LENGTH_SHORT).show();
                    return;
                }
                String cateName=spinnerCate.getSelectedItem().toString();

                Task_DTO newTask = new Task_DTO(0, findCategoryByName(cates, cateName),
                        user_id,
                        btnPickDate.getText().toString().trim(),
                        btnPickTime.getText().toString().trim(),
                        edtTitle.getText().toString().trim(),
                        edtDescription.getText().toString().trim(),
                        0);
               long taskId = taskDAO.insert(newTask);
                if(taskId!= -1){
                    Toast.makeText(TaskActionAdd.this, "Tạo công việc thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                    finish();
                }else {
                    Toast.makeText(TaskActionAdd.this, "Tạo công việc thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public static long findCategoryByName(ArrayList<Category_DTO> categoryList, String name) {
        for (Category_DTO category : categoryList) {
            if (category.getName().equals(name)) {
                return category.getId();  // Trả về đối tượng tìm được
            }
        }
        return -1;  // Nếu không tìm thấy, trả về null
    }
}