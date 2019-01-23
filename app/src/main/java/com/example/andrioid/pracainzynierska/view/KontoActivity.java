package com.example.andrioid.pracainzynierska.view;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andrioid.pracainzynierska.R;

public class KontoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konto);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
}
