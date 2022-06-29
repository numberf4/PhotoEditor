package com.example.photoediter.utils;

import android.graphics.Bitmap;

import com.filter.helper.MagicFilterType;

import java.util.List;

public class FilterOption {
    private List<MagicFilterType> listFilter;
    private Bitmap bitmap;
    private List<Bitmap> listBmFilter;

    public FilterOption(List<MagicFilterType> listFilter, Bitmap bitmap, List<Bitmap> listBmFilter) {
        this.listFilter = listFilter;
        this.bitmap = bitmap;
        this.listBmFilter = listBmFilter;
    }

    public List<Bitmap> getListBmFilter() {
        return listBmFilter;
    }

    public void setListBmFilter(List<Bitmap> listBmFilter) {
        this.listBmFilter = listBmFilter;
    }

    public List<MagicFilterType> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<MagicFilterType> listFilter) {
        this.listFilter = listFilter;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
