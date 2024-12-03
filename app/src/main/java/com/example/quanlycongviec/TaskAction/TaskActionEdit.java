package com.example.quanlycongviec.TaskAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.Utils.Common;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.Utils.ShareStore;

import java.util.ArrayList;

public class TaskActionEdit extends AppCompatActivity {
    Button btnPickDate, btnPickTime, btnCancel, btnConfirm;
    EditText edtTitle, edtDescription;
    TaskDAO taskDAO;
    CategoryDAO categoryDAO;
    Spinner spinnerCate;
    ShareStore store;
    long user_id;
    Task_DTO taskSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.task_action_add);
        store= new ShareStore(TaskActionEdit.this);
        if(store.getValue("user_id", null) == null){
            Intent it = new Intent(TaskActionEdit.this, LoginActivity.class);
            startActivity(it);
        }
        user_id = Long.parseLong(store.getValue("user_id", null));
        taskDAO = new TaskDAO(TaskActionEdit.this);
        categoryDAO= new CategoryDAO(TaskActionEdit.this);
        spinnerCate= findViewById(R.id.spinnerCate);
        edtTitle= findViewById(R.id.edtTitle);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        edtDescription= findViewById(R.id.edtDescription);
        ArrayList<Category_DTO> cates = categoryDAO.getAll();
        ArrayList<String> spinnerData = new ArrayList<>();
        for (Category_DTO cate :
                cates) {
            spinnerData.add(cate.getName());
        }
        Common.setAdapterSpinner(TaskActionEdit.this, spinnerCate, spinnerData);

        long taskId = getIntent().getLongExtra("selectedIdTask", -1); // -1 là giá trị mặc định nếu không có ID

        if (taskId != -1) {
            taskSelected  = taskDAO.getById(Integer.parseInt(Long.toString(taskId)));
            Category_DTO cateSelected = categoryDAO.getById( Integer.parseInt(Long.toString(taskSelected.getCategoryId())) );

            btnPickDate.setText(taskSelected.getDate());
            btnPickTime.setText(taskSelected.getTime());
            edtTitle.setText(taskSelected.getTitle());
            edtDescription.setText(taskSelected.getDescription());

            for(int i=0; i< spinnerData.size(); i++){
                if(spinnerData.get(i).equals(cateSelected.getName())){
                    spinnerCate.setSelection(i);
                }
            }



            btnPickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btnPickDate.getText().toString().equals("Chọn ngày")){
                        Common.showDatePicker(TaskActionEdit.this, btnPickDate, -1,-1,-1, null);
                    }else{
                        String ngayDangChon = btnPickDate.getText().toString();
                        String ngayArr[] = ngayDangChon.split("/");
                        Common.showDatePicker(TaskActionEdit.this, btnPickDate,Integer.parseInt(ngayArr[0]),Integer.parseInt(ngayArr[1]),Integer.parseInt(ngayArr[2]), null);
                    }

                }
            });

            btnPickTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btnPickTime.getText().toString().equals("__:__")){
                        Common.showTimePickerDialog(TaskActionEdit.this, btnPickTime,-1,-1);
                    }else{
                        String gioDangChon = btnPickTime.getText().toString();
                        String gioArr[] = gioDangChon.split(":");
                        Common.showTimePickerDialog(TaskActionEdit.this, btnPickTime,Integer.parseInt(gioArr[0]),Integer.parseInt(gioArr[1]));
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
                        Toast.makeText(TaskActionEdit.this, "Nhập tên công việc", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Set lại giá trị cho taskSelected để Update
                    String cateName=spinnerCate.getSelectedItem().toString();

                    taskSelected.setStatus(0);
                    taskSelected.setDate(btnPickDate.getText().toString().trim());
                    taskSelected.setTitle(edtTitle.getText().toString().trim());
                    taskSelected.setTime(btnPickTime.getText().toString().trim());
                    taskSelected.setCategoryId(findCategoryByName(cates, cateName));
                    taskSelected.setDescription(edtDescription.getText().toString().trim());

                    long taskId = taskDAO.update( taskSelected.getId() ,taskSelected);
                    if(taskId!= -1){
                        Toast.makeText(TaskActionEdit.this, "Chỉnh sửa công việc thành công", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                        finish();
                    }else {
                        Toast.makeText(TaskActionEdit.this, "Tạo công việc thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Không xác định được công việc!", Toast.LENGTH_SHORT).show();
        }

        //Set Width cho Dialog
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }
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