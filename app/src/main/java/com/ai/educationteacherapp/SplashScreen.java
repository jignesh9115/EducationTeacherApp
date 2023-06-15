package com.ai.educationteacherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ai.educationteacherapp.POJOs.TeacherLoginPOJO;
import com.ai.educationteacherapp.databinding.ActivitySplashScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    private static final int DIALOG_ERROR_CONNECTION = 1;

    String url = "", response = "", username = "", password = "", enterusername = "", enterpassword = "";

    ArrayList<TeacherLoginPOJO> teacherLoginPOJOArrayList;
    TeacherLoginPOJO teacherLoginPOJO;


    SharedPreferences sp;

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("techerDetail", Context.MODE_PRIVATE);

        teacherLoginPOJOArrayList = new ArrayList<>();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        animation.setDuration(2000);

        binding.imgLogo.startAnimation(animation);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //=======================SET TITLE BAR CODE START======================

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        //=======================SET TITLE BAR CODE END========================


        if (!isOnline(SplashScreen.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); // displaying the created
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {


                    if (sp.getString("username", "").length() > 0 && sp.getString("password", "").length() > 0) {

                        enterusername = sp.getString("username", "").trim();
                        enterpassword = sp.getString("password", "").trim();
                        new getusernamepassTask().execute();

                    } else {

                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }


                }
            }, 5000);
        }
    }


    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        // check ni otherwise if connection not there it will thrown an exception..

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DIALOG_ERROR_CONNECTION:
                Log.d("inside net error", " " + DIALOG_ERROR_CONNECTION);
                return new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                        .setIcon(R.drawable.warning)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage("No internet connection.")
                        .setNegativeButton("ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                })

                        .setOnKeyListener(new Dialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface arg0, int keyCode,
                                                 KeyEvent event) {
                                // TODO Auto-generated method stub
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    finish();
                                }
                                return true;
                            }
                        }).create();

            default:
                return null;
        }


    }


    //============================get username and password Task start==========================

    public class getusernamepassTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            url = getString(R.string.Base_url) + getString(R.string.teacher_login_url) + enterusername + "&password=" + enterpassword;
            HttpHandler httpHandler = new HttpHandler();
            response = httpHandler.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(LoginScreen.this, "response==>" + response, Toast.LENGTH_SHORT).show();
            getdata();
        }
    }

    public void getdata() {
        try {
            JSONObject jsonObject = new JSONObject("" + response);

            if (jsonObject.getString("data").equalsIgnoreCase("false")) {

                Toast.makeText(this, "Your Account is Deactive", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);

            } else {

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

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();


            }

        } catch (JSONException e) {
            //Toast.makeText(this, "JSON ERROR=>" + e, Toast.LENGTH_SHORT).show();
        }

    }

    //============================get username and password Task end============================

}