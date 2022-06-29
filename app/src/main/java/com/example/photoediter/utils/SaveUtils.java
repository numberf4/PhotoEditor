package com.example.photoediter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;

import androidx.fragment.app.Fragment;

import com.filter.helper.MagicFilterType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SaveUtils {
    public static SaveUtils getInstance(Fragment fragment) {
        sharedPreferences = fragment.requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return new SaveUtils();
    }

    private static SharedPreferences sharedPreferences;

    public <T> void setList(List<T> list, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(key, json).apply();
    }

    public List<MagicFilterType> getList(String key) {
        List<MagicFilterType> arrayItems = null;
        String serializedObject = sharedPreferences.getString(key, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MagicFilterType>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;

    }

}
