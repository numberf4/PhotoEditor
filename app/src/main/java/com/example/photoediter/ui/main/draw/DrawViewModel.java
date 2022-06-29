package com.example.photoediter.ui.main.draw;

import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.LiveEvent;
import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class DrawViewModel extends BaseViewModel {
    PhotoRepository photoRepository;
    public MutableLiveData<List<Color>> listData = new MutableLiveData<>();

    @Inject
    public DrawViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getListColorBrush(int type) {
        photoRepository.getColorList(type).subscribe(new SingleObserver<List<Color>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Color> colors) {
                Log.d("HaiPd", "onSuccess get Color List: ");
                listData.postValue(colors);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}
