package com.example.andrioid.pracainzynierska.model;

public class Task {
    private String title;
    private String description;
    private int priority;
//    private

    public Task(){
        //pusty konstruktor
    }

    public Task(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
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
}
