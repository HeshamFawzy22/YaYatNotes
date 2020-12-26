package com.example.yatnotes.ui.courses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CoursesViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public CoursesViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoursesViewModel(application);
    }
}
