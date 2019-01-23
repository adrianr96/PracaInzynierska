package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModelFactory;

import javax.inject.Inject;

import static com.example.andrioid.pracainzynierska.view.TaskListActivity.PROJECT_PATH;


public class AddTaskActivity extends BaseActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private String projectPath;

    TaskViewModel taskViewModel;

    @Inject
    TaskViewModelFactory taskViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getActivityComponent().inject(this);
        taskViewModel = ViewModelProviders.of(this, taskViewModelFactory).get(TaskViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            projectPath = bundle.getString(PROJECT_PATH);
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add task");

        editTextTitle = findViewById(R.id.edit_text_title_task);
        editTextDescription = findViewById(R.id.edit_text_description_task);
        numberPickerPriority = findViewById(R.id.number_picker_priority_task);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_project_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote(editTextTitle.getText().toString(), editTextDescription.getText().toString(), numberPickerPriority.getValue());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote(String title, String description, int priority) {
        taskViewModel.saveNoteAndClose(title, description ,priority, projectPath);
    }
}

