package com.example.andrioid.pracainzynierska.dependencyInjection;

import com.example.andrioid.pracainzynierska.dependencyInjection.modules.ContextModule;
import com.example.andrioid.pracainzynierska.dependencyInjection.scope.ApplicationScope;
import com.example.andrioid.pracainzynierska.view.BaseApplication;

import dagger.Component;

@ApplicationScope
@Component(modules = {ContextModule.class})
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
}
