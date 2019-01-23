package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModelFactory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {

    private ProjectAdapter adapter;

    private static final int REQUEST_CODE = 1;
    SheetLayout mSheetLayout;
    FloatingActionButton mFab;

    MainViewModel viewMode;

    @Inject
    MainViewModelFactory mainViewModelFactory;

    @Inject
    public MainFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDagger();
        viewMode = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
    }

    private void setupDagger() {
        ((MainActivity)getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setupView(view);

        setUpRecyclerView(view);


        return view;
    }

    private void setupView(View view) {
        mSheetLayout = view.findViewById(R.id.bottom_sheet);
        mFab = view.findViewById(R.id.button_add_project);
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });

    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), AddProjectActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
            getActivity().overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down );
        }
    }


    private void setUpRecyclerView(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return;

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("User").document(user.getEmail())
                .collection("ProjectList");
        Query query = collectionReference.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class)
                .build();
        adapter = new ProjectAdapter(options, getActivity());

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_projects);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                deleteInProjectList(adapter.getItem(viewHolder.getAdapterPosition()));
                deleteProjectFromAllUsers(adapter.getItem(viewHolder.getAdapterPosition()));
                
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
            }
        });
    }

    private void deleteProjectFromAllUsers(final Project myPro) {
        viewMode.deleteProjectFromAllUsers(myPro);
    }

    private void deleteInProjectList(final Project myPro) {
        viewMode.deleteInProjectList(myPro);
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
