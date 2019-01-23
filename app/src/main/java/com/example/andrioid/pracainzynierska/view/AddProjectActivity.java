package com.example.andrioid.pracainzynierska.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.databinding.ActivityAddProjectBinding;
import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.viewmodel.AddProjectViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.AddProjectViewModelFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import javax.inject.Inject;

public class AddProjectActivity extends BaseActivity {
    private NumberPicker numberPickerPriority;

//    Button btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime;
//    Button btnEndDatePicker, btnEndTimePicker;
    TextView txtEndDate, txtEndTime;
    AddProjectViewModel addProjectViewModel;

    @Inject
    AddProjectViewModelFactory addProjectViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_project);

        getActivityComponent().inject(this);
        addProjectViewModel = ViewModelProviders.of(this, addProjectViewModelFactory).get(AddProjectViewModel.class);

        ActivityAddProjectBinding activityAddProjectBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_project);
        activityAddProjectBinding.setVm(addProjectViewModel);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add project");

//        btnDatePicker=(Button)findViewById(R.id.btn_date);
//        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(TextView)findViewById(R.id.in_date);
        txtTime=(TextView)findViewById(R.id.in_time);

//        btnEndDatePicker=(Button)findViewById(R.id.btn_edate);
//        btnEndTimePicker=(Button)findViewById(R.id.btn_etime);
        txtEndDate=(TextView)findViewById(R.id.in_edate);
        txtEndTime=(TextView)findViewById(R.id.in_etime);

        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(5);
        numberPickerPriority.setValue(addProjectViewModel.priority);

        setBtnClickListeners();
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
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBtnClickListeners(){
        findViewById(R.id.btn_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                if(addProjectViewModel.year == 0) {
                    addProjectViewModel.year = c.get(Calendar.YEAR);
                    addProjectViewModel.month = c.get(Calendar.MONTH);
                    addProjectViewModel.day = c.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProjectActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                addProjectViewModel.year = year;
                                addProjectViewModel.month = monthOfYear + 1;
                                addProjectViewModel.day = dayOfMonth;
                            }
                        }, addProjectViewModel.year, addProjectViewModel.month, addProjectViewModel.day);

                datePickerDialog.show();

            }
        });

        findViewById(R.id.btn_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                if(addProjectViewModel.hour == 0) {
                    addProjectViewModel.hour = c.get(Calendar.HOUR_OF_DAY);
                    addProjectViewModel.minute = c.get(Calendar.MINUTE);
                }

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddProjectActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                                addProjectViewModel.hour = hourOfDay;
                                addProjectViewModel.minute = minute;
                            }
                        }, addProjectViewModel.hour, addProjectViewModel.minute, false);

                timePickerDialog.show();
            }
        });

        findViewById(R.id.btn_edate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addProjectViewModel.eYear == 0) {
                    final Calendar ca = Calendar.getInstance();
                    addProjectViewModel.eYear = ca.get(Calendar.YEAR);
                    addProjectViewModel.eMonth = ca.get(Calendar.MONTH);
                    addProjectViewModel.eDay = ca.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProjectActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                addProjectViewModel.eYear = year;
                                addProjectViewModel.eMonth = monthOfYear + 1;
                                addProjectViewModel.eDay = dayOfMonth;
                            }
                        }, addProjectViewModel.eYear, addProjectViewModel.eMonth, addProjectViewModel.eDay);
                datePickerDialog.show();

            }
        });

        findViewById(R.id.btn_etime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addProjectViewModel.eHour == 0) {
                    final Calendar ca = Calendar.getInstance();
                    addProjectViewModel.eHour = ca.get(Calendar.HOUR_OF_DAY);
                    addProjectViewModel.eMinute = ca.get(Calendar.MINUTE);
                }
                // Get Current Time
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddProjectActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtEndTime.setText(hourOfDay + ":" + minute);
                                addProjectViewModel.eHour = hourOfDay;
                                addProjectViewModel.eMinute = minute;
                            }
                        }, addProjectViewModel.eHour, addProjectViewModel.eMinute, false);
                timePickerDialog.show();
            }
    });


//        btnIntent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar beginTime = Calendar.getInstance();
//                beginTime.set(2012, 0, 19, 7, 30);
//                Calendar endTime = Calendar.getInstance();
//                endTime.set(2012, 0, 19, 8, 30);
//                Intent intent = new Intent(Intent.ACTION_INSERT)
//                        .setData(CalendarContract.Events.CONTENT_URI)
//                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//                        .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                        .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
////                        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//                startActivity(intent);
//            }
//        });
    }

    private void saveNote() {
        addProjectViewModel.priority = numberPickerPriority.getValue();
        addProjectViewModel.saveNote();
    }

}
