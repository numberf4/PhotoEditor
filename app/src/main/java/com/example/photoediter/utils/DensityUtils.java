package com.example.photoediter.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.Constant;
import com.filter.base.GPUImage;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DensityUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showKeyboard(Context context, View view , boolean isShow) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (isShow) im.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        else im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void hideNavigation(Activity activity){
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {

            activity.getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = activity.getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }
    public static Bitmap changeSizeBitmap(Bitmap bitmap, int size){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int wB=0;
        int hB=0;
        Bitmap bitmapResult = null;
        float s = (float) width/height;
        float tl =0;
        if(s>1){
            tl = (float) width/size;
            wB = size;
            hB = (int)(height/tl);
        }
        else {
            tl = (float) height/size;
            hB = size;
            wB = (int)(width/tl);
        }
        bitmapResult = Bitmap.createScaledBitmap(bitmap,wB,hB,false);

        return  bitmapResult;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static void goToLink(Context context, String link){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void shareApp(String appPackageName, Context context){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        }
        catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }
    public static boolean hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
    public static int getScreenHeight(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
    public static String[] listColor(){
        String[] list = {"FFFFFF","000001","FF0000","FFF500","22E6F2","CC00FF","03DAC5","29AF05","0A1FD5","FF5722"};
        return list;
    }
    public static List<Color> getListColor(int type){
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < listColor().length; i++) {
            Color color = new Color(listColor()[i], type);
            colors.add(color);
        }
        return colors;
    }
    public static FilterOption getListFilter(Context context, Bitmap bitmap){
        FilterManager.init(context);
        Bitmap bmScale = Bitmap.createScaledBitmap(bitmap, Constant.SIZE_FILTER,Constant.SIZE_FILTER,false);
        List<MagicFilterType> list = Arrays.asList(FilterManager.getInstance().types);
        List<Bitmap>listBMFilter = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            GPUImage gpuImage = new GPUImage(context);
            gpuImage.setImage(bmScale);
            gpuImage.setFilter(FilterManager.getInstance().getFilter(list.get(i)));
            Bitmap bitmapFilter = gpuImage.getBitmapWithFilterApplied();
            listBMFilter.add(bitmapFilter);
        }
        return new FilterOption(list,bmScale, listBMFilter);
    }
}
