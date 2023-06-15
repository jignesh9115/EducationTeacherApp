package com.ai.educationteacherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.educationteacherapp.POJOs.FileAttachementPOJO;
import com.ai.educationteacherapp.databinding.EntityAttachmentfileListBinding;

import java.util.ArrayList;

public class AttachmentListRVAdapter extends RecyclerView.Adapter<AttachmentListRVAdapter.MyHolder>{

    ArrayList<FileAttachementPOJO> arrayList_attachmentFile;
    Context context;

    public AttachmentListRVAdapter(ArrayList<FileAttachementPOJO> arrayList_attachmentFile, Context context) {
        this.arrayList_attachmentFile = arrayList_attachmentFile;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(EntityAttachmentfileListBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        holder.binding.tvAttendanceNo.setText(""+(position+1));
        holder.binding.tvAssignmentFilename.setText(""+arrayList_attachmentFile.get(position).getName());

        holder.binding.tvAssignmentFilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+arrayList_attachmentFile.get(position).getUri(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList_attachmentFile.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        EntityAttachmentfileListBinding binding;

        public MyHolder(EntityAttachmentfileListBinding b) {
            super(b.getRoot());
            binding=b;
        }
    }

}
