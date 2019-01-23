package com.example.andrioid.pracainzynierska.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrioid.pracainzynierska.R;

import javax.inject.Inject;


public class AboutFragment extends Fragment {

    @Inject
    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDagger();
    }

    private void setupDagger() {
        ((MainActivity)getActivity()).getActivityComponent().inject(this);
    }

}