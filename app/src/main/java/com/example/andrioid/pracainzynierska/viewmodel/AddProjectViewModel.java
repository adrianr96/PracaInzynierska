package com.example.andrioid.pracainzynierska.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.model.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProjectViewModel extends ViewModel {
    AddProjectViewModelFactory addProjectViewModelFactory;
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public int year, month, day, hour, minute;
    public int eYear, eMonth, eDay, eHour, eMinute;
    public int priority;

    public AddProjectViewModel(AddProjectViewModelFactory addProjectViewModelFactory) {
        this.addProjectViewModelFactory = addProjectViewModelFactory;
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableField<String> getDescription() {
        return description;
    }

    public void saveNote() {
        long id = System.currentTimeMillis();
        if(this.title.get() == null || this.description.get() == null){
            Toast.makeText(addProjectViewModelFactory.context, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = this.title.get();
        String description = this.description.get();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(addProjectViewModelFactory.context, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        final Project project = new Project(title, description, priority, id, year, month, day, hour, minute, eYear, eMonth, eDay, eHour, eMinute);

        CollectionReference listProjectRef = FirebaseFirestore.getInstance()
                .collection("ProjectList");

        listProjectRef.add(project).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                addProjectToUser(project);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addProjectViewModelFactory.activity, "onFailure " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addProjectToUser(Project project) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("User").document(user.getEmail())
                .collection("ProjectList");

        collectionReference.add(project).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(addProjectViewModelFactory.activity, "Projekt został dodany", Toast.LENGTH_SHORT).show();
                addProjectViewModelFactory.activity.finish();
            }
        });
    }

}
