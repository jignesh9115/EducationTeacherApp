package com.ai.educationteacherapp.POJOs;

public class StudentListPOJO {

    String student_name,student_birthdate,student_roll_no,student_photo,student_standard,student_division,student_gnr_no,student_id,standard_id;

    public StudentListPOJO(String student_name, String student_birthdate, String student_roll_no, String student_photo, String student_standard, String student_division, String student_gnr_no, String student_id, String standard_id) {
        this.student_name = student_name;
        this.student_birthdate = student_birthdate;
        this.student_roll_no = student_roll_no;
        this.student_photo = student_photo;
        this.student_standard = student_standard;
        this.student_division = student_division;
        this.student_gnr_no = student_gnr_no;
        this.student_id = student_id;
        this.standard_id = standard_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_birthdate() {
        return student_birthdate;
    }

    public void setStudent_birthdate(String student_birthdate) {
        this.student_birthdate = student_birthdate;
    }

    public String getStudent_roll_no() {
        return student_roll_no;
    }

    public void setStudent_roll_no(String student_roll_no) {
        this.student_roll_no = student_roll_no;
    }

    public String getStudent_photo() {
        return student_photo;
    }

    public void setStudent_photo(String student_photo) {
        this.student_photo = student_photo;
    }

    public String getStudent_standard() {
        return student_standard;
    }

    public void setStudent_standard(String student_standard) {
        this.student_standard = student_standard;
    }

    public String getStudent_division() {
        return student_division;
    }

    public void setStudent_division(String student_division) {
        this.student_division = student_division;
    }

    public String getStudent_gnr_no() {
        return student_gnr_no;
    }

    public void setStudent_gnr_no(String student_gnr_no) {
        this.student_gnr_no = student_gnr_no;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStandard_id() {
        return standard_id;
    }

    public void setStandard_id(String standard_id) {
        this.standard_id = standard_id;
    }
}
