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

import com.example.quanlycongviec.CategoryAction.CategoryAdd;
import com.example.quanlycongviec.CategoryAction.CategoryView;
import com.example.quanlycongviec.CustomAdapter.CategoryAdapter;
import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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

    Button btnAddDM;
    ListView listDM;
    ArrayList<Category_DTO> listDmArr;
    CategoryDAO danhMucDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list, container, false);

        danhMucDAO = new CategoryDAO(getActivity());
        listDM = view.findViewById(R.id.listViewDanhMuc);
        btnAddDM= view.findViewById(R.id.btnThemDM);

        btnAddDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryAdd.class);
                activityResultLauncher.launch(intent);
            }
        });

        listDM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CategoryView.class);

                intent.putExtra("selectedCategoryId", listDmArr.get(i).getId());
                activityResultLauncher.launch(intent);
            }
        });

        getDataTask();
        return view;
    }

    public void getDataTask(){
        listDmArr= danhMucDAO.getAll();
        CategoryAdapter adapter2 = new CategoryAdapter(getActivity(), R.layout.listview_category, listDmArr);
        listDM.setAdapter(adapter2);
    }

}