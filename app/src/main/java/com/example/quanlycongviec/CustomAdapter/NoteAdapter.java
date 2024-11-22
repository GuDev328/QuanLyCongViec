package com.example.quanlycongviec.CustomAdapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviec.DAO.NoteDAO;
import com.example.quanlycongviec.DTO.Note_DTO;
import com.example.quanlycongviec.NoteAction.NoteActionEditActivity;
import com.example.quanlycongviec.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note_DTO> noteList;
    private Context context;
    private NoteDAO noteDAO;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    // Lưu trạng thái hiển thị của các dấu tích
    private ArrayList<Boolean> checkVisibility;

    // Lưu các trạng thái hiệu ứng rung
    private ArrayList<Boolean> isShaking;


    public NoteAdapter(ArrayList<Note_DTO> noteList, Context context, NoteDAO noteDAO, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.noteList = noteList;
        this.context = context;
        this.noteDAO = noteDAO;
        this.activityResultLauncher = activityResultLauncher;
        // Khởi tạo trạng thái hiển thị của nút tích
        this.checkVisibility = new ArrayList<>();
        this.isShaking = new ArrayList<>();
        for (int i = 0; i < noteList.size(); i++) {
            checkVisibility.add(false); // Tất cả ban đầu bị ẩn
            isShaking.add(false); // Tất cả ban đầu không rung
        }
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note_DTO card = noteList.get(position);
        holder.title.setText(card.getTitle());
        holder.content.setText(card.getContent());

        // Cập nhật trạng thái hiển thị của nút tích và màu nền
        if (checkVisibility.get(position)) {
            holder.checkButton.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.teal_2)); // Đổi màu
            startShaking(holder.cardView, position); // Bắt đầu rung
        } else {
            holder.checkButton.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white)); // Màu nền gốc
            stopShaking(holder.cardView, position); // Dừng rung
        }

        // Lưu id vào tag của view để dùng sau
        holder.itemView.setTag(card.getId());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    // Cập nhật trạng thái item khi long click
    public void toggleItemSelection(int position) {
        checkVisibility.set(position, !checkVisibility.get(position)); // Chuyển đổi trạng thái chọn
        notifyItemChanged(position); // Cập nhật lại item
    }

    // Hiệu ứng rung liên tục
    private void startShaking(View view, int position) {
        if (isShaking.get(position)) return; // Nếu đang rung thì không cần chạy lại

        isShaking.set(position, true);

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, 2, -2, 2, -2, 0);
        animator.setDuration(500); // Thời gian rung 500ms
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Lặp vô hạn
        animator.start();

        view.setTag(animator); // Lưu animator để dừng khi cần
    }

    // Dừng hiệu ứng rung
    private void stopShaking(View view, int position) {
        if (!isShaking.get(position)) return; // Nếu không rung thì không cần dừng

        isShaking.set(position, false);

        Object animatorTag = view.getTag();
        if (animatorTag instanceof ObjectAnimator) {
            ((ObjectAnimator) animatorTag).cancel(); // Dừng animator
        }

        view.setTranslationX(0); // Đặt lại vị trí
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageButton checkButton;
        CardView cardView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            content = itemView.findViewById(R.id.txtContent);
            checkButton = itemView.findViewById(R.id.iconCheck);
            cardView = (CardView) itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        long noteId = (long) view.getTag();
                        //Chuyển id qua màn hin sửa
                        Intent intent = new Intent(view.getContext(), NoteActionEditActivity.class);
                        intent.putExtra("selectedIdNote", noteId);
                        activityResultLauncher.launch(intent);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Đổi trạng thái hiển thị của nút tích
                        toggleItemSelection(position);
                    }
                    return true;
                }
            });
        }
    }
}
