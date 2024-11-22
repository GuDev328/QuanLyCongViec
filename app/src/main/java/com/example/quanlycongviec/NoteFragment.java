package com.example.quanlycongviec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.quanlycongviec.CustomAdapter.NoteAdapter;
import com.example.quanlycongviec.DAO.NoteDAO;
import com.example.quanlycongviec.DTO.Note_DTO;
import com.example.quanlycongviec.NoteAction.NoteActionAddActivity;
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
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        noteDao = new NoteDAO(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddNote);
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

        // Để khi có item được chọn thì toolbar sẽ xuất hiện
        recyclerView.setOnLongClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            noteAdapter.toggleItemSelection(position); // Chuyển trạng thái của item

            // Kiểm tra xem còn item nào được chọn hay không
            if (noteAdapter.hasSelectedItems()) {
                toolbar.setVisibility(View.VISIBLE); // Hiển thị toolbar khi có item được chọn
                txtSearch.setVisibility(View.GONE); // Ẩn txtSearch khi có item được chọn
            } else {
                toolbar.setVisibility(View.GONE); // Ẩn toolbar khi không còn item nào được chọn
                txtSearch.setVisibility(View.VISIBLE); // Hiển thị lại txtSearch
            }
            return true; // Xử lý nhấn lâu
        });

        return view;
    }

    public void getDataNote() {
        noteList = noteDao.getAll();
        noteAdapter = new NoteAdapter(noteList, getContext(), noteDao, activityResultLauncher, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    // Cập nhật trạng thái khi bỏ chọn item
    public void onItemUnselected() {
        toolbar.setVisibility(View.GONE); // Ẩn toolbar
        txtSearch.setVisibility(View.VISIBLE); // Hiện lại EditText
    }

    // Lọc dữ liệu dựa trên từ khóa tìm kiếm
    private void filterNotes(String query) {
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