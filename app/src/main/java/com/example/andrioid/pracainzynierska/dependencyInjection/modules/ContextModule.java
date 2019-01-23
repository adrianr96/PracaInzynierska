package com.example.andrioid.pracainzynierska.dependencyInjection.modules;

import android.content.Context;

import com.example.andrioid.pracainzynierska.dependencyInjection.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public Context context() {
        return context;
    }
}
