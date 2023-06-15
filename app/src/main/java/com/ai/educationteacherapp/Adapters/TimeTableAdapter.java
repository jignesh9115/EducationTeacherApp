package com.ai.educationteacherapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ai.educationteacherapp.POJOs.TimeTablePOJO;
import com.ai.educationteacherapp.R;
import com.ai.educationteacherapp.StudentListActivity;
import com.ai.educationteacherapp.databinding.EntityTimeTableBinding;

import java.util.ArrayList;

public class TimeTableAdapter extends BaseAdapter {

    ArrayList<TimeTablePOJO> timeTablePOJOArrayList;
    Context context;

    EntityTimeTableBinding binding;

    public TimeTableAdapter(ArrayList<TimeTablePOJO> timeTablePOJOArrayList, Context context) {
        this.timeTablePOJOArrayList = timeTablePOJOArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return timeTablePOJOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeTablePOJOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return timeTablePOJOArrayList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view=convertView;

        if (view==null) {

            binding = EntityTimeTableBinding.inflate(LayoutInflater.from(context), parent, false);
            view = binding.getRoot();

        }

        binding.tvStartTime.setText(""+timeTablePOJOArrayList.get(position).getStart_time());
        binding.tvEndTime.setText(""+timeTablePOJOArrayList.get(position).getEnd_time());
        binding.tvSubject.setText(""+timeTablePOJOArrayList.get(position).getSubject_title());
        binding.tvStandard.setText(""+timeTablePOJOArrayList.get(position).getStandard_title());

        binding.cardTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, StudentListActivity.class);
                intent.putExtra("standard_id",timeTablePOJOArrayList.get(position).getStandard_id());
                intent.putExtra("subject_id",timeTablePOJOArrayList.get(position).getSubject_id());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
