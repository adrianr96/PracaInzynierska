package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText getName;
    private EditText getEmail;
    private EditText getPassword;

    MainViewModel viewMode;

    @Inject
    MainViewModelFactory mainViewModelFactory;

    @Inject
    public SettingsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDagger();
        viewMode = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
    }

    private void setupDagger() {
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button a = (Button) view.findViewById(R.id.save_name);
        a.setOnClickListener(this);
        Button b = (Button) view.findViewById(R.id.save_email);
        b.setOnClickListener(this);
        Button c = (Button) view.findViewById(R.id.delete_user);
        c.setOnClickListener(this);
        Button d = (Button) view.findViewById(R.id.update_password);
        d.setOnClickListener(this);

        getName = view.findViewById(R.id.edit_text_title);
        getEmail = view.findViewById(R.id.edit_text_description);
        getPassword = view.findViewById(R.id.edit_text_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_name:
                updateProfile();
                break;
            case R.id.save_email:
                updateEmail();
                break;
            case R.id.delete_user:
                deleteUser();
                break;
            case R.id.update_password:
                updatePassword();
                break;
        }
    }

    public void updateProfile() {
        viewMode.updateProfile(getName.getText().toString());
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.makeHeaderView();
    }

    public void updateEmail() {
        viewMode.updateEmail(getEmail.getText().toString());
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.makeHeaderView();
    }

    public void deleteUser() {
        viewMode.deleteUser();
    }

    public void updatePassword() {
        viewMode.updatePassword(getPassword.getText().toString());
    }
}