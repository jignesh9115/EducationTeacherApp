package com.ai.educationteacherapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GDateTime {

    private Calendar cl;

    private int iyear, imonth, iday;
    private int ihr, imin;

    private SimpleDateFormat dateFormatterdmy, dateFormatterymd, timeFormatter12, timeFormatter24;

    private String datedmy, dateymd, day, month, week, year, time12, time24;

    private String month_name;


    public GDateTime() {
        // TODO Auto-generated constructor stub

        cl = Calendar.getInstance();
        dateFormatterymd = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatterdmy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter12 = new SimpleDateFormat("hh:mm:ss", Locale.US);
        timeFormatter24 = new SimpleDateFormat("HH:mm:ss", Locale.US);

        cl = Calendar.getInstance();
        iyear = cl.get(Calendar.YEAR);
        imonth = cl.get(Calendar.MONTH);
        iday = cl.get(Calendar.DAY_OF_MONTH);

        ihr = cl.get(Calendar.HOUR);
        imin = cl.get(Calendar.MINUTE);

        cl.set(iyear, imonth, iday);
        //cl.set(Calendar.HOUR_OF_DAY, 24);
        dateFormatterymd.format(cl.getTime());
        dateFormatterdmy.format(cl.getTime());
        // System.out.println(dateFormatterymd.format(cl.getTime()));
        //System.out.println(timeFormatter24.format(cl.getTime()));
        //System.out.println("date="+dateFormatterymd.format(cl.getTime()));
        //System.out.println("day="+cl.get(Calendar.DAY_OF_WEEK));
        //System.out.println("month="+((cl.get(Calendar.MONTH))+1));
        //System.out.println("week="+cl.get(Calendar.WEEK_OF_YEAR));
        //System.out.println("year="+cl.get(Calendar.YEAR));
        //System.out.println("time="+timeFormatter24.format(cl.getTime()));

        datedmy = dateFormatterdmy.format(cl.getTime()) + "";
        dateymd = dateFormatterymd.format(cl.getTime()) + "";
        day = cl.get(Calendar.DAY_OF_MONTH) + "";
        month = ((cl.get(Calendar.MONTH)) + 1) + "";
        week = cl.get(Calendar.WEEK_OF_YEAR) + "";
        year = cl.get(Calendar.YEAR) + "";
        time12 = timeFormatter12.format(cl.getTime()) + "";
        time24 = timeFormatter24.format(cl.getTime()) + "";
    }


    public String getDatedmy() {
        return datedmy;
    }


    public void setDatedmy(String datedmy) {
        this.datedmy = datedmy;
    }


    public String getDateymd() {
        return dateymd;
    }


    public void setDateymd(String dateymd) {
        this.dateymd = dateymd;
    }


    public String getDay() {
        return day;
    }


    public void setDay(String day) {
        this.day = day;
    }


    public String getMonth() {
        return month;
    }


    public void setMonth(String month) {
        this.month = month;
    }


    public String getWeek() {
        return week;
    }


    public void setWeek(String week) {
        this.week = week;
    }


    public String getYear() {
        return year;
    }


    public void setYear(String year) {
        this.year = year;
    }


    public String getTime12() {
        return time12;
    }


    public void setTime12(String time12) {
        this.time12 = time12;
    }


    public String getTime24() {
        return time24;
    }


    public void setTime24(String time24) {
        this.time24 = time24;
    }


    public String dmyToymd(String sdate) {

        Date date;
        try {
            date = dateFormatterdmy.parse("" + sdate.trim());
            return dateFormatterymd.format(date);

        } catch (Exception ex) {
            // Handle Exception.
        }
        return null;
    }

    public String ymdTodmy(String sdate) {

        Date date;
        try {
            date = dateFormatterymd.parse("" + sdate.trim());
            return dateFormatterdmy.format(date);

        } catch (Exception ex) {
            // Handle Exception.
        }
        return null;
    }


    public String getMonth_name(int month_number) {
        //String monthString = new DateFormatSymbols().getMonths()[month_number];
        //return  monthString;

        String monthString;
        switch (month_number) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }

        return monthString;
    }


    public String getDay_name(String sdate) {
        Date date = null;
        try {
            date = dateFormatterymd.parse(sdate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_number = c.get(Calendar.DAY_OF_WEEK);

        String dayOfWeek;
        switch (day_number-1) {

            case 1:
                dayOfWeek = "Mon";
                break;
            case 2:
                dayOfWeek = "Tue";
                break;
            case 3:
                dayOfWeek = "Wed";
                break;
            case 4:
                dayOfWeek = "Thu";
                break;
            case 5:
                dayOfWeek = "Fri";
                break;
            case 6:
                dayOfWeek = "Sat";
                break;
            case 7:
                dayOfWeek = "Sun";
                break;
            default:
                dayOfWeek = "Invalid day";
                break;
        }

        return dayOfWeek;
    }


    public int getMonth_number(String month_name) {
        int monthString=0;
        switch (month_name) {
            case "January":
                monthString = 1;
                break;
            case "February":
                monthString = 2;
                break;
            case "March":
                monthString = 3;
                break;
            case "April":
                monthString = 4;
                break;
            case "May":
                monthString = 5;
                break;
            case "June":
                monthString = 6;
                break;
            case "July":
                monthString = 7;
                break;
            case "August":
                monthString = 8;
                break;
            case "September":
                monthString = 9;
                break;
            case  "October":
                monthString = 10;
                break;
            case "November":
                monthString = 11;
                break;
            case "December":
                monthString = 12;
                break;
        }

        return monthString;
    }

}
