package com.example.quanlycongviec;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void setupListViewAdapter(Context context, ListView listView, ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    public static void setupListViewAdapterArr(Context context, ListView listView, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    public static void updateListViewData(ListView listView, ArrayList<String> dataList, String newData) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
        dataList.add(newData);
        adapter.notifyDataSetChanged();
    }

    public static void updateItemViewData(ListView listView, ArrayList<String> dataList, String newData) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
        dataList.add(newData);
        adapter.notifyDataSetChanged();
    }

    public static void deleteListViewData(ListView listView, ArrayList<String> dataList, int index) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
        dataList.remove(index);
        adapter.notifyDataSetChanged();
    }

    public static void updateItemListViewData(ListView listView, ArrayList<String> dataList, String newData, int index) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
//        int index = 0;
//        for(int i=0; i<dataList.size(); i++){
//            if(dataList.get(i).getMaSv().equals(newData.getMaSv())){
//                index=i;
//            }
//        }
        dataList.set(index, newData);
        adapter.notifyDataSetChanged();
    }

    public static String getSelectedRadioButtonValue(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return null;
        }
        RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }


    public static void setAdapterSpinner(Context context, Spinner spinner, ArrayList<String> list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void showDatePicker(Context context, Button btn, int dayDefault, int monthDefault, int yearDefault, final Runnable callback) {
        Calendar cal = Calendar.getInstance();
        int year = yearDefault == -1 ? cal.get(Calendar.YEAR) : yearDefault;
        int month = monthDefault == -1 ? cal.get(Calendar.MONTH) : monthDefault - 1;
        int day = dayDefault == -1 ? cal.get(Calendar.DAY_OF_MONTH) : dayDefault;

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        btn.setText(date);
                        if (callback != null) {
                            callback.run(); // gọi lại callback
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public static void showTimePickerDialog(Context context, Button btn,Integer hour, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        if (hour == -1 || minute == -1) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        btn.setText(selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true); // 'true' để hiển thị theo định dạng 24 giờ, 'false' cho AM/PM

        timePickerDialog.show();
    }
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
