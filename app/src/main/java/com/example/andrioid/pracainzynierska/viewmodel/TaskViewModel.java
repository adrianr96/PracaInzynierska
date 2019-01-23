package com.example.andrioid.pracainzynierska.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.model.User;
import com.example.andrioid.pracainzynierska.view.AddTaskActivity;
import com.example.andrioid.pracainzynierska.view.AddUserActivity;
import com.example.andrioid.pracainzynierska.view.TaskListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.andrioid.pracainzynierska.view.TaskListActivity.PROJECT_PATH;
import static com.example.andrioid.pracainzynierska.view.TaskListActivity.TASKS_COLLECTION;

public class TaskViewModel extends ViewModel {
    TaskViewModelFactory addTaskViewModelFactory;

    public TaskViewModel(TaskViewModelFactory addTaskViewModelFactory) {
        this.addTaskViewModelFactory = addTaskViewModelFactory;
    }

    public void addUserActivity(String eMail, final String projectPath) {
        final User user = new User();
        final String mail = eMail;
        user.setEmail(mail);
        FirebaseFirestore.getInstance().collection("User").document(user.getEmail()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseFirestore.getInstance().document(projectPath).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Project project = documentSnapshot.toObject(Project.class);

                                FirebaseFirestore.getInstance().collection("User")
                                        .document(mail)
                                        .collection("ProjectList")
                                        .document(documentSnapshot.getReference().getId())
                                        .set(project).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(addTaskViewModelFactory.activity, "onSuccess", Toast.LENGTH_SHORT).show();
                                        addTaskViewModelFactory.activity.finish();
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void saveNoteAndClose(String title, String description, int priority, String projectPath) {
       if(!(addTaskViewModelFactory.activity instanceof AddTaskActivity))
           return;

        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(addTaskViewModelFactory.activity, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference collectionReference = FirebaseFirestore.getInstance().document(projectPath).collection(TASKS_COLLECTION);
        collectionReference
                .add(new com.example.andrioid.pracainzynierska.model.Task(title, description, priority))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(addTaskViewModelFactory.activity, "onSuccess", Toast.LENGTH_SHORT).show();
                        addTaskViewModelFactory.activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addTaskViewModelFactory.activity, "onFailure "  + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goAddTaskActivity(Activity activity, String projectPath) {
        Intent intent = new Intent(activity, AddTaskActivity.class);
        intent.putExtra(PROJECT_PATH, projectPath);
       // setProjectPath(projectPath);
        activity.startActivity(intent);
    }

    public void goAddUserActivity(Activity activity, String projectPath) {
        Intent intent = new Intent(activity, AddUserActivity.class);
        intent.putExtra(PROJECT_PATH, projectPath);
       // setProjectPath(projectPath);
        activity.startActivity(intent);
    }

    public ArrayList<String> getNameEmailDetails(){
        ArrayList<String> emails = new ArrayList<String>();
        ContentResolver cr = addTaskViewModelFactory.activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        emails.add(email);
                    }
                }
                cur1.close();
            }
        }
        return emails;
    }

    public ArrayList<String> getPhoneEmailDetails(){
        ArrayList<String> phoneNumbers = new ArrayList<String>();
        ContentResolver cr = addTaskViewModelFactory.activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String phoneNumber=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        phoneNumbers.add(phoneNumber);
                    }
                }
                cur1.close();
            }
        }
        return phoneNumbers;
    }

    public ArrayList<String> getOnlyNameDetails(){
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = addTaskViewModelFactory.activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        names.add(name);
                    }
                }
                cur1.close();
            }
        }
        return names;
    }

    public void call(String userNumber){
        String phone = userNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        addTaskViewModelFactory.activity.startActivity(intent);
    }

}
