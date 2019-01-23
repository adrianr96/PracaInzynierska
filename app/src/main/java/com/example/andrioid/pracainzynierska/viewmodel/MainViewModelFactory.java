package com.example.andrioid.pracainzynierska.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.andrioid.pracainzynierska.model.MainRepository;

import javax.inject.Inject;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    Context context;

    public Context getContext() {
        return context;
    }

    @Inject
    MainRepository mainRepository;

    public MainRepository getMainRepository() {
        return mainRepository;
    }

    @Inject
    public MainViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(this);
    }

}
