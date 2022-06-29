package com.example.photoediter.ui.main.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.photoediter.common.models.LiveEvent;
import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;
import com.example.photoediter.utils.FilterOption;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class FilterViewModel extends BaseViewModel {
    PhotoRepository photoRepository;
    public LiveEvent<FilterOption> liveEvent = new LiveEvent<>();

    @Inject
    public FilterViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getListFilter(Context context, Bitmap bitmap) {
        photoRepository.getListFilter(context, bitmap).subscribe(new SingleObserver<FilterOption>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull FilterOption filterOption) {
                Log.d("TAG", "onSuccess: ");
                liveEvent.setValue(filterOption);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("TAG", "onError: ");

            }
        });
    }
}
