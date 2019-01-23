package com.example.andrioid.pracainzynierska.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.andrioid.pracainzynierska.dependencyInjection.ActivityComponent;
import com.example.andrioid.pracainzynierska.dependencyInjection.DaggerActivityComponent;
import com.example.andrioid.pracainzynierska.dependencyInjection.modules.BaseActivityModule;

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInject();
    }

    private void setupInject() {
        activityComponent = DaggerActivityComponent.builder()
                .baseActivityModule(new BaseActivityModule(this))
                .applicationComponent(BaseApplication.get(this).getApplicationComponent())
                .build();
    }
}
