package com.ai.educationteacherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.educationteacherapp.POJOs.StudentListPOJO;
import com.ai.educationteacherapp.databinding.EntityStudentListBinding;

import java.util.ArrayList;

public class StudentListRVAdapter extends RecyclerView.Adapter<StudentListRVAdapter.MyHolder> {

    ArrayList<StudentListPOJO> studentListPOJOArrayList;
    Context context;

    public StudentListRVAdapter(ArrayList<StudentListPOJO> studentListPOJOArrayList, Context context) {
        this.studentListPOJOArrayList = studentListPOJOArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(EntityStudentListBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.binding.tvStudentName.setText("" + studentListPOJOArrayList.get(position).getStudent_name());
        holder.binding.tvStudentRollno.setText("" + studentListPOJOArrayList.get(position).getStudent_roll_no());

    }

    @Override
    public int getItemCount() {
        return studentListPOJOArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        EntityStudentListBinding binding;

        public MyHolder(EntityStudentListBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

}
