package com.example.quanlycongviec.TaskAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.Common;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.R;

public class TaskDoneView extends AppCompatActivity {
    TextView txtDateTime, txtCate, txtTitle, txtDescription;
    Button btnConfirmReDoTask, btnCancelViewTask;
    TaskDAO taskDAO;
    CategoryDAO categoryDAO;
    ImageView  iconDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.task_done_view);
        //Set Width cho Dialog
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9), // Width 90% màn hình
                    WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
            );
        }
        txtDateTime= findViewById(R.id.txtDateTime);
        txtCate= findViewById(R.id.txtCate);
        txtTitle= findViewById(R.id.txtTitle);
        txtDescription= findViewById(R.id.txtDescription);
        taskDAO = new TaskDAO(getApplication());
        categoryDAO= new CategoryDAO(getApplication());
        long taskId = getIntent().getLongExtra("selectedIdTask", -1); // -1 là giá trị mặc định nếu không có ID

        if (taskId != -1) {
            Task_DTO task = taskDAO.getById(Integer.parseInt(Long.toString(taskId)));
            Category_DTO cate = categoryDAO.getById( Integer.parseInt(Long.toString(task.getCategoryId())) );
            txtCate.setText(cate.getName());
            txtDescription.setText(task.getDescription());
            txtTitle.setText(task.getTitle());
            txtDateTime.setText(task.getTime()+" - "+ task.getDate());

            btnCancelViewTask = findViewById(R.id.btnCancelViewTask);
            btnCancelViewTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btnConfirmReDoTask = findViewById(R.id.btnConfirmReDoTask);
            btnConfirmReDoTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.setStatus(0);
                    taskDAO.update(task.getId(), task);
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                    finish();
                }
            });

            iconDelete= findViewById(R.id.iconDelete);
            iconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.showConfirmDialog(TaskDoneView.this, "Xoá công việc",
                            "Bạn có chắc chắn muốn xoá công việc này?", new Runnable() {
                                @Override
                                public void run() {
                                    taskDAO.delete(task.getId());
                                    Toast.makeText(TaskDoneView.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                                    Intent resultIntent = new Intent();
                                    setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                                    finish();
                                }
                            });

                }
            });


        } else {
            Toast.makeText(this, "Không xác định được công việc!", Toast.LENGTH_SHORT).show();
        }
    }
}