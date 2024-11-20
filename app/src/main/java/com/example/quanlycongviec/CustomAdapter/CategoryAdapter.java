package com.example.quanlycongviec.CustomAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlycongviec.DAO.CategoryDAO;
import com.example.quanlycongviec.DTO.Category_DTO;
import com.example.quanlycongviec.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category_DTO> {
    private final Activity context;
    private final int layoutID;
    private final ArrayList<Category_DTO> categoryList;
    private final CategoryDAO categoryDAO;

    public CategoryAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Category_DTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.categoryList = objects;
        this.categoryDAO = new CategoryDAO(context);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, parent, false);
        }

        if (categoryList != null && categoryList.size() > 0 && position >= 0) {
            TextView categoryNameText = convertView.findViewById(R.id.txtTenDM);
            TextView categoryDescriptionText = convertView.findViewById(R.id.txtMoTaDM);

            Category_DTO category = categoryList.get(position);

            categoryNameText.setText(category.getName());
            categoryDescriptionText.setText(category.getDescription());
        }

        return convertView;
    }
}
