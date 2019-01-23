package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.model.User;
import com.example.andrioid.pracainzynierska.model.UserItem;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.andrioid.pracainzynierska.view.TaskListActivity.PROJECT_PATH;

public class AddUserActivity extends BaseActivity {

    private String projectPath;
    private EditText getUser;
    private ImageView imageViewAdd;
    private ImageView imageView;
    String userNumber;

    private ArrayList<UserItem> mUserList;
    private UserAdapter mAdapter;

    TaskViewModel taskViewModel;

    @Inject
    TaskViewModelFactory taskViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        getActivityComponent().inject(this);
        taskViewModel = ViewModelProviders.of(this, taskViewModelFactory).get(TaskViewModel.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            projectPath = bundle.getString(PROJECT_PATH);
        }

        getUser = findViewById(R.id.edit_text_add_user);
        imageViewAdd = findViewById(R.id.iv_add_user);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskViewModel.addUserActivity(getUser.getText().toString(), projectPath);
            }
        });

        imageView = findViewById(R.id.iv_call);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskViewModel.call(userNumber);
            }
        });

        initList();

        Spinner spinnerCountries = findViewById(R.id.spinner_countries);

        mAdapter = new UserAdapter(this, mUserList);
        spinnerCountries.setAdapter(mAdapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserItem clickedItem = (UserItem) parent.getItemAtPosition(position);
                String clickedUserName = clickedItem.getUserName();
                String clickedUserNumber = clickedItem.getUserNumber();
                getUser.setText(clickedUserName);
                userNumber = clickedUserNumber;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initList() {
        ArrayList<String> names = taskViewModel.getOnlyNameDetails();
        ArrayList<String> numbers = taskViewModel.getPhoneEmailDetails();
        ArrayList<String> email = taskViewModel.getNameEmailDetails();
        mUserList = new ArrayList<>();


            for (int i = 0; i < names.size(); i++) {
                mUserList.add(new UserItem(names.get(i).toString(), "123123123", email.get(i).toString()));
            }

//        mUserList.add(new UserItem();
    }



}
