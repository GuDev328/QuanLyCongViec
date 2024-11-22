package com.example.quanlycongviec.NoteAction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.Common;
import com.example.quanlycongviec.DAO.NoteDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Note_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.ShareStore;
import com.example.quanlycongviec.TaskAction.TaskActionEdit;
import com.example.quanlycongviec.TaskAction.TaskActionView;

public class NoteActionEditActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private ShareStore store;
    NoteDAO noteDAO;
    long user_id;
    Note_DTO noteSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_action_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Thêm icon quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }

        store = new ShareStore(NoteActionEditActivity.this);
        if (store.getValue("user_id", null) == null) {
            Intent it = new Intent(NoteActionEditActivity.this, LoginActivity.class);
            startActivity(it);
        }

        user_id = Long.parseLong(store.getValue("user_id", null));
        noteDAO = new NoteDAO(NoteActionEditActivity.this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);

        long noteId = getIntent().getLongExtra("selectedIdNote", -1); // -1 là giá trị mặc định nếu không có ID

        if (noteId != -1) {
            noteSelected = noteDAO.getById(Integer.parseInt(Long.toString(noteId)));
            editTextTitle.setText(noteSelected.getTitle());
            editTextContent.setText(noteSelected.getContent());
        } else {
            Toast.makeText(this, "Không xác định được ghi chú!", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            editNote();
            return true;
        } else if (id == R.id.action_delete) {
            deleteNote();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        Common.showConfirmDialog(NoteActionEditActivity.this, "Xoá ghi chú",
                "Bạn có chắc chắn muốn xoá ghi chú này?", new Runnable() {
                    @Override
                    public void run() {
                        noteDAO.delete(noteSelected.getId());
                        Toast.makeText(NoteActionEditActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
                        finish();
                    }
                });
    }

    private void editNote() {
        noteSelected.setTitle(editTextTitle.getText().toString().trim());
        noteSelected.setContent(editTextContent.getText().toString().trim());
        long noteId = noteDAO.update(noteSelected.getId(), noteSelected);
        if (noteId != -1) {
            Toast.makeText(NoteActionEditActivity.this, "Chỉnh sửa ghi chú thành công", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
        } else {
            Toast.makeText(NoteActionEditActivity.this, "Chỉnh sửa ghi chú thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}