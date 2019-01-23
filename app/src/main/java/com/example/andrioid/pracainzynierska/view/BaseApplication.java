package com.example.andrioid.pracainzynierska.view;

import android.app.Activity;
import android.app.Application;

import com.example.andrioid.pracainzynierska.dependencyInjection.ApplicationComponent;
import com.example.andrioid.pracainzynierska.dependencyInjection.DaggerApplicationComponent;
import com.example.andrioid.pracainzynierska.dependencyInjection.modules.ContextModule;

public class BaseApplication extends Application {
    private ApplicationComponent applicationComponent;

    public static BaseApplication get(Activity activity) {
        return (BaseApplication) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void createComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    private void setupInject() {
        applicationComponent.inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createComponent();
        setupInject();
    }

}
