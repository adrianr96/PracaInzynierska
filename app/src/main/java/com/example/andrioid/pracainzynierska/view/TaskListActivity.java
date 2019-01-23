package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.model.Task;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.TaskViewModelFactory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

public class TaskListActivity extends BaseActivity {

    public static String PROJECT_PATH = "PROJECT_PATH";
    public static String TASKS_COLLECTION = "tasks";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TaskAdapter adapter;
    private String projectPath;
    private FloatingActionMenu menuRed;

    TaskViewModel taskViewModel;

    @Inject
    TaskViewModelFactory taskViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        getActivityComponent().inject(this);
        taskViewModel = ViewModelProviders.of(this, taskViewModelFactory).get(TaskViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            projectPath = bundle.getString(PROJECT_PATH);
        }

        setUpRecyclerView();
        makeFabs();
    }

    private void setUpRecyclerView() {
        CollectionReference tasksRef = db.document(projectPath).collection(TASKS_COLLECTION);
        Query query = tasksRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        adapter = new TaskAdapter(options);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_projects_task);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void makeFabs() {
        menuRed = (FloatingActionMenu) findViewById(R.id.fab_menu_red);
        final com.github.clans.fab.FloatingActionButton fab = new com.github.clans.fab.FloatingActionButton(getApplicationContext());
        final com.github.clans.fab.FloatingActionButton fab1 = new com.github.clans.fab.FloatingActionButton(getApplicationContext());
        fab.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_MINI);
        fab.setImageResource(R.drawable.ic_add);
        fab.setLabelText("Add task");
        fab1.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_MINI);
        fab1.setImageResource(R.drawable.ic_person_add);
        fab1.setLabelText("Add user");

        menuRed.addMenuButton(fab1);
        menuRed.addMenuButton(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewModel.goAddTaskActivity(TaskListActivity.this, projectPath);

            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewModel.goAddUserActivity(TaskListActivity.this, projectPath);
            }
        });
    }
}
