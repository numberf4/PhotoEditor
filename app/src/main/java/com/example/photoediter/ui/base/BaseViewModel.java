package com.example.photoediter.ui.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
