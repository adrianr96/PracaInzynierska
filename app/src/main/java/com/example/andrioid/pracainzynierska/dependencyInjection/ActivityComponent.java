package com.example.andrioid.pracainzynierska.dependencyInjection;

import com.example.andrioid.pracainzynierska.dependencyInjection.modules.BaseActivityModule;
import com.example.andrioid.pracainzynierska.dependencyInjection.scope.ActivityScope;
import com.example.andrioid.pracainzynierska.view.AboutFragment;
import com.example.andrioid.pracainzynierska.view.AddProjectActivity;
import com.example.andrioid.pracainzynierska.view.AddTaskActivity;
import com.example.andrioid.pracainzynierska.view.AddUserActivity;
import com.example.andrioid.pracainzynierska.view.KontoActivity;
import com.example.andrioid.pracainzynierska.view.MainActivity;
import com.example.andrioid.pracainzynierska.view.MainFragment;
import com.example.andrioid.pracainzynierska.view.SettingsFragment;
import com.example.andrioid.pracainzynierska.view.TaskListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {BaseActivityModule.class}, dependencies = {ApplicationComponent.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(AboutFragment aboutFragment);
    void inject(AddTaskActivity addTaskActivity);
    void inject(AddUserActivity addUserActivity);
    void inject(KontoActivity kontoActivity);
    void inject(MainFragment mainFragment);
    void inject(SettingsFragment settingsFragment);
    void inject(TaskListActivity taskListActivity);
    void inject(AddProjectActivity addProjectActivity);
}
