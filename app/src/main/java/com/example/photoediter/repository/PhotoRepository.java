package com.example.photoediter.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.Font;
import com.example.photoediter.utils.BitmapUtils;
import com.example.photoediter.utils.DensityUtils;
import com.example.photoediter.utils.FileUtils;
import com.example.photoediter.utils.FilterOption;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoRepository {
    @Inject
    public PhotoRepository() {
    }

    public Single<List<String>> getImageFromDevice() {
        return Single.fromCallable(BitmapUtils::getImageFromDevice).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Bitmap> createBlurBitmap(Bitmap bitmap, int w, int h, int scale, int radius) {
        return Single.fromCallable(() -> BitmapUtils.fastBlur(bitmap, w, h, scale, radius)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<FilterOption> getListFilter(Context context, Bitmap bitmap) {
        return Single.fromCallable(() -> DensityUtils.getListFilter(context, bitmap)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<Font>> getListFont(String folder, Context context){
        return Single.fromCallable(() -> FileUtils.getListFont(context, folder)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<Color>> getListSticker(String folder, Context context){
        return Single.fromCallable(() -> FileUtils.getListSticker(context, folder)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<String>> getListFromAssets(String folder, Context context) {
        return Single.fromCallable(() -> FileUtils.getListFromAssets(context, folder)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<String> saveImageTotal(Bitmap bitmap,Bitmap bmSticker,Bitmap bmDraw,Context context) {
        return Single.fromCallable(() ->saveImage(bitmap,bmSticker,bmDraw,context)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<List<Color>> getColorList(int type){
        return Single.fromCallable(() ->DensityUtils.getListColor(type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public String saveImage(Bitmap bitmap,Bitmap bmSticker,Bitmap bmDraw,Context context){
        int maxH = DensityUtils.getScreenHeight(context);
        Bitmap bmSave = DensityUtils.changeSizeBitmap(bitmap, maxH);
        Bitmap bmGroup = Bitmap.createBitmap(bmSave.getWidth(), bmSave.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmGroup);
        canvas.drawBitmap(bmSave, 0, 0, null);
        Bitmap bmDraw1 = BitmapUtils.bitmapResizer(bmDraw, bmGroup.getWidth(), bmGroup.getHeight());
        canvas.drawBitmap(bmDraw1, 0, 0, null);
        Bitmap bmSticker1 = BitmapUtils.bitmapResizer(bmSticker, bmGroup.getWidth(), bmGroup.getHeight());
        canvas.drawBitmap(bmSticker1, 0, 0, null);

       return FileUtils.saveBitmapToLocal(bmGroup,100, context);
    }

}
