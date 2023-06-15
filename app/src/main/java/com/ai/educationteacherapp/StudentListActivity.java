package com.ai.educationteacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.ai.educationteacherapp.POJOs.AttendanceListPOJO;
import com.ai.educationteacherapp.POJOs.StudentListPOJO;
import com.ai.educationteacherapp.databinding.ActivityStudentListBinding;
import com.ai.educationteacherapp.databinding.EntityStudentListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StudentListActivity extends AppCompatActivity {

    ActivityStudentListBinding binding;

    String saurl="",url = "", response = "",saresponse = "";
    ArrayList<StudentListPOJO> studentListPOJOArrayList;
    StudentListPOJO studentListPOJO;

    StudentListAdapter studentListRVAdapter;

    RecyclerView.LayoutManager layoutManager;

    String subject_id, standard_id, attendance_date;

    ArrayList<AttendanceListPOJO> arrayList_attendance_list;
    AttendanceListPOJO attendanceListPOJO;

    SharedPreferences sp;

    int teacher_id = 0;

    DatePickerDialog dp;
    Calendar cal;
    int m, y, d;

    GDateTime gDateTime = new GDateTime();

    AlertDialog ad;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("techerDetail", Context.MODE_PRIVATE);
        teacher_id = Integer.parseInt(sp.getString("teacher_id", ""));

        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        Bundle bundle = getIntent().getExtras();
        standard_id = bundle.getString("standard_id");
        subject_id = bundle.getString("subject_id");

        arrayList_attendance_list = new ArrayList<>();
        studentListPOJOArrayList = new ArrayList<>();
        new getStudentListTask().execute();

        binding.tvAttendanceDate.setText("" + gDateTime.getDatedmy());
        attendance_date = gDateTime.getDateymd();

        binding.tvAttendanceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                y = Integer.parseInt(gDateTime.getYear());
                m = Integer.parseInt(gDateTime.getMonth()) - 1;
                d = Integer.parseInt(gDateTime.getDay());

                dp = new DatePickerDialog(StudentListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        attendance_date = df.format(cal.getTime());
                        binding.tvAttendanceDate.setText("" + dff.format(cal.getTime()));
                    }
                }, y, m, d);
                dp.show();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Gson gson = new GsonBuilder().create();
                JsonArray attendance_jsonArray = gson.toJsonTree(arrayList_attendance_list).getAsJsonArray();*/

                JSONObject attendance_data = new JSONObject();// main object
                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                for (int i = 0; i < arrayList_attendance_list.size(); i++) {
                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        jGroup.put("student_id", arrayList_attendance_list.get(i).getAttend_student_id());
                        jGroup.put("status", arrayList_attendance_list.get(i).getAttend_status());

                        jArray.put(jGroup);

                        // /attendance_data Name is JsonArray Name
                        attendance_data.put("attendance_data", jArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Toast.makeText(StudentListActivity.this, "" + jArray, Toast.LENGTH_SHORT).show();

                saurl=getString(R.string.Base_url)+"add_attendance.php?attedance_date="+attendance_date+"&teacher_id="+teacher_id+"&standard_id="+standard_id+"&subject_id="+subject_id+"&attedance_data="+jArray;

                new submitAttendanceTask().execute(saurl+"");

            }
        });


    }

    //================get student list by standard id task starts=====================

    public class getStudentListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            url = getString(R.string.Base_url) + getString(R.string.get_student_list_by_standard_id_url) + standard_id;
            Log.d("get Student List url=>", url + "");

            HttpHandler httpHandler = new HttpHandler();
            response = httpHandler.makeServiceCall(url + "");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("response=>", response + "");

            getStudentList();

        }

    }


    private void getStudentList() {

        try {
            JSONObject jsonObject = new JSONObject(response + "");
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                studentListPOJO = new StudentListPOJO(object.getString("student_name"),
                        object.getString("student_birthdate"),
                        object.getString("student_roll_no"),
                        object.getString("student_photo"),
                        object.getString("student_standard"),
                        object.getString("student_division"),
                        object.getString("student_gnr_no"),
                        object.getString("student_id"),
                        object.getString("standard_id"));

                studentListPOJOArrayList.add(studentListPOJO);

                attendanceListPOJO = new AttendanceListPOJO(object.getString("student_id"), "0");

                arrayList_attendance_list.add(attendanceListPOJO);

            }

            studentListRVAdapter = new StudentListAdapter(studentListPOJOArrayList, StudentListActivity.this);
            binding.rvStudentList.setLayoutManager(layoutManager);
            binding.rvStudentList.setAdapter(studentListRVAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //================get student list task ends=======================


    //=====================Recycler view Adapter code starts===================

    public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

        ArrayList<StudentListPOJO> studentListPOJOArrayList;
        Context context;

        boolean isSelectedAll;

        public StudentListAdapter(ArrayList<StudentListPOJO> studentListPOJOArrayList, Context context) {
            this.studentListPOJOArrayList = studentListPOJOArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(EntityStudentListBinding.inflate(LayoutInflater.from(getApplicationContext()), parent, false));
        }

        public void selectAll() {
            isSelectedAll = true;
            notifyDataSetChanged();
        }

        public void UnselectAll() {
            isSelectedAll = false;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            holder.studentListBinding.tvStudentName.setText("" + studentListPOJOArrayList.get(position).getStudent_name());
            holder.studentListBinding.tvStudentRollno.setText("" + studentListPOJOArrayList.get(position).getStudent_roll_no());

            holder.studentListBinding.cbAttendanceStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (holder.studentListBinding.cbAttendanceStatus.isChecked()) {

                        //Toast.makeText(context, "Present", Toast.LENGTH_SHORT).show();
                        arrayList_attendance_list.set(position, new AttendanceListPOJO(studentListPOJOArrayList.get(position).getStudent_id(), "1"));

                    } else {

                        //Toast.makeText(context, "Absent", Toast.LENGTH_SHORT).show();
                        arrayList_attendance_list.set(position, new AttendanceListPOJO(studentListPOJOArrayList.get(position).getStudent_id(), "0"));

                    }
                }

            });

            if (!isSelectedAll) holder.studentListBinding.cbAttendanceStatus.setChecked(false);
            else holder.studentListBinding.cbAttendanceStatus.setChecked(true);


            binding.cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (binding.cbSelectAll.isChecked()) {
                        selectAll();
                    } else {
                        UnselectAll();
                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return studentListPOJOArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            EntityStudentListBinding studentListBinding;

            public MyViewHolder(EntityStudentListBinding slbinding) {
                super(slbinding.getRoot());
                studentListBinding = slbinding;
            }
        }

    }

    //=====================Recycler view Adapter code ends===================


    //==========================submit attendance code starts==========================

    public class submitAttendanceTask extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            Log.d("add url=>",saurl+"");

            HttpHandler  httpHandler=new HttpHandler();
            saresponse=httpHandler.makeServiceCall(saurl+"");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("response=>",saresponse+"");

            if (saresponse.contains("added successfully.")){

                showDialog();

            }else {

                Toast.makeText(StudentListActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //==========================submit attendance code ends==============================


    public void showDialog(){

        builder=new AlertDialog.Builder(StudentListActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon_successfully);
        builder.setMessage("Attendance Submitted Successfully...");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });

        ad=builder.create();
        ad.show();

        ad.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }


}