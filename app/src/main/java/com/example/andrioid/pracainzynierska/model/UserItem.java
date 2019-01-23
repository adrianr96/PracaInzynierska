package com.example.andrioid.pracainzynierska.model;

public class UserItem {
    private String mUserName;
    private String mUserNumber;
    private String mUserEmail;
    private int mFlagImage;

    public UserItem(String userName, String userNumber, String userEmail){
        mUserEmail = userEmail;
        mUserName = userName;
        mUserNumber = userNumber;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserNumber() {
        return mUserNumber;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public int getFlagImage() {
        return mFlagImage;
    }
}
