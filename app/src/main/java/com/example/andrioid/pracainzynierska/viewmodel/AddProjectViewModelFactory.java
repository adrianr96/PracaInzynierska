package com.example.andrioid.pracainzynierska.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class AddProjectViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    Context context;

    @Inject
    Activity activity;

    @Inject
    public AddProjectViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new AddProjectViewModel(this);
    }

    public Context getContext() {
        return context;
    }
}
