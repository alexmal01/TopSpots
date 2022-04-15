package com.example.lokalizator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefConfig {

    public static void writeListInPref(Context context, List< LocationClass> list, String listName){

        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(listName).commit();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(listName, jsonString);
        editor.apply();
        editor.commit();
    }

    public static List<LocationClass> readListFromPref(Context context, String listName){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = sharedPreferences.getString(listName, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LocationClass>>() {}.getType();
        List<LocationClass> list = gson.fromJson(jsonString, type);

        return list;

    }
}
