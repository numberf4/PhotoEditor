package com.example.photoediter.ui.main;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.DataColor;
import com.example.photoediter.common.models.Font;
import com.example.photoediter.common.models.LiveEvent;
import com.example.photoediter.common.models.MessageEvent;
import com.example.photoediter.repository.PhotoRepository;
import com.example.photoediter.ui.base.BaseViewModel;
import com.example.photoediter.utils.FilterOption;
import com.filter.base.GPUImageFilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class MainViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> isClickItemFilter = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickItemSticker = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickItemText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickItemDraw = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> isCancelAddText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickFont = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickColorText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickColorBgText = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> isClickColorDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickSizeDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isDeleteDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isUndoDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isRedoDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isBack = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> undoSticker = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> redoSticker = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> undoTextSticker = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> redoTextSticker = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> backFilter = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backSticker = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backColorBrush = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backSizeBrush = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backFontText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backColorText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backColorBgText = new MutableLiveData<>(false);

    public MutableLiveData<GPUImageFilter> imageFilter = new MutableLiveData<>();
    public MutableLiveData<String> sticker = new MutableLiveData<>();
    public LiveEvent<MessageEvent> sticker2 = new LiveEvent<>();
    public LiveEvent<GPUImageFilter> filter = new LiveEvent<>();
    public LiveEvent<MessageEvent> text = new LiveEvent<>();
    public MutableLiveData<Integer> brushSize = new MutableLiveData<>();

    public MutableLiveData<String> pathLiveData = new MutableLiveData<>();
    public MutableLiveData<String> typeText = new MutableLiveData<>();

//    public MutableLiveData<String> contentEditText = new MutableLiveData<>();
    public MutableLiveData<String> typeFont = new MutableLiveData<>();
    public MutableLiveData<Integer> positionFont = new MutableLiveData<>();
    public MutableLiveData<DataColor> dataColor = new MutableLiveData<>();
    public MutableLiveData<DataColor> colorBrush = new MutableLiveData<>();
    public MutableLiveData<Integer> customColorBrush = new MutableLiveData<>();
    public MutableLiveData<DataColor> dataBgColor = new MutableLiveData<>();
    public MutableLiveData<Integer> colorText = new MutableLiveData<>();
    public MutableLiveData<Integer> bgColorText = new MutableLiveData<>();
    PhotoRepository photoRepository;

    @Inject
    public MainViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void setIsClickItemFilter(Boolean isClickItemFilter) {
        this.isClickItemFilter.setValue(isClickItemFilter);
    }

    public void setIsDeleteDraw(Boolean isDeleteDraw) {
        this.isDeleteDraw.setValue(isDeleteDraw);
    }

    public void setDataColor(DataColor dataColor) {
        this.dataColor.postValue(dataColor);
    }

    public void setColorBrush(DataColor colorBrush) {
        this.colorBrush.setValue(colorBrush);
    }

    public void setDataBgColor(DataColor dataBgColor) {
        this.dataBgColor.postValue(dataBgColor);
    }

    public void setCustomColorBrush(int customColorBrush) {
        this.customColorBrush.setValue(customColorBrush);
    }

    public void setBrushSize(int brushSize) {
        this.brushSize.setValue(brushSize);
    }

    public void setUndoSticker(Boolean undoSticker) {
        this.undoSticker.setValue(undoSticker);
    }

    public void setRedoSticker(Boolean redoSticker) {
        this.redoSticker.setValue(redoSticker);
    }

    public void setIsClickItemSticker(Boolean isClickItemSticker) {
        this.isClickItemSticker.setValue(isClickItemSticker);
    }

    public void setIsClickItemText(Boolean isClickItemText) {
        this.isClickItemText.setValue(isClickItemText);
    }

    public void setIsClickItemDraw(Boolean isClickItemDraw) {
        this.isClickItemDraw.setValue(isClickItemDraw);
    }

    public void setIsClickColorDraw(Boolean isClickColorDraw) {
        this.isClickColorDraw.setValue(isClickColorDraw);
    }

    public void setIsUndoDraw(Boolean isUndoDraw) {
        this.isUndoDraw.setValue(isUndoDraw);
    }

    public void setIsRedoDraw(Boolean isRedoDraw) {
        this.isRedoDraw.setValue(isRedoDraw);
    }

    public void setIsBack(Boolean isBack) {
        this.isBack.setValue(isBack);
    }

    public void setIsClickSizeDraw(Boolean isClickSizeDraw) {
        this.isClickSizeDraw.setValue(isClickSizeDraw);
    }

    public void setUndoTextSticker(Boolean undoTextSticker) {
        this.undoTextSticker.setValue(undoTextSticker);
    }

    public void setRedoTextSticker(Boolean redoTextSticker) {
        this.redoTextSticker.setValue(redoTextSticker);
    }

    public void setColorText(int colorText) {
        this.colorText.setValue(colorText);
    }

    public void setBgColorText(int bgColorText) {
        this.bgColorText.setValue(bgColorText);
    }

    public void saveBitmapToLocal(Bitmap bitmap, Bitmap bmSticker, Bitmap bmDraw, Context context) {
        photoRepository.saveImageTotal(bitmap, bmSticker, bmDraw, context).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(String s) {
                pathLiveData.postValue(s);
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }
}
