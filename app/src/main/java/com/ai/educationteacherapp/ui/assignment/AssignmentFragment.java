package com.ai.educationteacherapp.ui.assignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.educationteacherapp.Adapters.AttachmentListRVAdapter;
import com.ai.educationteacherapp.GDateTime;
import com.ai.educationteacherapp.HttpHandler;
import com.ai.educationteacherapp.POJOs.FileAttachementPOJO;
import com.ai.educationteacherapp.R;
import com.ai.educationteacherapp.StudentListActivity;
import com.ai.educationteacherapp.databinding.FragmentAssignmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AssignmentFragment extends Fragment {

    private AssignmentViewModel slideshowViewModel;

    FragmentAssignmentBinding binding;

    DatePickerDialog dp;
    Calendar cal;
    int m, y, d;

    GDateTime gDateTime = new GDateTime();

    String assignment_title = "", assignment_description = "", due_date = "No Due Date", standard_id = "", subject_id = "",assignment_date="";

    String stdurl = "", stdresponse = "", suburl = "", subresponse = "", submitassginmenturl = "", submitassginmentresponse = "";

    //====================variable for images start================
    private int PICK_FILE_REQUEST = 2;

    //Uri to store the image uri
    Uri uri = null;

    String attachmentFile_Name = "";
    String upload_url = "";

    String path = "";

    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    ArrayList<FileAttachementPOJO> arrayList_FileAttachement;
    ArrayList<String> arrayList_attachment_path;

    //====================variable for images end====================

    ArrayList<String> arrayList_standard_title, arrayList_standard_id, arrayList_subject_title, arrayList_subject_id;
    ArrayAdapter arrayAdapter;

    AttachmentListRVAdapter attachmentListRVAdapter;

    RecyclerView.LayoutManager layoutManager;

    SharedPreferences sp;

    int teacher_id=0;

    AlertDialog ad;
    AlertDialog.Builder builder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(AssignmentViewModel.class);

        binding = FragmentAssignmentBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        sp = getActivity().getSharedPreferences("techerDetail", Context.MODE_PRIVATE);
        teacher_id= Integer.parseInt(sp.getString("teacher_id",""));


        arrayList_attachment_path = new ArrayList<>();
        arrayList_FileAttachement = new ArrayList<>();

        arrayList_standard_id = new ArrayList<>();
        arrayList_standard_title = new ArrayList<>();
        arrayList_subject_id = new ArrayList<>();
        arrayList_subject_title = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        new getStandardListTask().execute();
        new getSubjectListTask().execute();

        binding.tvAssignmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                y = Integer.parseInt(gDateTime.getYear());
                m = Integer.parseInt(gDateTime.getMonth()) - 1;
                d = Integer.parseInt(gDateTime.getDay());

                dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        due_date = df.format(cal.getTime());
                        binding.tvAssignmentDueDate.setText("" + dff.format(cal.getTime()));
                    }
                }, y, m, d);
                dp.show();
            }
        });


        binding.spnStandard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                standard_id = arrayList_standard_id.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spnSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subject_id = arrayList_subject_id.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.tvAssignmentAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AskPermissions(getActivity(), permissionsRequired)) {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, 1);
                } else {
                    showFileChooser();
                }
            }
        });


        binding.btnUploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignment_title=binding.edtAssignmentTitle.getText().toString().trim();
                assignment_description=binding.edtAssignmentDescription.getText().toString().trim();
                assignment_date=gDateTime.getDateymd();

                //Toast.makeText(getActivity(), "title=>"+assignment_title+"\ndescription=>"+assignment_description+"\ndate=>"+assignment_date+"\nteacher_id=>"+teacher_id+"\nstandard_id=>"+standard_id+"\nsubject_id=>"+subject_id+"\nDue date=>"+due_date, Toast.LENGTH_SHORT).show();

                new submitAssignmentTask().execute();

                binding.edtAssignmentTitle.setHint("Title");
                binding.edtAssignmentTitle.setText("");
                binding.edtAssignmentDescription.setHint("Description(Optional)");
                binding.edtAssignmentDescription.setText("");


            }
        });

        return root;
    }




    //====================add attachment code starts==========================================

    public static boolean AskPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //===================choose image from device code start==============
    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("image/*");
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "select multipal images"), PICK_FILE_REQUEST);


    }
    //=====================choose image from device code ends================

    //===================get path code strart=================================

    public String getPath(Uri uri) {

        try {

            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getActivity().getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

        } catch (NullPointerException e) {
            //Toast.makeText(this, "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
        }

        return path;
    }

    public static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //================get path code ends=================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ClipData clipData = data.getClipData();

        if (clipData == null) {

            uri = data.getData();
            upload_url = getFilePath(getActivity(), uri);
            attachmentFile_Name = "" + upload_url.toString().substring(upload_url.toString().lastIndexOf('/') + 1);
            Log.d("attachmentFile_Name =", attachmentFile_Name + "");

            arrayList_FileAttachement.add(new FileAttachementPOJO(attachmentFile_Name, uri));

            attachmentListRVAdapter = new AttachmentListRVAdapter(arrayList_FileAttachement, getActivity());
            binding.rvAttachmentList.setLayoutManager(layoutManager);
            binding.rvAttachmentList.setAdapter(attachmentListRVAdapter);

        } else if (clipData != null) {

            Log.d("clipData.getItemCount", clipData.getItemCount() + "");
            for (int i = 0; i < clipData.getItemCount(); i++) {

                ClipData.Item item = clipData.getItemAt(i);
                uri = item.getUri();
                upload_url = getFilePath(getActivity(), uri);

                attachmentFile_Name = "" + upload_url.toString().substring(upload_url.toString().lastIndexOf('/') + 1);
                //attachmentFile_Name = "" + uri.toString();
                Log.d("attachmentFile_Name =", attachmentFile_Name + "");

                arrayList_FileAttachement.add(new FileAttachementPOJO(attachmentFile_Name, uri));

                attachmentListRVAdapter = new AttachmentListRVAdapter(arrayList_FileAttachement, getActivity());
                binding.rvAttachmentList.setLayoutManager(layoutManager);
                binding.rvAttachmentList.setAdapter(attachmentListRVAdapter);

            }

        }

    }


    //====================add attachment code starts==========================================


    //========================get standard list task starts=================================

    public class getStandardListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            stdurl = getString(R.string.Base_url) + getString(R.string.get_standard_list_url);
            Log.d("standard list url=>", stdurl + "");

            HttpHandler httpHandler = new HttpHandler();
            stdresponse = httpHandler.makeServiceCall(stdurl);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("standard response=>", stdresponse + "");

            getStandardList();

        }

    }


    private void getStandardList() {

        try {
            JSONObject jsonObject = new JSONObject(stdresponse + "");
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                arrayList_standard_id.add(object.getString("id"));
                arrayList_standard_title.add(object.getString("standard_title"));
            }

            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.mytextview, arrayList_standard_title);
            binding.spnStandard.setAdapter(arrayAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //========================get standard list task ends===================================


    //========================get standard list task starts=================================

    public class getSubjectListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            suburl = getString(R.string.Base_url) + getString(R.string.get_subject_list_url);
            Log.d("subject list url=>", suburl + "");

            HttpHandler httpHandler = new HttpHandler();
            subresponse = httpHandler.makeServiceCall(suburl);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("subject response=>", subresponse + "");

            getSubjectList();

        }

    }


    private void getSubjectList() {

        try {
            JSONObject jsonObject = new JSONObject(subresponse + "");
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                arrayList_subject_id.add(object.getString("id"));
                arrayList_subject_title.add(object.getString("subject_title"));
            }

            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.mytextview, arrayList_subject_title);
            binding.spnSubject.setAdapter(arrayAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //========================get standard list task ends===================================


    //==========================submit Assignment task starts==============================

    public class submitAssignmentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            submitassginmenturl=getString(R.string.Base_url)+getString(R.string.add_assignment_url)+due_date+"&title="+assignment_title+"&assignment_description="+assignment_description+"&standard_id="+standard_id+"&subject_id="+subject_id+"&teacher_id="+teacher_id+"&assign_date="+assignment_date;
            Log.d("submit assignment url=>",submitassginmenturl+"");

            HttpHandler httpHandler = new HttpHandler();
            submitassginmentresponse=httpHandler.makeServiceCall(submitassginmenturl);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("submitassign response=>",submitassginmentresponse+"");

            //Toast.makeText(getActivity(), ""+submitassginmentresponse, Toast.LENGTH_SHORT).show();

            showDialog();

            /*arrayList_attachment_path.clear();
            arrayList_FileAttachement.clear();
            attachmentListRVAdapter.notifyDataSetChanged();*/


        }
    }

    //==========================submit Assignment task ends==============================


    public void showDialog(){

        builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon_successfully);
        builder.setMessage("Assignment Added Successfully...");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        ad=builder.create();
        ad.show();

        ad.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

    }



}