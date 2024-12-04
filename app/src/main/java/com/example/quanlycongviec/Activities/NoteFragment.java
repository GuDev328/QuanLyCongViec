package com.example.quanlycongviec.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlycongviec.Utils.Common;
import com.example.quanlycongviec.CustomAdapter.NoteAdapter;
import com.example.quanlycongviec.DAO.NoteDAO;
import com.example.quanlycongviec.DTO.Note_DTO;
import com.example.quanlycongviec.NoteAction.NoteActionAddActivity;
import com.example.quanlycongviec.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    //Chạy lại khi đóng activity khác
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getDataNote();
                    }
                }
            });

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note_DTO> noteList = null;
    private ArrayList<Note_DTO> filteredNoteList = null; // Thêm danh sách lọc
    private NoteDAO noteDao;
    private EditText txtSearch;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        toolbar = view.findViewById(R.id.toolbarNote);
        txtSearch = view.findViewById(R.id.txtSearch);

        toolbar.setVisibility(View.GONE); //ẩn toolbar ban đầu
        txtSearch.setVisibility(View.VISIBLE); //hiện txtSearch ban đầu
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        noteDao = new NoteDAO(getActivity());

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        // Sử dụng StaggeredGridLayoutManager để hiển thị danh sách theo dạng lưới
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        // Khởi tạo FloatingActionButton
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddNote);
        // Xử lý sự kiện khi FloatingActionButton được nhấn
        fabAdd.setOnClickListener(v -> {
            //Chuyển sang màn hình thêm mới
            Intent intent = new Intent(getContext(), NoteActionAddActivity.class);
            activityResultLauncher.launch(intent);
        });

        // Lắng nghe sự thay đổi trong EditText để lọc dữ liệu
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterNotes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        getDataNote();

        return view;
    }

    public void getDataNote() {
        noteList = noteDao.getAll();
        noteAdapter = new NoteAdapter(noteList, getContext(), noteDao, activityResultLauncher, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.checkall_option) {
            // Xử lý khi người dùng chọn "Check All"
            noteAdapter.selectAll();
            updateToolbarIcons();
            return true;
        } else if (id == R.id.uncheckall_option) {
            noteAdapter.clearSelection();
            updateToolbarIcons();
            toolbar.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            return true;
        }else if (id == R.id.delete_option) {
            deleteSelectedNotes();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    private void deleteSelectedNotes() {
        // Hiển thị hộp thoại xác nhận trước khi xóa
        Common.showConfirmDialog(getActivity(), "Xoá ghi chú", "Bạn có chắc chắn muốn xóa các ghi chú đã chọn?", new Runnable() {
            @Override
            public void run() {
                try {
                    int i = noteDao.deleteMultiple(noteAdapter.getSelectedNotes()); // Xóa trong database
                    noteAdapter.deleteSelectedNotes(); // Xóa ghi chú đã chọn trong adapter
                    onMultiSelectModeEnd(); // Kết thúc chế độ chọn
                    Toast.makeText(getContext(), "Xóa thành công " + i + " ghi chú", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateToolbarIcons() {
        MenuItem checkAllItem = toolbar.getMenu().findItem(R.id.checkall_option);
        MenuItem uncheckAllItem = toolbar.getMenu().findItem(R.id.uncheckall_option);

        boolean allSelected = noteAdapter.hasSelectedItems() && noteAdapter.getSelectedNotes().size() == noteAdapter.getItemCount();

        checkAllItem.setVisible(!allSelected); // Ẩn nếu đã chọn tất cả
        uncheckAllItem.setVisible(allSelected); // Hiện nếu đã chọn tất cả
    }

    // Xử lý khi chế độ chọn kết thúc
    public void onMultiSelectModeEnd() {
        toolbar.setVisibility(View.GONE); // Ẩn toolbar
        txtSearch.setVisibility(View.VISIBLE); // Hiện lại EditText
        noteAdapter.clearSelection(); // Xóa trạng thái chọn
    }

    // Lọc dữ liệu dựa trên từ khóa tìm kiếm
    private void filterNotes(String query) {
        // Nếu từ khóa tìm kiếm rỗng, hiển thị tất cả ghi chú
        if (query.isEmpty()) {
            filteredNoteList = new ArrayList<>(noteList); // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
        } else {
            filteredNoteList = new ArrayList<>();
            for (Note_DTO note : noteList) {
                if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        note.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredNoteList.add(note); // Thêm các ghi chú khớp với từ khóa vào danh sách
                }
            }
        }
        noteAdapter.updateNoteList(filteredNoteList); // Cập nhật danh sách vào adapter
    }
}