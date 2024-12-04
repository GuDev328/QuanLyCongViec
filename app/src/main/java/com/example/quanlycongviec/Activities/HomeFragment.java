package com.example.quanlycongviec.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.quanlycongviec.Utils.Common;
import com.example.quanlycongviec.CustomAdapter.TaskAdapter;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DAO.TaskDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.TaskAction.TaskActionAdd;
import com.example.quanlycongviec.TaskAction.TaskActionView;
import com.example.quanlycongviec.TaskAction.TaskDoneView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

                        getDataTask();
                    }
                }
            });
    FloatingActionButton btnAdd;
    Button  btnPickDate;
    ListView listNotDone, listDone;
    TaskDAO taskDAO;
    ArrayList<Task_DTO> listNotDoneArr;
    ArrayList<Task_DTO> listDoneArr;
    CategoryDAO categoryDAO;
    Spinner spinnerCate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        taskDAO = new TaskDAO(getActivity());
        listNotDone = view.findViewById(R.id.listNotDone);
        listDone = view.findViewById(R.id.listDone);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnAdd= view.findViewById(R.id.btnTaskAdd);
        categoryDAO= new CategoryDAO(getActivity());
        spinnerCate= view.findViewById(R.id.spinnerCate);
        ArrayList<Category_DTO> cates = categoryDAO.getAll();
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add("Tất cả");
        for (Category_DTO cate :
                cates) {
            spinnerData.add(cate.getName());
        }
        Common.setAdapterSpinner(getActivity(), spinnerCate, spinnerData);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDataTask();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TaskActionAdd.class);
                activityResultLauncher.launch(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(calendar.getTime());
        btnPickDate.setText(today);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPickDate.getText().toString().equals("Chọn ngày")){
                    Common.showDatePicker(getActivity(), btnPickDate, -1,-1,-1, new Runnable() {
                        @Override
                        public void run() {
                            // Xử lý sau khi người dùng chọn ngày
                            getDataTask();
                        }
                    });
                }else{
                    String ngayDangChon = btnPickDate.getText().toString();
                    String ngayArr[] = ngayDangChon.split("/");
                    Common.showDatePicker(getActivity(), btnPickDate,Integer.parseInt(ngayArr[0]),Integer.parseInt(ngayArr[1]),Integer.parseInt(ngayArr[2]), new Runnable() {
                        @Override
                        public void run() {
                            // Xử lý sau khi người dùng chọn ngày
                            getDataTask();
                        }
                    });
                }

            }
        });


        listNotDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskActionView.class);

                intent.putExtra("selectedIdTask", listNotDoneArr.get(i).getId());
                activityResultLauncher.launch(intent);
            }
        });

        listDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskDoneView.class);

                intent.putExtra("selectedIdTask", listDoneArr.get(i).getId());
                activityResultLauncher.launch(intent);
            }
        });


        getDataTask();
        return view;
    }


    public long getCategoryID(){
        String name = spinnerCate.getSelectedItem().toString().trim();
        if(name.equals("Tất cả")){
            return -1;
        }
        ArrayList<Category_DTO> cates = categoryDAO.getAll();

        for (Category_DTO category : cates) {
            if (category.getName().equals(name)) {
                return category.getId();  // Trả về đối tượng tìm được
            }
        }
        return -1;
    }

    public void getDataTask(){
        listNotDoneArr= taskDAO.getListNotDone(btnPickDate.getText().toString(), getCategoryID());
        Collections.reverse(listNotDoneArr);
        TaskAdapter adapter = new TaskAdapter(getActivity(), R.layout.listview_list_task_not_done, listNotDoneArr);
        listNotDone.setAdapter(adapter);

        listDoneArr= taskDAO.getListDone(btnPickDate.getText().toString(), getCategoryID());
        Collections.reverse(listDoneArr);
        TaskAdapter adapter2 = new TaskAdapter(getActivity(), R.layout.listview_list_task_not_done, listDoneArr);
        listDone.setAdapter(adapter2);
    }
}