package com.example.andrioid.pracainzynierska.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    MainViewModelFactory mainViewModelFactory;

    private ArrayList<String> mMenuTitles;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userEmail;
    private String userDisplayName;

    private String path;

    public MainViewModel(MainViewModelFactory mainViewModelFactory) {
        this.mainViewModelFactory = mainViewModelFactory;
    }

    public String getUserEmail() {
        if(userEmail == null)
            userEmail = getUser().getEmail();

        return userEmail;
    }

    public String getUserDisplayName() {
        if(userDisplayName == null)
            if(getUser().getDisplayName() == null) {
                return "";
            }
        userDisplayName = getUser().getDisplayName();

        return userDisplayName;
    }

    public ArrayList<String> getMenuTitles() {
        if(mMenuTitles == null)
            mMenuTitles = mainViewModelFactory.mainRepository.getMenuTitles();

        return mMenuTitles;
    }

    public FirebaseUser getUser() {
        if(firebaseUser == null)
            firebaseUser = getFirebaseAuth().getCurrentUser();

        return firebaseUser;
    }

    public FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        return firebaseAuth;
    }

    private FirebaseFirestore getFirebaseFirestor() {
        if(firebaseFirestore == null)
            firebaseFirestore = FirebaseFirestore.getInstance();

        return firebaseFirestore;
    }

    public void addUser() {
        FirebaseUser user = getFirebaseAuth().getCurrentUser();
        if(user == null)
            return;
        String name = user.getDisplayName();
        String email = user.getEmail();
        String id = user.getUid();
        User account = new User(name, email, id);

        getFirebaseFirestor().collection("User").document(user.getEmail()).set(account)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mainViewModelFactory.context, "Dodalo", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mainViewModelFactory.context, "Nie dodalo", Toast.LENGTH_SHORT);

                    }
                });
    }

    public void updateProfile(String changeNameA) {
        FirebaseUser user = getFirebaseAuth().getCurrentUser();
        String changeName = changeNameA;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(changeName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mainViewModelFactory.context, "User profile updated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateEmail(String changeEmailA) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String changeEmail = changeEmailA;
        user.updateEmail(changeEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mainViewModelFactory.context, "User email address updated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainViewModelFactory.context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mainViewModelFactory.context, "User account deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainViewModelFactory.context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePassword(String newPasswordA) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = newPasswordA;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mainViewModelFactory.context, "User password updated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainViewModelFactory.context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteProjectFromAllUsers(final Project myPro) {
        FirebaseFirestore.getInstance().collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        for(int i=0; i<list.size(); i++) {
                            String email = list.get(i);
                            FirebaseFirestore.getInstance().collection("User").document(email).collection("ProjectList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Project> projectList = queryDocumentSnapshots.toObjects(Project.class);
                                    for (Project projectOb : projectList) {
                                        if (projectOb.getId() == myPro.getId()) {
                                            int itemPositon = projectList.indexOf(projectOb);
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(itemPositon);
                                            FirebaseFirestore.getInstance().document(documentSnapshot.getReference().getPath()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                        }
                                    }

                                }
                            });
                        }
                    }}
            }
        });
    }

    public void deleteInProjectList(final Project myPro) {
        FirebaseFirestore.getInstance().collection("ProjectList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Project> projectList = queryDocumentSnapshots.toObjects(Project.class);
                for (Project projectOb : projectList) {
                    if (projectOb.getId() == myPro.getId()) {
                        int itemPositon = projectList.indexOf(projectOb);
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(itemPositon);
                        FirebaseFirestore.getInstance().document(documentSnapshot.getReference().getPath()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                }

            }
        });
    }

}
