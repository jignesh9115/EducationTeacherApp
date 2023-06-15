package com.ai.educationteacherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.educationteacherapp.POJOs.TeacherLoginPOJO;
import com.ai.educationteacherapp.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    String url = "", response = "", username = "", password = "", enterusername = "", enterpassword = "";

    ArrayList<TeacherLoginPOJO> teacherLoginPOJOArrayList;
    TeacherLoginPOJO teacherLoginPOJO;

    ProgressDialog pdialog;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("techerDetail", Context.MODE_PRIVATE);

        teacherLoginPOJOArrayList = new ArrayList<>();


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtUsername.getText().toString().length() > 0 || binding.edtPassword.getText().toString().length() > 0) {

                    enterusername = binding.edtUsername.getText().toString().trim();
                    enterpassword = binding.edtPassword.getText().toString().trim();
                    new getusernamepassTask().execute();

                } else {
                    Toast.makeText(LoginActivity.this, "enter username & password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//============================get username and password Task start==========================

    public class getusernamepassTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdialog = new ProgressDialog(LoginActivity.this);
            pdialog.setMessage("please wait...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            url = getString(R.string.Base_url) + getString(R.string.teacher_login_url) + enterusername + "&password=" + enterpassword;
            Log.d("Login url=>", url + "");

            HttpHandler httpHandler = new HttpHandler();
            response = httpHandler.makeServiceCall(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(LoginActivity.this, "response=>" + response, Toast.LENGTH_SHORT).show();

            Log.d("response=>", response + "");

            getdata();

            if (pdialog.isShowing()) {

                pdialog.dismiss();

            }
        }
    }

    public void getdata() {

        try {
            JSONObject jsonObject = new JSONObject(response + "");

            if (jsonObject.getString("data").equalsIgnoreCase("false")) {

                Toast.makeText(this, "Invalid Username & Password", Toast.LENGTH_LONG).show();

            }else{

                JSONObject object = jsonObject.getJSONObject("data");

                teacherLoginPOJO = new TeacherLoginPOJO(object.getString("id"),
                        object.getString("name"),
                        object.getString("user_name"),
                        object.getString("password"));

                teacherLoginPOJOArrayList.add(teacherLoginPOJO);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("teacher_id", teacherLoginPOJOArrayList.get(0).getId());
                editor.putString("name", teacherLoginPOJOArrayList.get(0).getName());
                editor.putString("username", teacherLoginPOJOArrayList.get(0).getUser_name());
                editor.putString("password", teacherLoginPOJOArrayList.get(0).getPassword());
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //============================get username and password Task end============================

}