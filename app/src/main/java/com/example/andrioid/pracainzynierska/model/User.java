package com.example.andrioid.pracainzynierska.model;

public class User {
    private String name;
    private String email;
    private String id;


    public User(){
        //empty constructor
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;

    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getId(){
        return id;
    }

}
