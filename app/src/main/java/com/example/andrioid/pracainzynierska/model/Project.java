package com.example.andrioid.pracainzynierska.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Project {
    private String title;
    private String description;
    private int priority;
    private long id;
    private int year, month, day, hour, minute;
    private int eYear, eMonth, eDay, eHour, eMinute;

//    private ArrayList<Task> task  = new ArrayList<Task>();
//    private

    public Project(){
        //pusty konstruktor
    }

    public Project(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public Project(String title, String description, int priority, long id, int year, int month, int day, int hour, int minute,int eYear, int eMonth, int eDay, int eHour, int eMinute) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.eYear = eYear;
        this.eMonth = eMonth;
        this.eDay = eDay;
        this.eHour = eHour;
        this.eMinute = eMinute;
//        this.beginTime = beginTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getYear() {return year;}

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int geteYear() {return eYear;}

    public int geteMonth() {
        return eMonth;
    }

    public int geteDay() {
        return eDay;
    }

    public int geteHour() {
        return eHour;
    }

    public int geteMinute() {
        return eMinute;
    }

//    public ArrayList<Task> getTask() {
//        return task;
//    }
}
