package com.ai.educationteacherapp.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceListPOJO {

    @SerializedName("student_id")
    @Expose
    private  String attend_student_id;

    @SerializedName("status")
    @Expose
    private String attend_status;

    public AttendanceListPOJO(String attend_student_id, String attend_status) {
        this.attend_student_id = attend_student_id;
        this.attend_status = attend_status;
    }

    public String getAttend_student_id() {
        return attend_student_id;
    }

    public void setAttend_student_id(String attend_student_id) {
        this.attend_student_id = attend_student_id;
    }

    public String getAttend_status() {
        return attend_status;
    }

    public void setAttend_status(String attend_status) {
        this.attend_status = attend_status;
    }
}
