package com.example.andrioid.pracainzynierska.dependencyInjection.modules;

import android.app.Activity;
import android.content.Context;

import com.example.andrioid.pracainzynierska.dependencyInjection.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseActivityModule {
    private final Activity context;

    public BaseActivityModule(Activity context) {
        this.context = context;
    }

    @Provides
    @ActivityScope
    public Context context() {
        return context;
    }

    @Provides
    @ActivityScope
    public Activity activity() {
        return context;
    }
}
