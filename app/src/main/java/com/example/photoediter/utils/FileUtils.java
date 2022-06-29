package com.example.photoediter.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileUtils {
    private static FileUtils instance = null;

    private static Context mContext;

    private static final String APP_DIR = "Abner";

    private static final String TEMP_DIR = "Abner/.TEMP";

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    mContext = context.getApplicationContext();
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }
    public static List<Font> getListFont(Context context, String folder){
        List<Font> fonts = new ArrayList<>();
        List<String> list= getListFromAssets(context,folder);
        for (int i = 0; i < list.size(); i++) {
            String[] strFont = list.get(i).split(Constant.REGEX);
            String tempStrFont = strFont[5];
            String[] fontName = tempStrFont.split(Constant.DOT);
            Font font = new Font(tempStrFont);
            font.setName(fontName[0]);
            fonts.add(font);
        }
       return fonts;
    }
    public static List<Color> getListSticker(Context context, String folder){
        List<Color> stickers = new ArrayList<>();
        List<String> list= getListFromAssets(context,folder);
        for (int i = 0; i < list.size(); i++) {
            stickers.add(new Color(list.get(i), Constant.STICKER));
        }
        return stickers;
    }

    public static List<String> getListFromAssets(Context context, String folder) {
        ArrayList<String> list = new ArrayList<>();
        try {
            String[] images = context.getAssets().list(folder);
            for (int i = 0; i < images.length; i++) {
                list.add("file:///android_asset/" + folder + File.separator + images[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static String saveBitmapToLocal(Bitmap bm,  int quality, Context context) {
        String path =null;
        try {
            File rootFile = new File(context.getCacheDir() + File.separator + "image");
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File file = new File(rootFile, Calendar.getInstance().getTimeInMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
            //Insert files into the system Gallery
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), path, null);

            //Update the database by sending broadcast notifications after saving pictures
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }
    public static String saveBitmapToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File rootFile = new File(context.getCacheDir() + File.separator + "image");
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File file = new File(rootFile, Calendar.getInstance().getTimeInMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    public File createTempFile(String prefix, String extension)
            throws IOException {
        File file = new File(getAppDirPath() + ".TEMP/" + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;
    }

    public String getAppDirPath() {
        String path = null;
        if (getLocalPath() != null) {
            path = getLocalPath() + APP_DIR + "/";
        }
        return path;
    }

    private static String getLocalPath() {
        String sdPath = null;
        sdPath = mContext.getFilesDir().getAbsolutePath() + "/";
        return sdPath;
    }

    public boolean isSDCanWrite() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory().canWrite()
                && Environment.getExternalStorageDirectory().canRead()) {
            return true;
        } else {
            return false;
        }
    }

    private FileUtils() {
        if (isSDCanWrite()) {
            creatSDDir(APP_DIR);
            creatSDDir(TEMP_DIR);
        }
    }

    public File creatSDDir(String dirName) {
        File dir = new File(getLocalPath() + dirName);
        dir.mkdirs();
        return dir;
    }
}
