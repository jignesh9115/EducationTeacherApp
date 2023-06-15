package com.ai.educationteacherapp.POJOs;

public class TimeTablePOJO {

    String subject_title,standard_title,start_time,end_time,day_id,standard_id,subject_id;

    public TimeTablePOJO(String subject_title, String standard_title, String start_time, String end_time, String day_id, String standard_id, String subject_id) {
        this.subject_title = subject_title;
        this.standard_title = standard_title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.day_id = day_id;
        this.standard_id = standard_id;
        this.subject_id = subject_id;
    }

    public String getSubject_title() {
        return subject_title;
    }

    public void setSubject_title(String subject_title) {
        this.subject_title = subject_title;
    }

    public String getStandard_title() {
        return standard_title;
    }

    public void setStandard_title(String standard_title) {
        this.standard_title = standard_title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDay_id() {
        return day_id;
    }

    public void setDay_id(String day_id) {
        this.day_id = day_id;
    }

    public String getStandard_id() {
        return standard_id;
    }

    public void setStandard_id(String standard_id) {
        this.standard_id = standard_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
