package com.example.andrioid.pracainzynierska.model;

import android.content.Context;

import com.example.andrioid.pracainzynierska.R;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

public class MainRepository {

    private ArrayList<String> mMenuTitles;

    @Inject
    Context context;

    @Inject
    public MainRepository() {
    }

    public ArrayList<String> getMenuTitles() {
        if(mMenuTitles == null)
            mMenuTitles = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.menuOptions)));

        return mMenuTitles;
    }

}
