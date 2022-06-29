package com.example.photoediter.ui.main.sticker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.photoediter.common.models.Color;
import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class StickerViewModel extends BaseViewModel {
    PhotoRepository photoRepository;
    public MutableLiveData<List<Color>> listSticker = new MutableLiveData<>();

    @Inject
    public StickerViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getListSticker(String folder, Context context) {
        photoRepository.getListSticker(folder, context).subscribe(new SingleObserver<List<Color>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Color> colors) {
                listSticker.postValue(colors);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}
