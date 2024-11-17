package com.example.quanlycongviec.CustomAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.DTO.Task_DTO;
import com.example.quanlycongviec.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task_DTO> {
    Activity context;
    int layoutID;
    ArrayList<Task_DTO> list = new ArrayList<Task_DTO>();
    CategoryDAO categoryDAO ;
    public TaskAdapter(@NonNull Activity context, int resource,
                     @NonNull ArrayList<Task_DTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.categoryDAO = new CategoryDAO(context);
        this.layoutID = resource;
        this.list =  objects;
    }

    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, parent, false);  // Thêm parent vào để tối ưu việc tạo view
        }

        if (list != null && list.size() > 0 && position >= 0) {
            TextView timeText = convertView.findViewById(R.id.timeText);
            TextView titleText = convertView.findViewById(R.id.titleText);
            TextView cateText = convertView.findViewById(R.id.titleCate);

            Task_DTO task = list.get(position);
            Category_DTO cate = categoryDAO.getById(Integer.parseInt( Long.toString(task.getCategoryId()) ) );
            timeText.setText(task.getTime());
            titleText.setText(task.getTitle());
            cateText.setText(cate.getName());
        }

        return convertView;
    }
}

