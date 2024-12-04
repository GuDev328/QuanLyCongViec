package com.example.quanlycongviec.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.Utils.Common;
import com.example.quanlycongviec.DAO.UserDAO;
import com.example.quanlycongviec.DTO.User_DTO;
import com.example.quanlycongviec.ProfileAction.ChangePasswordActivity;
import com.example.quanlycongviec.R;
import com.example.quanlycongviec.Utils.ShareStore;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    Button btnChangePassword, btnBirthDay,btnConfirmEdit ,btnCancelEdit, btnEdit, btnLogout;
    UserDAO userDAO;
    User_DTO userDTO;
    ShareStore store;
    EditText edtEmail, edtName;
    RadioGroup groupGender;
    ImageView avatar;
    byte[] tempArrByteAvatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userDAO = new UserDAO(getActivity());
        store = new ShareStore(getActivity());
        edtEmail= view.findViewById(R.id.editEmail);
        edtName = view.findViewById(R.id.editName);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit);
        btnBirthDay = view.findViewById(R.id.btnBirthDay);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        groupGender =  view.findViewById(R.id.groupGender);
        btnEdit = view.findViewById(R.id.btnEdit);
        avatar = view.findViewById(R.id.avatar);
btnLogout=view.findViewById(R.id.btnLogout);
        hideControls();
       //btnEdit.setVisibility(View.VISIBLE);

        avatar.setOnClickListener(v -> {
            // Mở thư viện ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 989);
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store.clearAll();
                Intent it = new Intent(getActivity(), LoginActivity.class);
                startActivity(it);

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showControls();
            }
        });

        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if(edtName.getText().toString().trim().equals("")){
                            Toast.makeText(getActivity(), "Hãy nhập họ tên", Toast.LENGTH_SHORT).show();
                            return;
                        }
                User_DTO oldUser = userDAO.getById(Integer.parseInt(store.getValue("user_id", null)) );
                        oldUser.setAvatar(tempArrByteAvatar);
                        oldUser.setName(edtName.getText().toString().trim());
                        oldUser.setBirthday(Common.getSelectedRadioButtonValue(groupGender));
                        userDAO.update(oldUser.getId(),oldUser);
                        Toast.makeText(getActivity(), "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
                hideControls();
            }
        });


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(it);
            }
        });

        btnBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnBirthDay.getText().toString().equals("Chọn ngày")) {
                    Common.showDatePicker(getActivity(), btnBirthDay, -1, -1, -1, null);
                } else {
                    String ngayDangChon = btnBirthDay.getText().toString();
                    String ngayArr[] = ngayDangChon.split("/");

                    Common.showDatePicker(getActivity(), btnBirthDay, Integer.parseInt(ngayArr[0]), Integer.parseInt(ngayArr[1]), Integer.parseInt(ngayArr[2]), null);
                }
            }
        });
        getUserInfo();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 989 && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                // Lấy Bitmap từ URI
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
                // Hiển thị ảnh trên ImageView
                avatar.setImageBitmap(bitmap);
                tempArrByteAvatar= bitMapToByteArray(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  byte[] bitMapToByteArray(Bitmap bitmap) {
        // Chuyển đổi Bitmap thành mảng byte (BLOB)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void getUserInfo(){
       userDTO = userDAO.getById(Integer.parseInt(store.getValue("user_id", null)) );
        edtEmail.setText(userDTO.getEmail());
        edtName.setText(userDTO.getName());
        btnBirthDay.setText(userDTO.getBirthday());
        tempArrByteAvatar= userDTO.getAvatar();
        avatar.setImageBitmap(BitmapFactory.decodeByteArray(userDTO.getAvatar(), 0, userDTO.getAvatar().length));
        for (int i = 0; i < groupGender.getChildCount(); i++) {
            View view = groupGender.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(userDTO.getGender())) {
                    radioButton.setChecked(true);
                    break; // Thoát vòng lặp khi tìm thấy
                }
            }
        }
    }

    public void hideControls(){
        edtName.setEnabled(false);
        btnBirthDay.setEnabled(false);
        for (int i = 0; i < groupGender.getChildCount(); i++) {
            View view = groupGender.getChildAt(i);
            if (view instanceof RadioButton) {
                view.setEnabled(false);
            }
        }
        avatar.setEnabled(false);
        btnEdit.setVisibility(View.VISIBLE);
        btnCancelEdit.setVisibility(View.GONE);
        btnConfirmEdit.setVisibility(View.GONE);
    }

    public void showControls(){
        edtName.setEnabled(true);
        btnBirthDay.setEnabled(true);
        for (int i = 0; i < groupGender.getChildCount(); i++) {
            View view = groupGender.getChildAt(i);
            if (view instanceof RadioButton) {
                view.setEnabled(true);
            }
        }
        avatar.setEnabled(true);
        btnEdit.setVisibility(View.GONE);
        btnCancelEdit.setVisibility(View.VISIBLE);
        btnConfirmEdit.setVisibility(View.VISIBLE);
    }

}