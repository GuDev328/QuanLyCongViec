package com.example.quanlycongviec.NoteAction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.DAO.NoteDAO;
import com.example.quanlycongviec.DTO.Note_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.Utils.ShareStore;

public class NoteActionAddActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private ShareStore store;
    NoteDAO noteDAO;
    long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_action_add);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        // Thêm icon quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }

        store = new ShareStore(NoteActionAddActivity.this);
        if (store.getValue("user_id", null) == null) {
            Intent it = new Intent(NoteActionAddActivity.this, LoginActivity.class);
            startActivity(it);
        }

        user_id = Long.parseLong(store.getValue("user_id", null));
        noteDAO = new NoteDAO(NoteActionAddActivity.this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveNote();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    // Lưu ghi chú
    private void saveNote() {
        // Kiểm tra xem tiêu đề và nội dung có được nhập hay không
        if (editTextTitle.getText().toString().trim().equals("")) {
            Toast.makeText(NoteActionAddActivity.this, "Nhập tiêu đề ghi chú", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra xem nội dung có được nhập hay không
        if (editTextContent.getText().toString().trim().equals("")) {
            Toast.makeText(NoteActionAddActivity.this, "Nhập nội dung ghi chú", Toast.LENGTH_SHORT).show();
            return;
        }
        // Nếu tiêu đề và nội dung đều được nhập, tạo mới ghi chú
        Note_DTO newNote = new Note_DTO(0, user_id, editTextTitle.getText().toString().trim(), editTextContent.getText().toString().trim());
        // Thêm ghi chú vào database
        long noteId = noteDAO.insert(newNote);
        if (noteId != -1) {
            // Hiển thị thông báo thành công
            Toast.makeText(NoteActionAddActivity.this, "Tạo ghi chú thành công", Toast.LENGTH_SHORT).show();
            // Trả về kết quả thành công
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);  // Gửi kết quả về Activity 1
            finish();
        } else {
            Toast.makeText(NoteActionAddActivity.this, "Tạo ghi chú thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}