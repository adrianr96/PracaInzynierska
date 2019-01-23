package com.example.andrioid.pracainzynierska.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class TaskViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    Context context;

    @Inject
    Activity activity;

    @Inject
    public TaskViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskViewModel(this);
    }

}
