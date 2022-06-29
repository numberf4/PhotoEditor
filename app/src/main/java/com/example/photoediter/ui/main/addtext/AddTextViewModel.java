package com.example.photoediter.ui.main.addtext;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.Font;
import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class AddTextViewModel extends BaseViewModel {
    PhotoRepository photoRepository;
    public MutableLiveData<List<Color>> listData = new MutableLiveData<>();
    public MutableLiveData<List<Font>> listFonts = new MutableLiveData<>();

    public void getListColor(int type) {
        photoRepository.getColorList(type).subscribe(new SingleObserver<List<Color>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Color> colors) {
                listData.postValue(colors);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void getListFont(String folder, Context context) {
        photoRepository.getListFont(folder, context).subscribe(new SingleObserver<List<Font>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Font> fonts) {
                listFonts.postValue(fonts);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Inject
    public AddTextViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
}
